package com.example.weatherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ahmadrosid.svgloader.SvgLoader;
import com.example.weatherapp.entity.WeatherForecast;
import com.example.weatherapp.network.AsyncResponse;
import com.example.weatherapp.network.DownloadSvgTask;
import com.example.weatherapp.network.DownloadXmlTask;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    AppCompatActivity self;
    TextView weatherText;
    ImageView weatherImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        self = this;
        weatherText = findViewById(R.id.weather_text);
        weatherImage = findViewById(R.id.weather_image);
        Button refreshButton = findViewById(R.id.refreshButton);

        refreshButton.setOnClickListener(new buttonClickListener());

        downloadWeatherData();
    }

    private class buttonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            downloadWeatherData();
        }
    }

    private void downloadWeatherData() {
        new DownloadXmlTask(new AsyncResponse<WeatherForecast>() {
            @Override
            public void processFinish(WeatherForecast output) {
                if(output != null) {
                    weatherText.setText(output.toString());

                    // Load weather image
                    try {
                        char symbol = output.symbol.charAt(0);
                        URL url = DownloadSvgTask.createUrl(symbol);
                        SvgLoader.pluck()
                                .with(self)
                                .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                                .load(url.toString(), weatherImage);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    weatherText.setText("Connection error. Please try again.");
                }
            }
        }).execute(DownloadXmlTask.URL);
    }
}
