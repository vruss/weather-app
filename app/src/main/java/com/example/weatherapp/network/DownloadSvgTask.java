package com.example.weatherapp.network;

import android.graphics.drawable.VectorDrawable;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadSvgTask extends AsyncTask<Character, Void, VectorDrawable> {

    private static final String TAG = "DownloadSvgTask";
    public static final String IMAGE_URL = "https://api.met.no/weatherapi/weathericon/1.1/?symbol=X&content_type=image/svg%2Bxml";

    private AsyncResponse<VectorDrawable> delegate;

    public DownloadSvgTask(AsyncResponse<VectorDrawable> delegate) {
        this.delegate = delegate;
    }

    @Override
    protected VectorDrawable doInBackground(Character... characters) {
        try {
            return loadXmlFromNetwork(characters[0]);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(VectorDrawable result) {
        super.onPostExecute(result);
        delegate.processFinish(result);
    }

    private VectorDrawable loadXmlFromNetwork(char urlCharacter) throws XmlPullParserException, IOException {
        InputStream stream = null;
        VectorDrawable vectorImage = null;
//        WeatherXmlParser weatherXmlParser = new WeatherXmlParser();
//        WeatherForecast weatherForecast = null;

        try {
            stream = downloadUrl(urlCharacter);
            // TODO: Parse svg data and return VectorDrawable
//            weatherForecast = weatherXmlParser.parse(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return vectorImage;
    }

    private InputStream downloadUrl(char urlCharacter) throws IOException {
        java.net.URL url = createUrl(urlCharacter);
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

    public static URL createUrl(char symbol) throws MalformedURLException {
        String imageUrl = IMAGE_URL.replace('X', symbol);
        return new URL(imageUrl);
    }
}
