package com.example.compaurum.rss_reader;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.compaurum.rss_reader.DBHelper.MyDBTools;
import com.example.compaurum.rss_reader.Interfaces.Constants;
import com.example.compaurum.rss_reader.adapter.ListAdapter;
import com.example.compaurum.rss_reader.parser.Channel;
import com.example.compaurum.rss_reader.parser.Item;
import com.example.compaurum.rss_reader.parser.Items;
import com.example.compaurum.rss_reader.DBHelper.DBHelper;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements Constants{

    private Items mFeeds = new Items();
    private CheckBox mFavorite;
    private TextView mProccess;
    private ListView mLvMain;
    private TextView channelTitle;
    private ListAdapter mAdapter;
    Handler handler = null;
    private boolean mUpdateButtonEnabled = true;
    private ProgressDialog mProgressDialog;
    private DBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLvMain = (ListView) findViewById(android.R.id.list);
        mProccess = (TextView) findViewById(R.id.proccess);
        mFavorite = (CheckBox) findViewById(R.id.checkBox1);
        channelTitle = (TextView) findViewById(R.id.channelTitle);
        //mAdapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.label, mFeeds);
        mAdapter = new ListAdapter(this, mFeeds);
        //mAdapter = new ArrayAdapter(this, R.layout.list_item, mNames);
        mLvMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mLvMain.setAdapter(mAdapter);

        mLvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewItem.class);
                intent.putExtra("fullText", ((Item) mFeeds.get(position)).getFullText());
                intent.putExtra("link", ((Item) mFeeds.get(position)).getLink());
                intent.putExtra("date", ((Item) mFeeds.get(position)).getpubDateString());
                startActivity(intent);
                //Log.d("LOG_TAG", "itemClick: position = " + position + ", id = " + id);
                //Log.d("LOG_TAG", "date " + ((Item) mFeeds.get(position)).getMpubDate());

            }
        });

        mLvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                delete(mFeeds.get(position));
                return true;
            }
        });
        loadFromBase(null);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings){
            return true;
        } else if (id == R.id.update){
            new UpdateRss(this, mLvMain).update();
        } else if (id == R.id.clear){
            deleteAll();
        }

        return super.onOptionsItemSelected(item);
    }

    public TextView getProccess() {
        return mProccess;
    }

    public void setProgressDialog(boolean bool){
        if (bool) {
            mProccess.setText("Started");
            mUpdateButtonEnabled = false;
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("Downloading news");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }else{
            mProccess.setText("Ended");
            mUpdateButtonEnabled = true;
            mProgressDialog.dismiss();
        }
    }

    public ListView getLvMain() {
        return mLvMain;
    }

    public ArrayList getFeeds() {
        return mFeeds;
    }

    public ListAdapter getAdapter() {
        return mAdapter;
    }

    public Handler getHandler() {
        return handler;
    }

    public void updateList(Items list) {
        MyDBTools myDBTools = new MyDBTools(new DBHelper(this));
        myDBTools.insert(list);
        loadFromBase(myDBTools);
    }

    public void updateListView(){
        mAdapter.notifyDataSetChanged();
    }

    public void deleteAll(){
        MyDBTools myDBTools = new MyDBTools(new DBHelper(this));
        myDBTools.deleteAll();
        mFeeds.clear();
        updateListView();
    }

    public void delete(Item item){
        MyDBTools myDBTools = new MyDBTools(new DBHelper(this));
        myDBTools.delete(item);
        mFeeds.remove(item);
        updateListView();
    }

    public void update(Item item){
        MyDBTools myDBTools = new MyDBTools(new DBHelper(this));
        myDBTools.update(item);
    }

    public void loadFromBase(MyDBTools tools){
        MyDBTools myDBTools ;
        if (tools == null){
            myDBTools = new MyDBTools(new DBHelper(this));
        }else myDBTools = tools;

        Items items = myDBTools.selectAll();
        if (items != null) {
            this.mFeeds.clear();
            this.mFeeds.addAll(items);
            updateListView();
        }
    }
}