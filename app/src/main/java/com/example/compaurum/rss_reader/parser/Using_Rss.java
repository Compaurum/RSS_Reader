package com.example.compaurum.rss_reader.parser;

/**
 * Created by compaurum on 26.10.2015.
 */
public class Using_Rss {
    public static void main(String[] args){
        RssParser rp = new RssParser(); //"http://www.telegraf.in.ua/rss.xml"
        rp.parse(new String("ef"));
        Channel feed = rp.getFeed();

// Listing all categories & the no. of elements in each category
            if (feed.getCategory() != null)
            {
                System.out.println("Category: ");
                for (String category : feed.getCategory().keySet())
                {
                    System.out.println(category
                            + ": "
                            + (feed.getCategory().get(category)).size());
                }
            }


// Listing all items in the feed
        System.out.println("");
        for (int i = 0; i < feed.getItems().size(); i++)
            System.out.println(feed.getItems().get(i).getMpubDate());
    }
}
