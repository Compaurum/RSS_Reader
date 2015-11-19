package com.example.compaurum.rss_reader;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.compaurum.rss_reader.DBHelper.DBHelper;
import com.example.compaurum.rss_reader.DBHelper.MyDBTools;
import com.example.compaurum.rss_reader.adapter.ListAdapter;
import com.example.compaurum.rss_reader.constants.Constants;
import com.example.compaurum.rss_reader.dialog.YesNoDialog;
import com.example.compaurum.rss_reader.dialog.YesNoDialogListener;
import com.example.compaurum.rss_reader.parser.Item;
import com.example.compaurum.rss_reader.parser.Items;


public class MainActivity extends ActionBarActivity implements Constants, YesNoDialogListener {

    private Items mFeeds = new Items();
    private boolean mFavorite = false;
    private TextView mProccess;
    private ListView mLvMain;
    private ListAdapter mAdapter;
    private boolean mUpdateButtonEnabled = true;
    private ProgressDialog mProgressDialog;
    private DBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLvMain = (ListView) findViewById(android.R.id.list);
        mAdapter = new ListAdapter(this, mFeeds);
        mLvMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mLvMain.setAdapter(mAdapter);
        mLvMain.setOnItemClickListener(mOnItemClickListener);
        mLvMain.setOnItemLongClickListener(mOnItemLongClickListener);

        loadFromBase(null, mFavorite);
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
                new UpdateRss(this, mLvMain).update();
                break;
            case R.id.favorite:
                mFavorite = !mFavorite;
                if (mFavorite) {
                    item.setIcon(R.drawable.ic_check_circle_black_whitecircle_36dp);
                } else {
                    item.setIcon(R.drawable.ic_check_circle_white_36dp);
                }
                loadFromBase(null, mFavorite);
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

    public TextView getProccess() {
        return mProccess;
    }

    public void setProgressDialog(boolean bool) {
        if (bool) {
            mProccess.setText("Started");
            mUpdateButtonEnabled = false;
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("Downloading news");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } else {
            mProccess.setText("Ended");
            mUpdateButtonEnabled = true;
            mProgressDialog.dismiss();
        }
    }

    public ListView getLvMain() {
        return mLvMain;
    }

    public Items getFeeds() {
        return mFeeds;
    }

    public ListAdapter getAdapter() {
        return mAdapter;
    }


    public void update(Items list) {
        MyDBTools myDBTools = new MyDBTools(new DBHelper(this));
        myDBTools.insert(list);
        loadFromBase(myDBTools, mFavorite);
    }

    public void updateListView() {
        mAdapter.notifyDataSetChanged();
    }

    public void deleteAll() {
        MyDBTools myDBTools = new MyDBTools(new DBHelper(this));
        myDBTools.deleteAll();
        mFeeds.clear();
        updateListView();
    }

    public void delete(Item item) {
        MyDBTools myDBTools = new MyDBTools(new DBHelper(this));
        myDBTools.delete(item);
        mFeeds.remove(item);
        updateListView();
    }

    public void update(Item item) {
        MyDBTools myDBTools = new MyDBTools(new DBHelper(this));
        myDBTools.update(item);
    }

    public void loadFromBase(MyDBTools tools, boolean favorite) {
        MyDBTools myDBTools;
        if (tools == null) {
            myDBTools = new MyDBTools(new DBHelper(this));
        } else myDBTools = tools;

        Items items = myDBTools.selectAll(favorite);
        if (items != null) {
            this.mFeeds.clear();
            this.mFeeds.addAll(items);
            updateListView();
        } else {
            this.mFeeds.clear();
            Toast.makeText(this, "База новостей пуста! \n Обновите страницу ", Toast.LENGTH_SHORT).show();
        }
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
}