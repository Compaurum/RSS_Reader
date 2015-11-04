package com.example.compaurum.rss_reader;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

//import com.example.compaurum.rss_reader.adapter.ListAdapter;
import com.example.compaurum.rss_reader.parser.Item;
import com.example.compaurum.rss_reader.parser.Items;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity{

    private ArrayList mFeeds = new ArrayList();  //{"Mark", "John", "Idiot"};
    private CheckBox mFavorite;
    private Button mButton;
    private ListView mLvMain;
    private TextView channelTitle;
    private ArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mNames.add("Mark");
       // mNames.add("John");
        //mNames.add("Victor");

        mLvMain = (ListView) findViewById(android.R.id.list);
        mButton = (Button) findViewById(R.id.button);
        mFavorite = (CheckBox) findViewById(R.id.checkBox1);
        channelTitle = (TextView) findViewById(R.id.channelTitle);
        mAdapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.label, mFeeds);
        //mAdapter = new ArrayAdapter(this, R.layout.list_item, mNames);
        mLvMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mLvMain.setAdapter(mAdapter);

        mButton.setOnClickListener(new UpdateRss(this, mLvMain));

        mLvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewItem.class);
                intent.putExtra("fullText", ((Item)mFeeds.get(position)).getFullText());
                intent.putExtra("link", ((Item)mFeeds.get(position)).getLink());
                intent.putExtra("date", ((Item)mFeeds.get(position)).getMpubDate());
                startActivity(intent);
                Log.d("LOG_TAG", "itemClick: position = " + position + ", id = " + id);
                Log.d("LOG_TAG", "date " + ((Item)mFeeds.get(position)).getMpubDate());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
    public void updateList(Items list){
        this.mFeeds.clear();
        this.mFeeds.addAll(list);
        this.mAdapter.notifyDataSetChanged();
    }

    /*protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) mAdapter.getItem(position);
        Toast.makeText(this, item + " selected list Item", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = (String) mAdapter.getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_SHORT).show();
    }*/
}