package com.example.compaurum.rss_reader.parser;

import java.io.Serializable;

/**
 * Created by compaurum on 14.10.2015.
 */
public class Item implements Serializable{
    private String mTitle;
    private String mLink;
    private String mDescription;
    private String mCategory;
    private String mpubDate;
    private String mFullText;
    private boolean mFavorite = false;

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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getMpubDate() {
        return mpubDate;
    }

    public void setMpubDate(String mpubDate) {
        this.mpubDate = mpubDate;
    }

    public String getFullText() {
        return mFullText;
    }

    public void setFullText(String fullText) {
        mFullText = fullText;
    }

    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
    }

    @Override
    public String toString()
    {
        return (this.mTitle + " ");// + ": " + this.mpubDate + "n" + this.mDescription + "\n ---- " + this.mFullText);
    }
}
