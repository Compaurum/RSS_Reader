package com.example.compaurum.rss_reader.parser;

import java.util.ArrayList;
import java.util.HashMap;

public class Channel {
    private String title;
    private String description;
    private String link;
    private String language;
    private String generator;
    private Items items;
    private HashMap<String, ArrayList <Item>> category;

    public Items getItems() {
        return items;
    }

    public HashMap<String, ArrayList<Item>> getCategory() {
        return category;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

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
