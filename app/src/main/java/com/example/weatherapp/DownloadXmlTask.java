package com.example.weatherapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadXmlTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "DownloadXmlTask";

    public static final String URL = "https://api.met.no/weatherapi/locationforecast/1.9/?lat=60.10;lon=9.58";
    public static final String IMAGE_URL = "https://api.met.no/weatherapi/weathericon/1.1/?symbol=X&content_type=image/svg%2Bxml";

    private TextView textView;

    public DownloadXmlTask(TextView textView) {
        this.textView = textView;
    }

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
        Log.d(TAG, "Task finished! " + result);
        textView.setText(result);
    }


    // Uploads XML from stackoverflow.com, parses it, and combines it with
    // HTML markup. Returns HTML string.
    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        // Instantiate the parser
        WeatherXmlParser weatherXmlParser = new WeatherXmlParser();
        WeatherForecast weatherForecast = null;

        StringBuilder weatherString = new StringBuilder();

        try {
            stream = downloadUrl(urlString);
            weatherForecast = weatherXmlParser.parse(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        weatherString.append("Temperature: ").append(weatherForecast.temperature).append(" celsius\n");
        weatherString.append("Wind speed: ").append(weatherForecast.windSpeed).append(" mps, toward ").append(weatherForecast.windDirection);
        weatherString.append("\nCloudiness: ").append(weatherForecast.cloudiness).append("%\n");
        weatherString.append("Precipitation: between ").append(weatherForecast.precipitation.first).append(" mm and ").append(weatherForecast.precipitation.second).append(" mm\n");

        return weatherString.toString();
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