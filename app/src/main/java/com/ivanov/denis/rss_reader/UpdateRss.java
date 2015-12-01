package com.ivanov.denis.rss_reader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ivanov.denis.rss_reader.downloader.Downloader;
import com.ivanov.denis.rss_reader.constants.Constants;
import com.ivanov.denis.rss_reader.parser.Channel;
import com.ivanov.denis.rss_reader.parser.RssParser;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class UpdateRss extends AsyncTask implements Constants {

    private ArrayAdapter<String> mAdapter;
    private String mLink = "http://www.telegraf.in.ua/rss.xml";
    private MainActivity mContext;

    public UpdateRss(Context context) {
        this.mContext = (MainActivity)context;
    }

    public void update() {
        if (!isInternet()) {
                Toast.makeText(mContext, "Turn on Internet", Toast.LENGTH_SHORT).show();
            return;
        }
        this.execute();
    }

    public boolean isInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) return true;
        else return false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //mContext.setProgressDialog(true);
    }

    @Override
    protected void onPostExecute(Object o) {
        mContext.update(((Channel) o).getItems());
        //mContext.setProgressDialog(false);
        super.onPostExecute(o);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        String fullText;
        RssParser rp = new RssParser();
        try {
            fullText = (new Downloader()).download(mLink);
            rp.parse(fullText);
        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
        } catch (TimeoutException e) {
            Log.d("ERROR", e.getMessage());
        }
        return rp.getFeed();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        //mContext.setProgressDialog(false);
    }
}
