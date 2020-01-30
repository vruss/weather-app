package com.example.weatherapp;

import android.util.Pair;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WeatherXmlParser {

    public WeatherForecast parse(InputStream in) throws XmlPullParserException, IOException {
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

    public WeatherForecast readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        WeatherForecast weatherForecast = null;
        List entries = new ArrayList();

        // END_TAG = 3  || START_TAG = 2
        parser.require(XmlPullParser.START_TAG, null, "weatherdata");
        while (entries.size() < 2) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                parser.next();
                continue;
            }
            if (parser.getName().equals("location")) {
                entries.add(readWeatherForecast(parser));
            }
            parser.next();
        }

        weatherForecast = (WeatherForecast) entries.get(0);
        WeatherForecast precipitationForecast = (WeatherForecast) entries.get(1);
        weatherForecast.precipitation = precipitationForecast.precipitation;
        weatherForecast.symbol = precipitationForecast.symbol;
        return weatherForecast;
    }

    private WeatherForecast readWeatherForecast(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "location");
        String temperature = null;
        String windSpeed = null;
        String windDirection = null;
        String cloudiness = null;
        Pair<String, String> precipitation = null;
        String symbol = null;

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
            } else if (name.equals("precipitation")) {
                precipitation = readPrecipitation(parser);
            } else if (name.equals("symbol")) {
                symbol = readSymbol(parser);
            } else {
                skip(parser);
            }
        }
        return new WeatherForecast(temperature, windSpeed, windDirection, cloudiness, precipitation, symbol);
    }

    private String readTemperature(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "temperature");
        String temperature = parser.getAttributeValue(null, "value");
        parser.next();
        parser.require(XmlPullParser.END_TAG, null, "temperature");
        return temperature;
    }

    private String readWindDirection(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "windDirection");
        String direction = parser.getAttributeValue(null, "name");
        parser.next();
        parser.require(XmlPullParser.END_TAG, null, "windDirection");
        return direction;
    }

    private String readWindSpeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "windSpeed");
        String speed = parser.getAttributeValue(null, "mps");
        parser.next();
        parser.require(XmlPullParser.END_TAG, null, "windSpeed");
        return speed;
    }

    private String readCloudiness(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "cloudiness");
        String cloudiness = parser.getAttributeValue(null, "percent");
        parser.next();
        parser.require(XmlPullParser.END_TAG, null, "cloudiness");
        return cloudiness;
    }

    private Pair<String, String> readPrecipitation(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "precipitation");
        String minPrecipitation = parser.getAttributeValue(null, "minvalue");
        String maxPrecipitation = parser.getAttributeValue(null, "maxvalue");
        parser.next();
        parser.require(XmlPullParser.END_TAG, null, "precipitation");
        return Pair.create(minPrecipitation, maxPrecipitation);
    }

    private String readSymbol(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "symbol");
        String cloudiness = parser.getAttributeValue(null, "number");
        parser.next();
        parser.require(XmlPullParser.END_TAG, null, "symbol");
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
