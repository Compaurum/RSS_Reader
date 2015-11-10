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

/**
 * Created by compaurum on 09.11.2015.
 */
public class Downloader {

    private String mUrlString;
    InputStream urlInputStream;
    String fullText = "";
    private UpdateRss mUpdateRss = null;
    private long total = 0;
    private long length = 0;

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
            urlInputStream = conn.getInputStream();
        }

        char[] chars = new char[512];
        int k;
        length = conn.getContentLength();
        //urlInputStream.read(buffer);
        InputStreamReader inputStreamReader = new InputStreamReader(urlInputStream, "cp1251");
        while ((k = inputStreamReader.read(chars)) > 0) {
            //System.out.println(k);
            fullText += new String(chars, 0, k);
            total += k;
        }

        if (urlInputStream != null) urlInputStream.close();
        return fullText;
    }

//    private void publishProgress(int what) {
//        mUpdateRss.messageToHandler(what);
//    }

}
