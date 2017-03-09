package net.catten.ssim.common.http;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by catten on 2/23/17.
 */
public interface HttpSessionHolder {

    Document post(String target, String refer, UrlEncodedFormEntity formEntity) throws IOException;
    Document get(String target, String refer) throws IOException;

    void setUserAgent(String userAgent);
    String getUserAgent();

    void setCharset(String charset);
    String getCharset();

}
