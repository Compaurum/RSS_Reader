package com.example.compaurum.rss_reader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.compaurum.rss_reader.MainActivity;
import com.example.compaurum.rss_reader.parser.Channel;
import com.example.compaurum.rss_reader.parser.RssParser;

/**
 * Created by compaurum on 27.10.2015.
 */
public class UpdateRss{

    private ListView mLvMain;
    private ArrayAdapter<String> mAdapter;
    private String mLink = "http://www.telegraf.in.ua/rss.xml";
    private Channel mChannel = null;
    private MainActivity mContext;
    private Message msg;

    public UpdateRss(MainActivity context, ListView lvMain) {
        this.mLvMain = lvMain;
        this.mContext = context;
    }

    public void update() {
        if (!isInternet()) {
            Toast.makeText(mContext, "Turn on Internet", Toast.LENGTH_SHORT).show();
            return;
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mContext.getHandler().sendEmptyMessage(mContext.START_DOWNLOADING);
                mContext.getHandler().sendEmptyMessage(mContext.DOWNLOADING);
                RssParser rp = new RssParser(mLink);
                rp.parse();
                mChannel = rp.getFeed();
                if (mChannel != null) {
                    msg = mContext.getHandler().obtainMessage(mContext.END_DOWNLOADING, mChannel);
                    mContext.getHandler().sendMessage(msg);
                }
            }
        });
        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        if (mChannel != null) {
//            mContext.updateList(mChannel.getItems());
//        }
    }

    public boolean isInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) return true;
        else return false;
    }
}
