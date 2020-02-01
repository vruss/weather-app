package com.example.weatherapp.network;

import android.os.AsyncTask;
import android.util.Log;

import com.example.weatherapp.controller.WeatherXmlParser;
import com.example.weatherapp.entity.WeatherForecast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadXmlTask extends AsyncTask<String, Void, WeatherForecast>   {

    private static final String TAG = "DownloadXmlTask";

    public static final String URL = "https://api.met.no/weatherapi/locationforecast/1.9/?lat=60.10;lon=9.58";

    private AsyncResponse<WeatherForecast> delegate = null;

    public DownloadXmlTask(AsyncResponse<WeatherForecast> delegate) {
        this.delegate = delegate;
    }

    @Override
    protected WeatherForecast doInBackground(String... urls) {
        try {
            return loadXmlFromNetwork(urls[0]);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(WeatherForecast result) {
        super.onPostExecute(result);
        delegate.processFinish(result);
    }


    private WeatherForecast loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        WeatherXmlParser weatherXmlParser = new WeatherXmlParser();
        WeatherForecast weatherForecast = null;

        try {
            stream = downloadUrl(urlString);
            weatherForecast = weatherXmlParser.parse(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return weatherForecast;
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