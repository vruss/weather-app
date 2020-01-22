package com.example.weatherapp;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class XMLParser {

    private static final String GET_URL = "https://api.met.no/weatherapi/locationforecast/1.9/?lat=60.10;lon=9.58";

    public static NodeList parseURL(String url)
    {
        NodeList data = null;
        try {
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();

            Document doc = builder.parse(new URL(url).openStream());
            doc.getDocumentElement().normalize();

            data = doc.getElementsByTagName("location");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
