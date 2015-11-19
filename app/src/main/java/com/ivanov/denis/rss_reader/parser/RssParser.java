package com.ivanov.denis.rss_reader.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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

    public RssParser() {
        this.mText = new StringBuilder();
    }

    public void parse(String fullText) {
        InputStream urlInputStream = null;
        SAXParserFactory spf = null;
        SAXParser sp = null;
        try {
            spf = SAXParserFactory.newInstance();
            if (spf != null) {
                sp = spf.newSAXParser();
                //sp.parse(new ByteArrayInputStream((new Downloader()).download(mUrlString).getBytes()), this);
                sp.parse(new ByteArrayInputStream(fullText.getBytes()), this);
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
        } finally {
            spf = null;
            sp = null;
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
            case "pubdate":
                if (this.mItem != null)
                    this.mItem.setMpubDate(this.mText.toString().trim());
                System.out.println(mText.toString());
                break;
            case "category":
                if (this.mItem != null)
                    this.mCannel.addItem(this.mText.toString().trim(), this.mItem);
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