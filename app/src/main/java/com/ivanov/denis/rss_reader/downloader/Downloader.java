package com.ivanov.denis.rss_reader.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeoutException;

public class Downloader {

    private String mUrlString;
    private InputStream mUrlInputStream;
    private String mFullText = "";

    public Downloader() {}

    public String download(String url) throws NullPointerException, IOException, TimeoutException {
        if (url == null) {
            throw new NullPointerException("Null url string");
        } else
            this.mUrlString = url;
        return download();
    }

    private String download() throws IOException,TimeoutException{
        URL url = new URL(this.mUrlString);
        //_setProxy(); // Set the proxy if needed
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            mUrlInputStream = conn.getInputStream();
        }

        char[] chars = new char[8192];
        int k;
        InputStreamReader inputStreamReader = new InputStreamReader(mUrlInputStream, "cp1251");
        while ((k = inputStreamReader.read(chars)) > 0) {
            mFullText += new String(chars, 0, k);
        }

        if (mUrlInputStream != null) mUrlInputStream.close();
        return mFullText;
    }
}
