package com.example.compaurum.rss_reader;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;


public class ViewItem extends ActionBarActivity {

    private String mFullText;
    private String mLink;
    private String mDate;
    private String mFullPage;
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        this.mFullText = getIntent().getStringExtra("fullText");
        this.mLink = getIntent().getStringExtra("link");
        this.mDate = getIntent().getStringExtra("date");
        this.mFullPage = paragraph(mFullText) + paragraph(link(mLink)) + paragraph(mDate);

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.loadDataWithBaseURL(null, mFullPage, "text/html", "en_US", null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_item, menu);
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

    private String paragraph(String in){
        return "<p>" + in + "</p>";
    }
    private String link(String in){
        return "<a href = \""+ in + "\" >"+getResources().getString(R.string.lookOnWebSite)+"</a>";
    }
}
