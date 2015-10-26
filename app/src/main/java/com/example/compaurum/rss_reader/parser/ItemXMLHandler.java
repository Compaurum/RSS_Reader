package com.example.compaurum.rss_reader.parser;

/**
 * Created by compaurum on 26.10.2015.
 */

        import org.xml.sax.Attributes;
        import org.xml.sax.SAXException;
        import org.xml.sax.helpers.DefaultHandler;

        import java.util.ArrayList;

public class ItemXMLHandler extends DefaultHandler {

    Boolean currentElement = false;
    String currentValue = "";
    ItemMaster item = null;
    private ArrayList<ItemMaster> itemsList = new ArrayList<>();

    public ArrayList<ItemMaster> getItemsList() {
        return itemsList;
    }

    // Called when tag starts
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        currentElement = true;
        currentValue = "";
        if (localName.equals("ItemData")) {
            item = new ItemMaster();
        }

    }

    // Called when tag closing
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        currentElement = false;

        /** set value */
        if (localName.equalsIgnoreCase("ItemNumber"))
            item.setItemNumber(currentValue);
        else if (localName.equalsIgnoreCase("Description"))
            item.setDescription(currentValue);
        else if (localName.equalsIgnoreCase("Price"))
            item.setPrice(currentValue);
        else if (localName.equalsIgnoreCase("Weight"))
            item.setWeight(Double.parseDouble(currentValue));
        else if (localName.equalsIgnoreCase("ItemData"))
            itemsList.add(item);
    }

    // Called to get tag characters
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if (currentElement) {
            currentValue = currentValue +  new String(ch, start, length);
        }

    }

}
