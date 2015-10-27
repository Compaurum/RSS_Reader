package com.example.compaurum.rss_reader.newparser;

/**
 * Created by compaurum on 26.10.2015.
 */

import android.database.DefaultDatabaseErrorHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.DefaultHandler;

public class RssParser extends DefaultHandler {
    private String mUrlString;
    private Channel mCannel;
    private StringBuilder mText;
    private Item mItem;
    private boolean mImgStatus;

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
            urlInputStream = url.openConnection().getInputStream();
            spf = SAXParserFactory.newInstance();
            if (spf != null) {
                sp = spf.newSAXParser();
                sp.parse(urlInputStream, this);
            }
        }

        /*
         * Exceptions need to be handled
         * MalformedURLException
         * ParserConfigurationException
         * IOException
         * SAXException
         */ catch (Exception e) {
            System.out.println("Exception e");
            e.printStackTrace();
        } finally {
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
                else if (this.mImgStatus) this.mCannel.imageTitle = this.mText.toString().trim();
                else this.mCannel.title = this.mText.toString().trim();
                break;
            case "link":
                if (this.mItem != null) this.mItem.setLink(this.mText.toString().trim());
                else if (this.mImgStatus) this.mCannel.imageLink = this.mText.toString().trim();
                else this.mCannel.link = this.mText.toString().trim();
                break;
            case "description":
                if (this.mItem != null) this.mItem.setDescription(this.mText.toString().trim());
                else this.mCannel.description = this.mText.toString().trim();
                break;
            case "url":
                this.mCannel.imageUrl = this.mText.toString().trim();
                break;
            case "language":
                this.mCannel.language = this.mText.toString().trim();

            case "generator":
                this.mCannel.generator = this.mText.toString().trim();

            case "copyright":
                this.mCannel.copyright = this.mText.toString().trim();

            case "pubDate":
                if(this.mItem != null) this.mItem.setMpubDate(this.mText.toString().trim());

            case "category":
                if(this.mItem != null) this.mCannel.addItem(this.mText.toString().trim(), this.mItem);
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