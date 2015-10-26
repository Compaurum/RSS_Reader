package com.example.compaurum.rss_reader.ParseRss;

import java.io.Serializable;

/**
 * Created by compaurum on 14.10.2015.
 */
public class Channel implements Serializable {
    private Items mItems;
    private String mTitle;
    private String mLink;
    private String mLanguage;
    private String mDescription;
    private String mGenerator;
    private Image mImage;

    public Channel() {
        setItems(null);
        setTitle(null);
    }

    public Items getItems() {
        return mItems;
    }

    public void setItems(Items items) {
        mItems = items;
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

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getGenerator() {
        return mGenerator;
    }

    public void setGenerator(String generator) {
        mGenerator = generator;
    }

    public Image getImage() {
        return mImage;
    }

    public void setImage(Image image) {
        mImage = image;
    }
}
