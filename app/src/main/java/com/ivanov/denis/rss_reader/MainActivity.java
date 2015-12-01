package com.ivanov.denis.rss_reader;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ivanov.denis.rss_reader.dbHelper.DBHelper;
import com.ivanov.denis.rss_reader.dbHelper.MyDBTools;
import com.ivanov.denis.rss_reader.adapter.ListAdapter;
import com.ivanov.denis.rss_reader.constants.Constants;
import com.ivanov.denis.rss_reader.dialog.YesNoDialog;
import com.ivanov.denis.rss_reader.dialog.YesNoDialogListener;
import com.ivanov.denis.rss_reader.parser.Item;
import com.ivanov.denis.rss_reader.parser.Items;
import com.ivanov.denis.rss_reader.service.RSSReaderService;


public class MainActivity extends ActionBarActivity implements Constants, YesNoDialogListener {

    private Items mFeeds = new Items();
    private boolean mFavorite = false;
    private ListView mLvMain;
    private ListAdapter mAdapter;
    private boolean mUpdateButtonEnabled = true;
    private ProgressDialog mProgressDialog;
    private BroadcastReceiver mReceiver;
    private ServiceConnection mServiceConnection;
    private boolean mBound = false;
    private SharedPreferences mSharedPreferences;
    Intent rssReaderServiceIntent;
    private MyDBTools mMyDbTools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MAINACTIVITY", "ONCREATE");
        setContentView(R.layout.activity_main);

        rssReaderServiceIntent = new Intent(this, RSSReaderService.class);
        mMyDbTools = new MyDBTools(DBHelper.getInstance(this));
        mLvMain = (ListView) findViewById(android.R.id.list);
        mAdapter = new ListAdapter(this, mFeeds);
        mLvMain.setAdapter(mAdapter);
        mLvMain.setOnItemClickListener(mOnItemClickListener);
        mLvMain.setOnItemLongClickListener(mOnItemLongClickListener);
        mReceiver = new Receiver(this);
        registerReceiver(mReceiver, new IntentFilter(BROADCAST_ACTION));
        loadPreferences();
        loadFromBase(mFavorite);
        startService(rssReaderServiceIntent);

        mServiceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder binder) {
                Log.d(LOG_TAG, "MainActivity onServiceConnected");
                ((RSSReaderService.ServiceBinder)binder).cancelNotification();
                mBound = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
                mBound = false;
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(rssReaderServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
     protected void onStop() {
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("MAINACTIVITY", "ONDestroy");
        unregisterReceiver(mReceiver);
        if (mProgressDialog != null) mProgressDialog.dismiss();
        mMyDbTools.close();
        savePreferences();
        super.onDestroy();
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        switch (id) {
            case YES_NO_DIALOG_DELETE_ITEM:
                return new YesNoDialog(this, id, args.getInt("position")).create();
            case YES_NO_DIALOG_DELETE_ALL:
                return new YesNoDialog(this, id).create();
        }
        return super.onCreateDialog(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.update).setEnabled(mUpdateButtonEnabled);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.update:
                if (UpdateRssService.isInternet(this)) {
                    startService(rssReaderServiceIntent);
                }else {
                    Toast.makeText(this, R.string.turn_on_internet, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.favorite:
                mFavorite = !mFavorite;
                if (mFavorite) {
                    item.setIcon(R.drawable.ic_check_circle_black_whitecircle_36dp);
                } else {
                    item.setIcon(R.drawable.ic_check_circle_white_36dp);
                }
                loadFromBase(mFavorite);
                break;
            case R.id.clear:
                showDialog(YES_NO_DIALOG_DELETE_ALL);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            showDialog(YES_NO_DIALOG_DELETE_ITEM, bundle);
            return true;
        }
    };

    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            Intent intent = new Intent(getApplicationContext(), ViewItem.class);
            mFeeds.get(position).setReaded(true);
            update(mFeeds.get(position));
            updateListView();
            intent.putExtra("fullText", (mFeeds.get(position)).getFullText());
            intent.putExtra("link", (mFeeds.get(position)).getLink());
            intent.putExtra("date", (mFeeds.get(position)).getpubDateString());
            startActivity(intent);
        }
    };

    private void setProgressDialog(boolean bool) {
        if (bool) {
            mUpdateButtonEnabled = false;
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("Downloading news");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } else {
            mUpdateButtonEnabled = true;
            mProgressDialog.dismiss();
        }
    }

    public void update(Items list) {
        mMyDbTools.insert(list);
        loadFromBase(mFavorite);
    }

    public void updateListView() {
        mAdapter.notifyDataSetChanged();
    }

    public void deleteAll() {
        mMyDbTools.deleteAll();
        mFeeds.clear();
        updateListView();
    }

    public void delete(Item item) {
        mMyDbTools.delete(item);
        mFeeds.remove(item);
        updateListView();
    }

    public void update(Item item) {
        mMyDbTools.update(item);
    }

    public void loadFromBase(boolean favorite) {
        Items items = mMyDbTools.selectAll(favorite);
        this.mFeeds.clear();
        if (items != null) {
            this.mFeeds.addAll(items);
        } else {
            Toast.makeText(this, "База новостей пуста! \n Обновите страницу ", Toast.LENGTH_SHORT).show();
        }
        updateListView();
    }

    @Override
    public void onYesNoDialogClicked(int dialogType, int result, int position) {
        switch (dialogType) {
            case YES_NO_DIALOG_DELETE_ITEM:
                if (result == Dialog.BUTTON_POSITIVE) {
                    delete(mFeeds.get(position));
                }
                break;
            case YES_NO_DIALOG_DELETE_ALL:
                if (result == Dialog.BUTTON_POSITIVE) {
                    deleteAll();
                }
        }
    }

    @Override
    public void onYesNoDialogCancelled() {

    }

    private void savePreferences(){
        if (mSharedPreferences == null) mSharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(FAVORITE, mFavorite);
        editor.apply();
    }

    private  void loadPreferences(){
        mSharedPreferences = getPreferences(MODE_PRIVATE);
        mFavorite = mSharedPreferences.getBoolean(FAVORITE, false);
    }
    public void stopService(View view) {
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
        stopService(new Intent(this, RSSReaderService.class));
    }
}