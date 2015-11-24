package com.ivanov.denis.rss_reader.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.ivanov.denis.rss_reader.UpdateRssService;

public class RSSReaderService extends Service {
    String LOG_TAG = "MYSERVICE";
    public RSSReaderService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "MyService onCreate");
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "MyService onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "MyService startCommand");
        new UpdateRssService(this).update();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
