package com.example.compaurum.rss_reader.parser;

/**
 * Created by compaurum on 26.10.2015.
 */

//import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class RssParser extends DefaultHandler {

    private String mUrlString;
    private Channel mCannel;
    private StringBuilder mText;
    private Item mItem;
    private boolean mImgStatus;
    private Image mImage;

    public RssParser(String url) {
        this.mUrlString = url;
        this.mText = new StringBuilder();
    }

    public void parse() {
        InputStream urlInputStream = null;
        SAXParserFactory spf = null;
        SAXParser sp = null;

        try {
            URL url = new URL(this.mUrlString);
            //_setProxy(); // Set the proxy if needed
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                urlInputStream = conn.getInputStream();
            }
            spf = SAXParserFactory.newInstance();
            if (spf != null) {
                sp = spf.newSAXParser();
                //Log.d("ERROR", urlInputStream.toString());
                //Log.d("ERROR", this.toString());
                //Log.d("ERROR", sp.toString());
               // System.out.println(urlInputStream.available());
                    sp.parse(urlInputStream, this);
                //System.out.println(urlInputStream == null);

                //Log.d("ERROR", (String.valueOf(urlInputStream.available())));
            }
        }

        /*
         * Exceptions need to be handled
         * MalformedURLException
         * ParserConfigurationException
         * IOException
         * SAXException
         */ catch (Exception e) {
            System.out.println("Exception in RssParser.java");
            e.printStackTrace();
        }
        finally {
            try {
                if (urlInputStream != null) urlInputStream.close();
            } catch (Exception e) {
            }
        }
    }

    public Channel getFeed() {
        return (this.mCannel);
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) {
        switch (qName.toLowerCase()) {
            case "channel":
                this.mCannel = new Channel();
                break;
            case "item":
                if (this.mCannel != null) {
                    this.mItem = new Item();
                    this.mCannel.addItem(this.mItem);
                }
                break;
            case "image":
                if (this.mCannel != null) {
                    this.mImage = new Image();
                    this.mImgStatus = true;
                }
                break;
        }
    }

    public void endElement(String uri, String localName, String qName) {
        if (this.mCannel == null)
            return;

        switch (qName.toLowerCase()) {
            case "item":
                this.mItem = null;
                break;
            case "image":
                this.mImgStatus = false;
                break;
            case "title":
                if (this.mItem != null) this.mItem.setTitle(this.mText.toString().trim());
                else if (this.mImgStatus) this.mImage.setTitle(this.mText.toString().trim());
                else this.mCannel.setTitle(this.mText.toString().trim());
                break;
            case "link":
                if (this.mItem != null) this.mItem.setLink(this.mText.toString().trim());
                else if (this.mImgStatus) this.mImage.setLink(this.mText.toString().trim());
                else this.mCannel.setLink(this.mText.toString().trim());
                break;
            case "description":
                if (this.mItem != null) this.mItem.setDescription(this.mText.toString().trim());
                else this.mCannel.setDescription(this.mText.toString().trim());
                break;
            case "url":
                this.mImage.setUrl(this.mText.toString().trim());
                break;
            case "language":
                this.mCannel.setLanguage(this.mText.toString().trim());
                break;
            case "generator":
                this.mCannel.setGenerator(this.mText.toString().trim());
                break;
            case "pubDate":
                if(this.mItem != null) this.mItem.setMpubDate(this.mText.toString().trim());
                break;
            case "category":
                if(this.mItem != null) this.mCannel.addItem(this.mText.toString().trim(), this.mItem);
                break;
            case "yandex:full-text":
                if (this.mItem != null) this.mItem.setFullText(this.mText.toString().trim());

        }

        this.mText.setLength(0);
    }

    public void characters(char[] ch, int start, int length) {
        this.mText.append(ch, start, length);
    }

    // public static void _setProxy()
    //throws IOException
    //{
    //Properties sysProperties = System.getProperties();
    //sysProperties.put("proxyHost<Proxy IP Address>");
    //sysProperties.put("proxyPort<Proxy Port Number>");
    //System.setProperties(sysProperties);
    //}

}