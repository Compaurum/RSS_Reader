package com.example.compaurum.rss_reader.newparser;

import com.example.compaurum.rss_reader.newparser.RssParser.* ;

import java.util.ArrayList;

/**
 * Created by compaurum on 26.10.2015.
 */
public class Using_Rss {
    public static void main(String[] args){
        RssParser rp = new RssParser("http://www.telegraf.in.ua/rss.xml");
        rp.parse();
        Channel feed = rp.getFeed();

// Listing all categories & the no. of elements in each category
            if (feed.category != null)
            {
                System.out.println("Category: ");
                for (String category : feed.category.keySet())
                {
                    System.out.println(category
                            + ": "
                            + (feed.category.get(category)).size());
                }
            }


// Listing all items in the feed
        for (int i = 0; i < feed.items.size(); i++)
            System.out.println(feed.items.get(i).getTitle());
    }
}
