package com.example.compaurum.rss_reader;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    static String TAG = "default";
    private String[] mNames = {"Mark", "John", "Idiot"};
    private ImageView mSaveIcon;
    private static int sCount = 1;
    private Button mButton;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                mButton.setText("Clicked");
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.button);
        ListView lvMain = (ListView) findViewById(android.R.id.list);
        mSaveIcon = (ImageView) findViewById(R.id.toSaveIcon); // why this is null ???
        mButton.setOnClickListener(this);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,R.layout.list_item, R.id.label, mNames);
        lvMain.setAdapter(adapter);
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



//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        String item = (String) getListAdapter().getItem(position);
//        Toast.makeText(this, item + " selected", Toast.LENGTH_SHORT).show();
//    }
}