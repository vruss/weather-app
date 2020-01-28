package com.example.weatherapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParserException;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DownloadXmlTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "DownloadXmlTask";

    public static final String URL = "https://api.met.no/weatherapi/locationforecast/1.9/?lat=60.10;lon=9.58";

    // Implementation of AsyncTask used to download XML feed from stackoverflow.com.
    @Override
    protected String doInBackground(String... urls) {
        try {
            return loadXmlFromNetwork(urls[0]);
        } catch (IOException e) {
            return "Connection error!" + e.toString();
        } catch (XmlPullParserException e) {
            return "Xml error!" + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "Result of parsing" + result);
//        return result;
    }


    // Uploads XML from stackoverflow.com, parses it, and combines it with
    // HTML markup. Returns HTML string.
    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        // Instantiate the parser
        WeatherXmlParser weatherXmlParser = new WeatherXmlParser();
        List<WeatherXmlParser.Entry> entries = null;
        String title = null;
        String url = null;
        String summary = null;

        StringBuilder htmlString = new StringBuilder();
//        htmlString.append("<h3>" + getResources().getString(R.string.page_title) + "</h3>");
//        htmlString.append("<em>" + getResources().getString(R.string.updated) + " " +
//                formatter.format(rightNow.getTime()) + "</em>");

        try {
            stream = downloadUrl(urlString);
            entries = weatherXmlParser.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
        // Each Entry object represents a single post in the XML feed.
        // This section processes the entries list to combine each entry with HTML markup.
        // Each entry is displayed in the UI as a link that optionally includes
        // a text summary.
        for (WeatherXmlParser.Entry entry : entries) {
//            htmlString.append("<p><a href='");
            htmlString.append(entry.temperature);
//            htmlString.append("'>" + entry.title + "</a></p>");
            // If the user set the preference to include summary text,
            // adds it to the display.
//            if (pref) {
//                htmlString.append(entry.summary);
//            }
        }
        return htmlString.toString();
    }

    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        java.net.URL url = new URL(urlString);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.connect();

        if (conn.getResponseCode() == 429) {
            // TODO: Notify user of too many requests
            // TODO: Catch 429 locally here
            Log.d(TAG, conn.getHeaderFields().toString());
        }
        InputStream connStream = conn.getInputStream();
        return connStream;
    }
}