package com.example.weatherapp.entity;

import android.util.Pair;

public class WeatherForecast {

    public String temperature;
    public String windSpeed;
    public String windDirection;
    public String cloudiness;
    public Pair<String, String> precipitation;
    public String symbol;

    public WeatherForecast(String temperature, String windSpeed, String windDirection, String cloudiness, Pair<String, String> precipitation, String symbol) {
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.cloudiness = cloudiness;
        this.precipitation = precipitation;
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "Temperature: " + temperature + " celsius\n" +
                "Wind speed: " + windSpeed + " mps, toward " + windDirection + "\n" +
                "Cloudiness: " + cloudiness + "%\n" +
                "Precipitation: between " + precipitation.first + " mm and " + precipitation.second + " mm\n";
    }
}
