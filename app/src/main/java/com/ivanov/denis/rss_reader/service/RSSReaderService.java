package com.ivanov.denis.rss_reader.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.util.Log;

import com.ivanov.denis.rss_reader.DBHelper.DBHelper;
import com.ivanov.denis.rss_reader.DBHelper.MyDBTools;
import com.ivanov.denis.rss_reader.MainActivity;
import com.ivanov.denis.rss_reader.R;
import com.ivanov.denis.rss_reader.UpdateRssService;

import java.util.Timer;
import java.util.TimerTask;

public class RSSReaderService extends Service {
    String LOG_TAG = "MYSERVICE";
    NotificationManager notificationManager;
    Timer timer;
    TimerTask timerTask;
    //private static final long PERIOD = 3600000; //  1 hour
    private static final long PERIOD = 60000;   //   1 minute

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        timer = new Timer();
        Log.d(LOG_TAG, "MyService onCreate");
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "MyService onDestroy");
        timer.cancel();
        timerTask.cancel();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "MyService startCommand");
        if (timerTask != null) timerTask.cancel();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                updateFeeds();
            }
        };
        timer.schedule(timerTask, 0, PERIOD);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateFeeds(){
        new UpdateRssService(this).update();
    }

    public void sendNotif() {
        // 1-я часть
        Notification notif = new Notification(R.drawable.logo, getString(R.string.added_news),
                System.currentTimeMillis());

        // 3-я часть
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // 2-я часть
        notif.setLatestEventInfo(this, getString(R.string.Telegraph), new MyDBTools(new DBHelper(this)).countUnreadNews()+getString(R.string.unread_news), pIntent);

        // ставим флаг, чтобы уведомление пропало после нажатия
        notif.flags |= Notification.FLAG_AUTO_CANCEL;

        // отправляем
        notificationManager.notify(1, notif);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
