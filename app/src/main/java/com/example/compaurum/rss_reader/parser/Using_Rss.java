package com.example.compaurum.rss_reader.parser;

import android.util.Log;

import com.example.compaurum.rss_reader.Downloader.Downloader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by compaurum on 26.10.2015.
 */
public class Using_Rss {

    public static void main(String[] args) throws ParseException {
        //String string = "Tue, 10 Nov 2015 12:30:54 +0200";

        try {
            Date date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ", Locale.ENGLISH).parse("Tue, 10 Nov 2015 12:30:54 +0200");
            System.out.println(date);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            System.out.println(dateFormat.format(date));
            System.out.println(new SimpleDateFormat("dd/mm/yyyy HH:mm").parse("1/1/13 12:30"));
            System.out.println(new SimpleDateFormat("dd/mm/yy HH:mm").parse("1/1/13 12:30"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static void main1(String[] args) throws UnsupportedEncodingException {
        String mLink = "http://www.telegraf.in.ua/rss.xml";
        String fullText = "";
        RssParser rp = new RssParser(); //"http://www.telegraf.in.ua/rss.xml"
        rp.parse(new String(""));
        Channel feed = rp.getFeed();
        try {
            fullText = (new Downloader()).download(mLink);
            rp.parse(fullText);
        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
        }catch (Exception e){
            Log.d("ERROR", e.getMessage());
        }


        feed = rp.getFeed();
 //Listing all categories & the no. of elements in each category
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


 //Listing all items in the feed
        System.out.println("");
        for (int i = 0; i < feed.getItems().size(); i++)
            System.out.println(new String(feed.getItems().get(i).getTitle().getBytes("cp1251")));
    }
}
