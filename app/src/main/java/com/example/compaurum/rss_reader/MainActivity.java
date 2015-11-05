package com.example.compaurum.rss_reader;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

//import com.example.compaurum.rss_reader.adapter.ListAdapter;
import com.example.compaurum.rss_reader.parser.Channel;
import com.example.compaurum.rss_reader.parser.Item;
import com.example.compaurum.rss_reader.parser.Items;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity{

    private ArrayList mFeeds = new ArrayList();  //{"Mark", "John", "Idiot"};
    private CheckBox mFavorite;
    private TextView mProccess;
    private ListView mLvMain;
    private TextView channelTitle;
    private ArrayAdapter mAdapter;
    static Handler handler = null;
    private boolean mUpdateButtonEnabled = true;
    public final static int START_DOWNLOADING = 1;
    public final static int DOWNLOADING = 2;
    public final static int END_DOWNLOADING = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLvMain = (ListView) findViewById(android.R.id.list);
        mProccess = (TextView)findViewById(R.id.proccess);
        //mButton = (Button) findViewById(R.id.button);
        mFavorite = (CheckBox) findViewById(R.id.checkBox1);
        channelTitle = (TextView) findViewById(R.id.channelTitle);
        mAdapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.label, mFeeds);
        //mAdapter = new ArrayAdapter(this, R.layout.list_item, mNames);
        mLvMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mLvMain.setAdapter(mAdapter);

        //mButton.setOnClickListener(new UpdateRss(this, mLvMain));
        mLvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewItem.class);
                intent.putExtra("fullText", ((Item) mFeeds.get(position)).getFullText());
                intent.putExtra("link", ((Item) mFeeds.get(position)).getLink());
                intent.putExtra("date", ((Item) mFeeds.get(position)).getMpubDate());
                startActivity(intent);
                Log.d("LOG_TAG", "itemClick: position = " + position + ", id = " + id);
                Log.d("LOG_TAG", "date " + ((Item) mFeeds.get(position)).getMpubDate());

            }
        });

        handler = new Handler(){
            public void handleMessage(android.os.Message msg) {
                // обновляем TextView
                switch (msg.what){
                    case START_DOWNLOADING:
                        mProccess.setText("Started");
                        mUpdateButtonEnabled = false;
                        break;
                    case DOWNLOADING:
                        mProccess.setText("Dowloading");
                        break;
                    case END_DOWNLOADING:
                        mProccess.setText("Ended");
                        updateList(((Channel)msg.obj).getItems());
                        mUpdateButtonEnabled = true;
                        break;
                    default:
                        break;
                }
            };
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
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
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.update){
            new UpdateRss(this, mLvMain).update();
        }

        return super.onOptionsItemSelected(item);
    }


    public ListView getLvMain() {
        return mLvMain;
    }
    public TextView getChannelTitle() {
        return channelTitle;
    }
    public ArrayList getFeeds(){
        return mFeeds;
    }
    public ArrayAdapter<String> getAdapter() {
        return mAdapter;
    }
    public static Handler getHandler() {
        return handler;
    }

    public void updateList(Items list){
        this.mFeeds.clear();
        this.mFeeds.addAll(list);
        this.mAdapter.notifyDataSetChanged();
    }
}