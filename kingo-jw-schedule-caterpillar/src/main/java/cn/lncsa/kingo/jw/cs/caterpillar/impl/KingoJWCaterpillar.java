package cn.lncsa.kingo.jw.cs.caterpillar.impl;

import cn.lncsa.ssim.common.events.TickEventReceiver;
import cn.lncsa.ssim.common.http.HttpSessionHolder;
import cn.lncsa.ssim.common.http.impl.HttpSessionHolderImpl;
import cn.lncsa.ssim.schedule.CourseScheduleCaterpillar;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by catten on 11/17/16.
 */
public class KingoJWCaterpillar implements CourseScheduleCaterpillar {
    private Properties loginProperties;
    private Properties stringProperties;
    private int delayMS;

    private Logger logger = LoggerFactory.getLogger(KingoJWCaterpillar.class);

    private HttpSessionHolder httpSessionHolder = new HttpSessionHolderImpl();

    private TickEventReceiver tickEventReceiver = message -> {
        logger.info("Working : " + message[0] + "/" + message[1] + " - " + message[2]);
    };

//    public KingoJWCaterpillar(Properties loginProperties) throws IOException {
//        init(loginProperties);
//    }

    public KingoJWCaterpillar() {
        logger.info("Using default tick receiver : logger ");
    }

    static String getEncryptedPassword(String username, String password) {
        return md5(username + md5(password).substring(0, 30).toUpperCase() + "12749").substring(0, 30).toUpperCase();
    }

