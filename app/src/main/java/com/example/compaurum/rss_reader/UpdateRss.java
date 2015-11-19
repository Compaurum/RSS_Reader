package com.example.compaurum.rss_reader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.compaurum.rss_reader.Downloader.Downloader;
import com.example.compaurum.rss_reader.constants.Constants;
import com.example.compaurum.rss_reader.parser.Channel;
import com.example.compaurum.rss_reader.parser.RssParser;

import java.io.IOException;

public class UpdateRss extends AsyncTask implements Constants {

    private ListView mLvMain;
    private ArrayAdapter<String> mAdapter;
    private String mLink = "http://www.telegraf.in.ua/rss.xml";
    private Channel mChannel = null;
    private MainActivity mContext;

    public UpdateRss(Context context, ListView lvMain) {
        this.mLvMain = lvMain;
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
        mContext.getProccess().setText("Started");
        mContext.setProgressDialog(true);
    }

    @Override
    protected void onPostExecute(Object o) {
        mContext.update(((Channel) o).getItems());
        mContext.setProgressDialog(false);
        super.onPostExecute(o);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        String fullText = null;
        RssParser rp = new RssParser();
        try {
            fullText = (new Downloader(UpdateRss.this)).download(mLink);
            rp.parse(fullText);
        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
        }catch (Exception e){
            Log.d("ERROR", e.getMessage());
        }
        mChannel = rp.getFeed();

        return mChannel;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mContext.setProgressDialog(false);
    }
}
