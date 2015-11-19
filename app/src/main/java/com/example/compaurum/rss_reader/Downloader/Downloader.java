package com.example.compaurum.rss_reader.Downloader;

import android.app.ProgressDialog;
import android.util.Log;

import com.example.compaurum.rss_reader.MainActivity;
import com.example.compaurum.rss_reader.UpdateRss;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

public class Downloader {

    private String mUrlString;
    private InputStream mUrlInputStream;
    private String mFullText = "";
    private UpdateRss mUpdateRss = null;

    public Downloader(UpdateRss updateRss) {
        this.mUpdateRss = updateRss;
    }
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
