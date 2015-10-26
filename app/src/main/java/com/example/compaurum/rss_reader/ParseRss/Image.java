package com.example.compaurum.rss_reader.ParseRss;

/**
 * Created by compaurum on 14.10.2015.
 */
public class Image {
    private String mUrl;
    private String mTitle;
    private String mLink;

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