    private static String md5(String s) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ignore) {

        }
        // 计算md5函数
        assert md != null;
        md.update(s.getBytes());
        // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
        return new BigInteger(1, md.digest()).toString(16);
    }

    private void init(Properties loginProperties) throws IOException {
        this.stringProperties = new Properties();
        this.loginProperties = loginProperties;

        stringProperties.load(KingoJWCaterpillar.class.getResourceAsStream("/cn/lncsa/kingo/jw/cs/caterpillar/strings.properties"));
        logger.info("Properties Loaded.");

        setUserAgent(loginProperties.getProperty("userAgent"));
        logger.info("Using User-Agent : " + getUserAgent());
        setCharset(loginProperties.getProperty("charset"));
        logger.info("Using encoding : " + getCharset());

        delayMS = Integer.parseInt(loginProperties.getProperty("delay"));
        logger.info("Page capture delay : " + String.valueOf(delayMS) + " ms.");
    }

    /**
     * Initialize the Session, logon server
     *
     * @return
     * @throws IOException
     */
    public boolean initializeSession() throws IOException {

        String loginPageAddress = stringProperties.getProperty("loginPageAddress");
        Document resultPage;

        //Try to get indexPage
        resultPage = httpSessionHolder.get(loginPageAddress, loginPageAddress);

        FormElement theForm = (FormElement) resultPage.getElementsByTag("form").first();
        if (!Validate.isForm(theForm) && theForm.elements().size() <= 0) {
            logger.warn("Login form not found, maybe the website was close or under construction.");
            return false;
        }

        //Try to login
        resultPage = httpSessionHolder.post(loginPageAddress, loginPageAddress, prepareLoginData(theForm));

        Element element = resultPage.getElementById("divLogNote").child(0);
        if (!element.text().equals(stringProperties.getProperty("loginSuccessTip"))) {
            logger.warn("Login result : " + element.text());
            return false;
        }
        return true;
    }

    /**
     * Get Term list.
     *
     * @return
     * @throws IOException
     */
    @Override
    public Map<String, String> getTermsFromRemote() throws IOException {

        String queryPage = stringProperties.getProperty("classInfoQueryPage");

        //Get page
        Document resultPage = httpSessionHolder.get(queryPage, queryPage);

        //Analyze page
        FormElement theForm = (FormElement) resultPage.getElementById("form1");
        Elements termSelector = theForm.select("option");
        Map<String, String> resultMap = new HashMap<>();
        for (Element option : termSelector) {
            resultMap.put(option.attr("value"), option.text());
        }
        return resultMap;
    }

    /**
     * Get subject list of the term
     *
     * @param termCode
     * @return
     * @throws IOException
     */
    @Override
    public Map<String, String> getCoursesFromRemote(String termCode) throws IOException {

        String queryPage = stringProperties.getProperty("subjectListQueryPage") + termCode;
        String referPage = stringProperties.getProperty("classInfoQueryPage");

        //Get page
        Document resultPage = httpSessionHolder.get(queryPage, referPage);

        //Analyze page
        Element scriptElement = resultPage.getElementsByTag("script").first();
        if (scriptElement != null) {
            String script = scriptElement.html();
            script = script.substring(script.indexOf("<"), script.lastIndexOf(">"));
            Element selectorElement = Jsoup.parse(script).select("select").first();
            Map<String, String> resultMap = new HashMap<>();
            for (Element option : selectorElement.children()) {
                resultMap.put(option.attr("value"), option.text());
            }
            resultMap.remove("");
            return resultMap;
        }
        return null;
    }

    /**
     * Get all subjects in a term and save them to file.
     *
     * @param termCode
     * @param outputFolder
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public int getTermSubjectToFiles(String termCode, File outputFolder) throws IOException, InterruptedException {

        if(!outputFolder.exists() || !outputFolder.mkdir()){
            logger.error("Can't make output directory : " + outputFolder.getAbsolutePath());
            return -1;
        }

        if (!outputFolder.isDirectory()) {
            logger.error("specified path is not a directory : " + outputFolder.getAbsolutePath());
            return -1;
        }

        File[] files = outputFolder.listFiles();
        if (files != null && files.length > 0) {
            logger.info("Cleaning old files...");
            for (File file : files) {
                Files.delete(file.toPath());
            }
            logger.info("Cleaning finished.");
        }

        if (isLoginExpire()) initializeSession();
        logger.info("checking login status finished");

        String referPage = stringProperties.getProperty("classInfoQueryPage");
        String targetPage = stringProperties.getProperty("subjectQueryPage");


        LinkedList<NameValuePair> nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("Sel_XNXQ", termCode));
        nameValuePairs.add(new BasicNameValuePair("gs", loginProperties.getProperty("tableFormat")));
        nameValuePairs.add(new BasicNameValuePair("txt_yzm", ""));
        nameValuePairs.addLast(new BasicNameValuePair("Sel_KC", ""));

        logger.info("Capturing subject data...");
        Map<String, String> lessonListOfTheTerm = getCoursesFromRemote(termCode);
        int total = lessonListOfTheTerm.size();
        int current = 0;
        logger.info("total item count - " + lessonListOfTheTerm.keySet().size());
        for (String key : lessonListOfTheTerm.keySet()) {
            current++;
            nameValuePairs.removeLast();
            nameValuePairs.addLast(new BasicNameValuePair("Sel_KC", key));

            File file = new File(outputFolder, key + ".html");
            FileWriterWithEncoding fileWriter = new FileWriterWithEncoding(file, "UTF-8");
            fileWriter.write(httpSessionHolder.post(targetPage, referPage, new UrlEncodedFormEntity(nameValuePairs)).toString());
            fileWriter.flush();
            fileWriter.close();

            tickEventReceiver.tick(total, current, key);
            Thread.sleep(delayMS);
        }
        System.out.println();
        logger.info("Capture finished.");
        return current;
    }

    /**
     * Get a page , if the validate field in the from is require, return true.
     *
     * @return
     * @throws IOException
     */
    @Override
    public boolean isLoginExpire() throws IOException {

        String queryPage = stringProperties.getProperty("classInfoQueryPage");

        Document resultPage = httpSessionHolder.get(queryPage, queryPage);

        Element element = resultPage.getElementById("txt_yzm");
        boolean result = !element.parent().attr("style").equals("display:none");
        logger.info("login result - " + (!result ? "failed" : "success"));
        return result;
    }

    /**
     * Preparing logon form data
     *
     * @param formElement
     * @return
     * @throws UnsupportedEncodingException
     */
    private UrlEncodedFormEntity prepareLoginData(FormElement formElement) throws UnsupportedEncodingException {
        List<NameValuePair> nameValuePairs = new LinkedList<>();
        for (Element element : formElement.elements()) {
            if (element.hasAttr("name")) {
                String attrName = element.attr("name");
                switch (attrName) {
                    case "pcInfo":
                        nameValuePairs.add(new BasicNameValuePair(attrName, getUserAgent()));
                        break;
                    case "dsdsdsdsdxcxdfgfg":
                        nameValuePairs.add(new BasicNameValuePair(attrName, getEncryptedPassword(loginProperties.getProperty("username"), loginProperties.getProperty("password"))));
                        break;
                    case "Sel_Type":
                        nameValuePairs.add(new BasicNameValuePair(attrName, loginProperties.getProperty("role")));
                        break;
                    case "txt_asmcdefsddsd":
                        nameValuePairs.add(new BasicNameValuePair(attrName, loginProperties.getProperty("username")));
                        break;
                    case "typeName":
                        Elements selectElement = formElement.select("select");
                        nameValuePairs.addAll(
                                selectElement.stream().filter(
                                        e -> e.attr("value").equals(loginProperties.getProperty("role")))
                                        .map(e -> new BasicNameValuePair(attrName, e.text()))
                                        .collect(Collectors.toList()));
                        break;
                    default:
                        nameValuePairs.add(new BasicNameValuePair(attrName, element.attr("value")));
                        break;
                }
            }
        }
        return new UrlEncodedFormEntity(nameValuePairs, getCharset());
    }

    @Override
    public String getUserAgent() {
        return httpSessionHolder.getUserAgent();
    }

    @Override
    public void setUserAgent(String userAgent) {
        httpSessionHolder.setUserAgent(userAgent);
    }

    @Override
    public void setTickReceiver(TickEventReceiver tickEventReceiver) {
        this.tickEventReceiver = tickEventReceiver;
        logger.info("Using tick receiver : " + tickEventReceiver.getClass());
    }

    @Override
    public String getCharset() {
        return httpSessionHolder.getCharset();
    }

    @Override
    public void setCharset(String charset) {
        httpSessionHolder.setCharset(charset);
    }

    @Override
    public Properties getLoginProperties() {
        return loginProperties;
    }

    @Override
    public void setLoginProperties(Properties loginProperties) throws IOException {
        init(loginProperties);
        this.loginProperties = loginProperties;
    }
}
