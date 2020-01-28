package com.example.weatherapp;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WeatherXmlParser {

    public static class Entry {
        public final String temperature;
        public final String windSpeed;
        public final String windDirection;
        public final String cloudiness;
        public final String precipitation;

        private Entry(String temperature, String windSpeed, String windDirection, String cloudiness, String precipitation) {
            this.temperature = temperature;
            this.windSpeed = windSpeed;
            this.windDirection = windDirection;
            this.cloudiness = cloudiness;
            this.precipitation = precipitation;
        }
    }

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    public List readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, null, "weatherdata");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("product")) {
                parser.nextTag();
            } else if (name.equals("time")) {
                parser.nextTag();
            } else if (name.equals("location")) {
                entries.add(readEntry(parser));
                break;
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private Entry readEntry(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "location");
        String temperature = null;
        String windSpeed = null;
        String windDirection = null;
        String cloudiness = null;
        String precipitation = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("temperature")) {
                temperature = readTemperature(parser);
            } else if (name.equals("windDirection")) {
                windDirection = readWindDirection(parser);
            } else if (name.equals("windSpeed")) {
                windSpeed = readWindSpeed(parser);
            } else if (name.equals("cloudiness")) {
                cloudiness = readCloudiness(parser);
            } else {
                skip(parser);
            }
        }
        return new Entry(temperature, windSpeed, windDirection, cloudiness, precipitation);
    }

    private String readTemperature(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "temperature");
        String temperature = parser.getAttributeValue(null, "value");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "temperature");
        return temperature;
    }

    private String readWindDirection(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "windDirection");
        String direction = parser.getAttributeValue(null, "name");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "windDirection");
        return direction;
    }

    private String readWindSpeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "windSpeed");
        String speed = parser.getAttributeValue(null, "mps");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "windSpeed");
        return speed;
    }

    private String readCloudiness(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "cloudiness");
        String cloudiness = parser.getAttributeValue(null, "percent");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "cloudiness");
        return cloudiness;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
