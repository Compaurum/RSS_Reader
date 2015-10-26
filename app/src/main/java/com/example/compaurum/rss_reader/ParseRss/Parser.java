package com.example.compaurum.rss_reader.ParseRss;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import android.sax.*;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Created by compaurum on 14.10.2015.
 */
public class Parser {

    private Channel mChannel;
    private Items mItems;
    private Item mItem;

    public Parser(){
        mItems = new Items();
    }

    public Channel parse(InputStream inputStream){
        RootElement root = new RootElement("rss");
        Element chanElement = root.getChild("channel");
        Element chanTitle = chanElement.getChild("title");
        Element chanLink = chanElement.getChild("link");
        Element chanDescription = chanElement.getChild("description");
        Element chanLastBuildDate = chanElement.getChild("lastBuildDate");
        Element chanDocs = chanElement.getChild("docs");
        Element chanLanguage = chanElement.getChild("language");

        Element chanItem = chanElement.getChild("item");
        Element itemTitle = chanItem.getChild("title");
        Element itemDescription = chanItem.getChild("description");
        Element itemLink = chanItem.getChild("link");

        chanElement.setStartElementListener(new StartElementListener() {

            @Override
            public void start(Attributes attributes) {
                mChannel = new Channel();
            }
        });

        // Listen for the end of a text element and set the text as our
        // channel's title.
        chanTitle.setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                mChannel.setTitle(body);
            }
        });

        // Same thing happens for the other elements of channel ex.

        // On every <item> tag occurrence we create a new Item object.
        chanItem.setStartElementListener(new StartElementListener() {
            public void start(Attributes attributes) {
                mItem = new Item();
            }
        });

        // On every </item> tag occurrence we add the current Item object
        // to the Items container.
        chanItem.setEndElementListener(new EndElementListener() {
            public void end() {
                mItems.add(mItem);
            }
        });

        itemTitle.setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                mItem.setTitle(body);
            }
        });

        // and so on

        // here we actually parse the InputStream and return the resulting
        // Channel object.
        try {
            Xml.parse(inputStream, Xml.Encoding.UTF_8, root.getContentHandler());
            return mChannel;
        } catch (SAXException e) {
            // handle the exception
        } catch (IOException e) {
            // handle the exception
        }

        return null;
    }

}
