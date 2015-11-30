package com.ivanov.denis.rss_reader;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ivanov.denis.rss_reader.DBHelper.DBHelper;
import com.ivanov.denis.rss_reader.DBHelper.MyDBTools;
import com.ivanov.denis.rss_reader.Downloader.Downloader;
import com.ivanov.denis.rss_reader.constants.Constants;
import com.ivanov.denis.rss_reader.parser.Channel;
import com.ivanov.denis.rss_reader.parser.Items;
import com.ivanov.denis.rss_reader.parser.RssParser;
import com.ivanov.denis.rss_reader.service.RSSReaderService;

import java.io.IOException;

public class UpdateRssService extends AsyncTask implements Constants {

    private String mLink = "http://www.telegraf.in.ua/rss.xml";
    private Service mService;

    public UpdateRssService(Service service){
        this.mService = service;
    }
    public void update() {
        if (!isInternet()) {
            //Toast.makeText(mService, "Turn on Internet", Toast.LENGTH_SHORT).show();
            return;
        }
        this.execute();
    }

    public boolean isInternet() {
        return isInternet(mService);
    }

    public static boolean isInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return  (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    protected void onPreExecute() {
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra(STATUS_DOWNLOADING, START_DOWNLOADING);
        mService.sendBroadcast(intent);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        Items items = ((Channel) o).getItems();
        MyDBTools myDBTools = new MyDBTools(new DBHelper(mService));
        myDBTools.insert(items);
        myDBTools.close();
        myDBTools = null;

        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra(STATUS_DOWNLOADING, END_DOWNLOADING);
        mService.sendBroadcast(intent);
        ((RSSReaderService)mService).sendNotification();
        super.onPostExecute(o);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        String fullText = null;
        RssParser rp = new RssParser();
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra(STATUS_DOWNLOADING, DOWNLOADING);
        mService.sendBroadcast(intent);
        try {
            fullText = (new Downloader()).download(mLink);
            rp.parse(fullText);
        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
        }catch (Exception e){
            Log.d("ERROR", e.getMessage());
        }
        Channel mChannel = rp.getFeed();
        if (mChannel == null) cancel(true);
        return mChannel;
    }

    @Override
    protected void onCancelled() {
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra(STATUS_DOWNLOADING, ERROR_DOWNLOADING);
        mService.sendBroadcast(intent);
        super.onCancelled();
    }
}
