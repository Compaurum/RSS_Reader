package com.example.compaurum.rss_reader.parser;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by compaurum on 26.10.2015.
 */
public class Parser {
    private EditText xmlInput;
    private TextView xmlOutput;

    private void parseXML() {

        String parsedData = "";

        try {

            Log.w("AndroidParseXMLActivity", "Start");
            /** Handling XML */
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();

            ItemXMLHandler myXMLHandler = new ItemXMLHandler();
            xr.setContentHandler(myXMLHandler);
            InputSource inStream = new InputSource();
            Log.w("AndroidParseXMLActivity", "Parse1");

            inStream.setCharacterStream(new StringReader(xmlInput.getText().toString()));
            Log.w("AndroidParseXMLActivity", "Parse2");

            xr.parse(inStream);
            Log.w("AndroidParseXMLActivity", "Parse3");

            ArrayList<ItemMaster> itemsList = myXMLHandler.getItemsList();
            for (int i = 0; i < itemsList.size(); i++) {
                ItemMaster item = itemsList.get(i);
                parsedData = parsedData + "----->\n";
                parsedData = parsedData + "Item Number: " + item.getItemNumber() + "\n";
                parsedData = parsedData + "Description: " + item.getDescription() + "\n";
                parsedData = parsedData + "Price: " + item.getPrice() + "\n";
                parsedData = parsedData + "Weight: " + item.getWeight() + "\n";

            }
            Log.w("AndroidParseXMLActivity", "Done");
        } catch (Exception e) {
            Log.w("AndroidParseXMLActivity", e);
        }

        xmlOutput.setText(parsedData);
    }
}
