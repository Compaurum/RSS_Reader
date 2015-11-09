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

import com.example.compaurum.rss_reader.Downloader.Downloader;
import com.example.compaurum.rss_reader.Interfaces.Constants;
import com.example.compaurum.rss_reader.MainActivity;
import com.example.compaurum.rss_reader.parser.Channel;
import com.example.compaurum.rss_reader.parser.RssParser;

import java.io.IOException;
import java.net.SocketException;

/**
 * Created by compaurum on 27.10.2015.
 */
public class UpdateRss implements Constants {

    private ListView mLvMain;
    private ArrayAdapter<String> mAdapter;
    private String mLink = "http://www.telegraf.in.ua/rss.xml";
    private Channel mChannel = null;
    private MainActivity mContext;

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
                String fullText = null;
                messageToHandler(START_DOWNLOADING);
                messageToHandler(DOWNLOADING);
                RssParser rp = new RssParser();
                try {
                    fullText = (new Downloader(UpdateRss.this)).download(mLink);
                    rp.parse(fullText);
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    Log.d("ERROR", e.getMessage());
                    messageToHandler(ERROR_DOWNLOADING);
                }
                //rp.parse();
                mChannel = rp.getFeed();
                if (mChannel != null) {
                    messageToHandler(END_DOWNLOADING, mChannel);
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

    public void messageToHandler(int what) {
        mContext.getHandler().sendEmptyMessage(what);
    }

    public void messageToHandler(int what, Object object) {
        Message msg = mContext.getHandler().obtainMessage(what, object);
        mContext.getHandler().sendMessage(msg);
    }

    public void messageToHandler(int what, int arg) {
        Message msg = mContext.getHandler().obtainMessage(what, arg, arg);
        mContext.getHandler().sendMessage(msg);
    }
}
