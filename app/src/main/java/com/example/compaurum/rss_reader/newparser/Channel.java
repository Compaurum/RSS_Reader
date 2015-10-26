package com.example.compaurum.rss_reader.newparser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by compaurum on 26.10.2015.
 */
public class Channel {
    public  String title;
    public  String description;
    public  String link;
    public  String language;
    public  String generator;
    public  String copyright;
    public  String imageUrl;
    public  String imageTitle;
    public  String imageLink;

    protected Items items;
    protected HashMap<String, ArrayList <Item>> category;

    public void addItem(Item item)
    {
        if (this.items == null)
            this.items = new Items();
        this.items.add(item);
    }

    public void addItem(String category, Item item)
    {
        if (this.category == null)
            this.category = new HashMap<String, ArrayList<Item>>();
        if (!this.category.containsKey(category))
            this.category.put(category, new ArrayList<Item>());
        this.category.get(category).add(item);
    }
}
