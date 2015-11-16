package com.example.compaurum.rss_reader.parser;

public class Image {
    private String mUrl;
    private String mTitle;
    private String mLink;

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

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    @Override
    public String toString() {
        return "Url: " + this.mUrl + "\n Title: " + mTitle + "\n Link: " + mLink;
    }
}
