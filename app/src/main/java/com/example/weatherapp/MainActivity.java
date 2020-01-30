package com.example.weatherapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView weatherText = (TextView) findViewById(R.id.weather_text);
        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Main", "Clicked refresh button!");
                new DownloadXmlTask(weatherText).execute(DownloadXmlTask.URL);

            }
        });

    }

    private class refreshButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

        }
    }
}
