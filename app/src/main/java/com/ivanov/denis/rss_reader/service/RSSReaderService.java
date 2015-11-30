package com.ivanov.denis.rss_reader.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.ivanov.denis.rss_reader.DBHelper222.DBHelper;
import com.ivanov.denis.rss_reader.DBHelper222.MyDBTools;
import com.ivanov.denis.rss_reader.MainActivity;
import com.ivanov.denis.rss_reader.R;
import com.ivanov.denis.rss_reader.UpdateRssService;

import java.util.Timer;
import java.util.TimerTask;

public class RSSReaderService extends Service {
    String LOG_TAG = "MYSERVICE";
    private NotificationManager mNotificationManager;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private boolean mNotificate = true;
    //private static final long PERIOD = 3600000; //  1 hour
    private static final long PERIOD = 60000;   //   1 minute

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mTimer = new Timer();
        Log.d(LOG_TAG, "MyService onCreate");
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "MyService onDestroy");
        mTimer.cancel();
        //mTimerTask.cancel();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "MyService startCommand");
        if (mTimerTask != null) mTimerTask.cancel();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                updateFeeds();
            }
        };
        mTimer.schedule(mTimerTask, 0, PERIOD);
        return START_STICKY;
    }

    private void updateFeeds(){
        new UpdateRssService(this).update();
    }

    public void sendNotification() {
        if (!mNotificate) return;
        // 1-я часть
        Notification notification = new Notification(R.drawable.logo, getString(R.string.added_news),
                System.currentTimeMillis());
        // 3-я часть
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        // 2-я часть
        MyDBTools myDBTools = new MyDBTools(new DBHelper(this));
        notification.setLatestEventInfo(this, getString(R.string.Telegraph), myDBTools.countUnreadNews()+getString(R.string.unread_news), pIntent);
        myDBTools.close();
        // ставим флаг, чтобы уведомление пропало после нажатия
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        // отправляем
        mNotificationManager.notify(1, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        mNotificate = false;
        return new ServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mNotificate = true;
        return true;
    }

    public class ServiceBinder extends Binder{
        public void cancelNotification(){
            mNotificate = false;
            mNotificationManager.cancelAll();
        }
    }
}
