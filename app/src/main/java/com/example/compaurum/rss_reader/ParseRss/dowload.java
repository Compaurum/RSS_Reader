package com.example.compaurum.rss_reader.ParseRss;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by compaurum on 14.10.2015.
 */
public class dowload {
    public static void main(String [] args) throws IOException {
        URL url = new URL("http://www.telegraf.in.ua/rss.xml");
        InputStream inputStream = url.openConnection().getInputStream();

        Parser telegraph = new Parser();
        Channel channel = telegraph.parse(inputStream);
        System.out.println(channel.getItems().toString());


//        while (inputStream.available() > 0){
//            byte[] buffer = new byte[100];
//            String input = "";
//            char k;
//            System.out.println("qwe");
//            while (inputStream.read(buffer) > 0){
//                System.out.println("222");
//                for (int i = 0; i < buffer.length; i++) {
//                    k = (char)buffer[i];
//                    input += k;
//                }
//                System.out.println(input);
//            }
//        }
    }
}
