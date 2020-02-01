package com.example.weatherapp;

import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.entity.WeatherForecast;
import com.example.weatherapp.network.AsyncResponse;
import com.example.weatherapp.network.DownloadSvgTask;
import com.example.weatherapp.network.DownloadXmlTask;

public class MainActivity extends AppCompatActivity {

    TextView weatherText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherText = findViewById(R.id.weather_text);
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

                    new DownloadSvgTask(new AsyncResponse<VectorDrawable>() {
                        @Override
                        public void processFinish(VectorDrawable output) {

                        }
                    }).execute(output.symbol.charAt(0));
                }
                else {
                    weatherText.setText("Connection error. Please try again.");
                }
            }
        }).execute(DownloadXmlTask.URL);
    }
}
