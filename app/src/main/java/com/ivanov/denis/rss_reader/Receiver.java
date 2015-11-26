package com.ivanov.denis.rss_reader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ivanov.denis.rss_reader.constants.Constants;

public class Receiver extends BroadcastReceiver implements Constants{
    String LOG_TAG = "SERVICE";
    private MainActivity mContext;

    public Receiver(Context context){
        this.mContext = (MainActivity)context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mContext == null) return;
        switch (intent.getAction()){
            case BROADCAST_ACTION:
                if (intent.getIntExtra(STATUS_DOWNLOADING, 999) == START_DOWNLOADING){
                    mContext.setProgressDialog(true);
                } else if (intent.getIntExtra(STATUS_DOWNLOADING, 999) == END_DOWNLOADING){
                    mContext.setProgressDialog(false);
                    mContext.loadFromBase(null, false);
                }else if (intent.getIntExtra(STATUS_DOWNLOADING, 999) == ERROR_DOWNLOADING){
                    mContext.setProgressDialog(false);
                }
        }
        mContext.updateListView();
        Log.d(LOG_TAG, "STATUS_DOWNLOADING: " + intent.getIntExtra(STATUS_DOWNLOADING, 999));
        Log.d(LOG_TAG, "STATUS_DOWNLOADING: " + intent.getAction());
    }
}
