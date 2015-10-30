package com.example.compaurum.rss_reader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.compaurum.rss_reader.parser.Channel;
import com.example.compaurum.rss_reader.parser.RssParser;

/**
 * Created by compaurum on 27.10.2015.
 */
public class UpdateRss implements View.OnClickListener {

    private ListView mLvMain;
    private ArrayAdapter<String> mAdapter;
    private String mLink = "http://www.telegraf.in.ua/rss.xml";
    private Channel feed = null;
    private MainActivity mContext;

    public UpdateRss(MainActivity context, ListView lvMain) {
        this.mLvMain = lvMain;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {

        ConnectivityManager connMgr = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {


            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    RssParser rp = new RssParser(mLink);
                    rp.parse();
                    feed = rp.getFeed();
                    Log.d("ERROR", feed.toString());
                    Log.d("ERROR", feed.getTitle() + " ");
                    //for (int i = 0; i < feed.getItems().size(); i++) {
                       // Log.d("FEED", feed.getItems().get(i).toString());
                    //}
                }
            });
            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (feed != null){
                mContext.getNames().clear();
                mContext.getNames().addAll(feed.getItems());
                mContext.getAdapter().notifyDataSetChanged();
            }
        } else {
            Toast.makeText(mContext, "Turn on Internet", Toast.LENGTH_SHORT).show();
        }
    }
}
