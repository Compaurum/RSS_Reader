package com.example.compaurum.rss_reader.newparser;

/**
 * Created by compaurum on 26.10.2015.
 */
import android.database.DefaultDatabaseErrorHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.DefaultHandler;

public class RssParser extends DefaultHandler
{
    private String        urlString;
    private Channel       channel;
    private StringBuilder text;
    private Item          item;
    private boolean       imgStatus;

    public RssParser(String url)
    {
        this.urlString = url;
        this.text = new StringBuilder();
    }

    public void parse()
    {
        InputStream urlInputStream = null;
        SAXParserFactory spf = null;
        SAXParser sp = null;

        try
        {
            URL url = new URL(this.urlString);
            //_setProxy(); // Set the proxy if needed
            urlInputStream = url.openConnection().getInputStream();
            spf = SAXParserFactory.newInstance();
            if (spf != null)
            {
                sp = spf.newSAXParser();
                sp.parse(urlInputStream, this);
            }
        }

        /*
         * Exceptions need to be handled
         * MalformedURLException
         * ParserConfigurationException
         * IOException
         * SAXException
         */

        catch (Exception e)
        {
            System.out.println("Exception e");
                    e.printStackTrace();
        }
        finally
        {
            try
            {
                if (urlInputStream != null) urlInputStream.close();
            }
            catch (Exception e) {}
        }
    }

    public Channel getFeed()
    {
        return (this.channel);
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes)
    {
        switch (qName.toLowerCase()){
            case "channel":
                this.channel = new Channel();
                break;
            case "item":
                if (this.channel != null){
                    this.item = new Item();
                    this.channel.addItem(this.item);
                }
                break;
            case "image":
                if (this.channel != null){
                    this.imgStatus = true;
                }
                break;
        }
    }

    public void endElement(String uri, String localName, String qName)
    {
        if (this.channel == null)
            return;

        if (qName.equalsIgnoreCase("item"))
            this.item = null;

        else if (qName.equalsIgnoreCase("image"))
            this.imgStatus = false;

        else if (qName.equalsIgnoreCase("title"))
        {
            if (this.item != null) this.item.setTitle(this.text.toString().trim());
            else if (this.imgStatus) this.channel.imageTitle = this.text.toString().trim();
            else this.channel.title = this.text.toString().trim();
        }

        else if (qName.equalsIgnoreCase("link"))
        {
            if (this.item != null) this.item.setLink(this.text.toString().trim());
            else if (this.imgStatus) this.channel.imageLink = this.text.toString().trim();
            else this.channel.link = this.text.toString().trim();
        }

        else if (qName.equalsIgnoreCase("description"))
        {
            if (this.item != null) this.item.setDescription(this.text.toString().trim());
            else this.channel.description = this.text.toString().trim();
        }

        else if (qName.equalsIgnoreCase("url") && this.imgStatus)
            this.channel.imageUrl = this.text.toString().trim();

        else if (qName.equalsIgnoreCase("language"))
            this.channel.language = this.text.toString().trim();

        else if (qName.equalsIgnoreCase("generator"))
            this.channel.generator = this.text.toString().trim();

        else if (qName.equalsIgnoreCase("copyright"))
            this.channel.copyright = this.text.toString().trim();

        else if (qName.equalsIgnoreCase("pubDate") && (this.item != null))
            this.item.setMpubDate(this.text.toString().trim());

        else if (qName.equalsIgnoreCase("category") && (this.item != null))
            this.channel.addItem(this.text.toString().trim(), this.item);

        this.text.setLength(0);
    }

    public void characters(char[] ch, int start, int length)
    {
        this.text.append(ch, start, length);
    }

   // public static void _setProxy()
            //throws IOException
    //{
    //Properties sysProperties = System.getProperties();
        //sysProperties.put("proxyHost<Proxy IP Address>");
        //sysProperties.put("proxyPort<Proxy Port Number>");
        //System.setProperties(sysProperties);
    //}

}