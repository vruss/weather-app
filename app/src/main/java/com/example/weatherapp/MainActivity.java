package com.example.weatherapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(this, NetworkActivity.class);
//        startActivity(intent);
        new DownloadXmlTask().execute(DownloadXmlTask.URL);
//        WeatherXmlParser.parseURL("https://api.met.no/weatherapi/locationforecast/1.9/?lat=60.10;lon=9.58");
    }


}
