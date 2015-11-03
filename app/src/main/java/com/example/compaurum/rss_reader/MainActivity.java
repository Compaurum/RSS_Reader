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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.compaurum.rss_reader.parser.Item;
import com.example.compaurum.rss_reader.parser.Items;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity{

    private ArrayList mNames = new ArrayList();  //{"Mark", "John", "Idiot"};
    private ImageView mSaveIcon;
    private Button mButton;
    private ListView mLvMain;
    private TextView channelTitle;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mNames.add("Mark");
       // mNames.add("John");
        //mNames.add("Victor");

        mLvMain = (ListView) findViewById(android.R.id.list);
        mButton = (Button) findViewById(R.id.button);
        //mSaveIcon = (ImageButton) findViewById(R.id.toSaveIcon);
        channelTitle = (TextView) findViewById(R.id.channelTitle);

        mAdapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.label, mNames);
        mLvMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mLvMain.setAdapter(mAdapter);

        mButton.setOnClickListener(new UpdateRss(this, mLvMain));

        mLvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewItem.class);
                intent.putExtra("fullText", ((Item)mNames.get(position)).getFullText());
                intent.putExtra("link", ((Item)mNames.get(position)).getLink());
                intent.putExtra("date", ((Item)mNames.get(position)).getMpubDate());
                startActivity(intent);
                Log.d("LOG_TAG", "itemClick: position = " + position + ", id = " + id);
                Log.d("LOG_TAG", "date " + ((Item)mNames.get(position)).getMpubDate());

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
    public ArrayList getNames(){
        return mNames;
    }
    public ArrayAdapter<String> getAdapter() {
        return mAdapter;
    }
    public void updateList(Items list){
        this.mNames.clear();
        this.mNames.addAll(list);
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