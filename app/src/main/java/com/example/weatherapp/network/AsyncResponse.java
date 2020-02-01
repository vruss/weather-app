package com.example.weatherapp.network;

public interface AsyncResponse<T> {
        void processFinish(T output);
}
