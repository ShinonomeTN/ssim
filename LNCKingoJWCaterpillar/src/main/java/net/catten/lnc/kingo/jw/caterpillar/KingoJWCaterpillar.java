package net.catten.lnc.kingo.jw.caterpillar;

import net.catten.ssim.common.events.TickEventProvider;
import net.catten.ssim.common.events.TickEventReceiver;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by catten on 11/17/16.
 */
public class KingoJWCaterpillar implements TickEventProvider {
    private Properties loginProperties;
    private Properties stringProperties;
    private String userAgent;
    private String charset;
    private int delayMS;

    private Logger logger = Logger.getLogger(KingoJWCaterpillar.class.toString());

    private HttpContext websiteContext;
    private CookieStore cookieStore;

    private HttpClient httpClient;

    private TickEventReceiver tickEventReceiver = message -> {
        //do nothing...
    };

    public KingoJWCaterpillar(Properties loginProperties) throws IOException {
        this.loginProperties = new Properties();
        this.stringProperties = new Properties();
        this.loginProperties = loginProperties;
        stringProperties.load(KingoJWCaterpillar.class.getResourceAsStream("/strings.properties"));
        logger.info("Properties Loaded.");

        cookieStore = new BasicCookieStore();
        websiteContext = new BasicHttpContext();
        websiteContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
        logger.info("Website context initialized.");

        userAgent = loginProperties.getProperty("userAgent");
        logger.info("Using User-Agent : " + userAgent);
        charset = loginProperties.getProperty("charset");
        logger.info("Using encoding : " + charset);
        delayMS = Integer.parseInt(loginProperties.getProperty("delay"));
        logger.info("Page capture delay : " + String.valueOf(delayMS) + " ms.");

        httpClient = HttpClients.createDefault();
        logger.info("HTTP Client started.");
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

    /**
     * Initialize the Session, logon server
     *
     * @return
     * @throws IOException
     */
    public boolean initializeSession() throws IOException {

        String loginPageAddress = stringProperties.getProperty("loginPageAddress");
        Document resultPage;

        CloseableHttpResponse response;

        //Try to get indexPage
        logger.info("Requesting address : " + loginPageAddress);
        response = getResponse(loginPageAddress, loginPageAddress);
        resultPage = Jsoup.parse(response.getEntity().getContent(), charset, "");
        response.close();

        FormElement theForm = (FormElement) resultPage.getElementsByTag("form").first();
        if (theForm.elements().size() < 1) {
            logger.warning("Login form not found, maybe the website was close or under construction.");
            return false;
        }

        //Try to login
        response = postResponse(loginPageAddress, loginPageAddress, prepareLoginData(theForm));
        resultPage = Jsoup.parse(response.getEntity().getContent(), charset, "");
        response.close();

        Element element = resultPage.getElementById("divLogNote").child(0);
        if (!element.text().equals(stringProperties.getProperty("loginSuccessTip"))) {
            logger.warning("Login result : " + element.text());
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
    public Map<String, String> getTerms() throws IOException {

        String queryPage = stringProperties.getProperty("classInfoQueryPage");
        //Get page
        CloseableHttpResponse response = getResponse(queryPage, queryPage);
        Document resultPage = Jsoup.parse(response.getEntity().getContent(), charset, "");
        response.close();
        logger.info("request finished.");

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
    public Map<String, String> getSubjects(Integer termCode) throws IOException {

        String queryPage = stringProperties.getProperty("subjectListQueryPage") + String.valueOf(termCode);
        String referPage = stringProperties.getProperty("classInfoQueryPage");
        //Get page
        CloseableHttpResponse response = getResponse(queryPage, referPage);
        Document resultPage = Jsoup.parse(response.getEntity().getContent(), charset, "");
        response.close();
        logger.info("request finished.");

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
     * Get all subjects in a term and save them to an array.
     *
     * @param termCode
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public Map<Integer, String> getTermSubjectToMemory(Integer termCode) throws IOException, InterruptedException {

        if (isLoginExpire()) initializeSession();
        logger.info("checking login status finished");

        String referPage = stringProperties.getProperty("classInfoQueryPage");
        String targetPage = stringProperties.getProperty("subjectQueryPage");


        LinkedList<NameValuePair> nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("Sel_XNXQ", String.valueOf(termCode)));
        nameValuePairs.add(new BasicNameValuePair("gs", loginProperties.getProperty("tableFormat")));
        nameValuePairs.add(new BasicNameValuePair("txt_yzm", ""));
        nameValuePairs.addLast(new BasicNameValuePair("Sel_KC", ""));

        Map<Integer, String> results = new HashMap<>();

        logger.info("Capturing subject data...");
        Map<String, String> lessonListOfTheTerm = getSubjects(termCode);
        int total = lessonListOfTheTerm.size();
        int current = 0;
        logger.info("total item count - " + lessonListOfTheTerm.keySet().size());
        for (String key : lessonListOfTheTerm.keySet()) {
            current++;
            nameValuePairs.removeLast();
            nameValuePairs.addLast(new BasicNameValuePair("Sel_KC", key));
            results.put(Integer.parseInt(key), getSingleSubjectPage(referPage, targetPage, new UrlEncodedFormEntity(nameValuePairs)));
            tickEventReceiver.tick(total, current, key);
            Thread.sleep(delayMS);
        }
        System.out.println();
        logger.info("Capture finished.");
        return results;
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
    public int getTermSubjectToFiles(Integer termCode, File outputFolder) throws IOException, InterruptedException {

        if (!outputFolder.isDirectory()) {
            logger.warning("specified path is not a directory : " + outputFolder.getAbsolutePath());
            return -1;
        }

        if (isLoginExpire()) initializeSession();
        logger.info("checking login status finished");

        String referPage = stringProperties.getProperty("classInfoQueryPage");
        String targetPage = stringProperties.getProperty("subjectQueryPage");


        LinkedList<NameValuePair> nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("Sel_XNXQ", String.valueOf(termCode)));
        nameValuePairs.add(new BasicNameValuePair("gs", loginProperties.getProperty("tableFormat")));
        nameValuePairs.add(new BasicNameValuePair("txt_yzm", ""));
        nameValuePairs.addLast(new BasicNameValuePair("Sel_KC", ""));

        logger.info("Capturing subject data...");
        Map<String, String> lessonListOfTheTerm = getSubjects(termCode);
        int total = lessonListOfTheTerm.size();
        int current = 0;
        logger.info("total item count - " + lessonListOfTheTerm.keySet().size());
        for (String key : lessonListOfTheTerm.keySet()) {
            current++;
            nameValuePairs.removeLast();
            nameValuePairs.addLast(new BasicNameValuePair("Sel_KC", key));

            File file = new File(outputFolder, key + ".html");
            FileWriterWithEncoding fileWriter = new FileWriterWithEncoding(file, "UTF-8");
            fileWriter.write(getSingleSubjectPage(referPage, targetPage, new UrlEncodedFormEntity(nameValuePairs)));
            fileWriter.flush();
            fileWriter.close();

            tickEventReceiver.tick(total, current, key);
            Thread.sleep(delayMS);
        }
        System.out.println();
        logger.info("Capture finished.");
        return current;
    }

    private String getSingleSubjectPage(String refer, String targetPage, UrlEncodedFormEntity formEntity) throws IOException {
        CloseableHttpResponse response = postResponse(targetPage, refer, formEntity);
        String result = IOUtils.toString(response.getEntity().getContent(), charset);
        response.close();
        return result;
    }

    /**
     * Get a page , if the validate field in the from is require, return true.
     *
     * @return
     * @throws IOException
     */
    public boolean isLoginExpire() throws IOException {

        String queryPage = stringProperties.getProperty("classInfoQueryPage");

        CloseableHttpResponse response = getResponse(queryPage, queryPage);
        Document resultPage = Jsoup.parse(response.getEntity().getContent(), charset, "");
        response.close();
        logger.info("request finished.");

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
                        nameValuePairs.add(new BasicNameValuePair(attrName, userAgent));
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
        return new UrlEncodedFormEntity(nameValuePairs, charset);
    }

    /**
     * Set user-agent as Chrome(Linux) and set refer
     *
     * @param request
     * @param refer
     */
    private void prepareBaseHeader(HttpRequestBase request, String refer) {
        request.setHeader("Referer", refer);
        request.setHeader("User-Agent", userAgent);
    }

    /**
     * Factory method for GET request
     *
     * @param targetPage
     * @param refer
     * @return
     * @throws IOException
     */
    private CloseableHttpResponse getResponse(String targetPage, String refer) throws IOException {
        HttpGet httpGet = new HttpGet(targetPage);
        prepareBaseHeader(httpGet, refer);
        return (CloseableHttpResponse) httpClient.execute(httpGet, websiteContext);
    }

    /**
     * Factory method for POST request
     *
     * @param targetPage
     * @param refer
     * @param formEntity
     * @return
     * @throws IOException
     */
    private CloseableHttpResponse postResponse(String targetPage, String refer, UrlEncodedFormEntity formEntity) throws IOException {
        HttpPost httpPost = new HttpPost(targetPage);
        prepareBaseHeader(httpPost, refer);
        httpPost.setEntity(formEntity);
        return (CloseableHttpResponse) httpClient.execute(httpPost, websiteContext);
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public void setTickReceiver(TickEventReceiver tickEventReceiver) {
        this.tickEventReceiver = tickEventReceiver;
    }
}
