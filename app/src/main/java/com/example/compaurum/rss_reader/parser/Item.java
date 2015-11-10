package com.example.compaurum.rss_reader.parser;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by compaurum on 14.10.2015.
 */
public class Item implements Serializable{
    private String mTitle;
    private String mLink;
    private String mDescription;
    private String mCategory;
    private Date mpubDate;
    private String mpubDateString;
    private String mFullText;
    private boolean mFavorite = false;

    public Item(){}

    public Item(String title, String link, String fullText, boolean favorite) {
        mTitle = title;
        mLink = link;
        mFullText = fullText;
        mFavorite = favorite;
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

    public String getpubDateString() {
        if (mpubDate != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            return dateFormat.format(mpubDate);
        }
        else if (mpubDateString != null){
            return mpubDateString;
        }
        else return null;
    }
    public Date getMpubDate(){
        return mpubDate;
    }

    public void setMpubDate(String mpubDate) {
        try {
           this.mpubDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ", Locale.ENGLISH).parse(mpubDate);
        } catch (ParseException e) {
            this.mpubDate = null;
            this.mpubDateString = mpubDate;
        }
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
