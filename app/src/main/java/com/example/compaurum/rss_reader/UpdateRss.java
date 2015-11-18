package com.example.compaurum.rss_reader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.compaurum.rss_reader.Downloader.Downloader;
import com.example.compaurum.rss_reader.Interfaces.Constants;
import com.example.compaurum.rss_reader.MainActivity;
import com.example.compaurum.rss_reader.parser.Channel;
import com.example.compaurum.rss_reader.parser.RssParser;

import java.io.IOException;
import java.net.SocketException;

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
        super.onPostExecute(o);
        mContext.updateList(((Channel) o).getItems());
        mContext.setProgressDialog(false);
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
