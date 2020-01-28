package com.example.weatherapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.xmlpull.v1.XmlPullParserException;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class DownloadXmlTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "DownloadXmlTask";

    public static final String URL = "https://api.met.no/weatherapi/locationforecast/1.9/?lat=60.10;lon=9.58";

    private AppCompatActivity source;

    public DownloadXmlTask(AppCompatActivity source) {
        this.source = source;
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
        TextView weatherText = (TextView) source.findViewById(R.id.weather_text);
        weatherText.setText(result);
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

        StringBuilder weatherString = new StringBuilder();

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

        for (WeatherXmlParser.Entry entry : entries) {
            weatherString.append(entry.temperature);
            weatherString.append(entry.windDirection);
        }
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