package com.example.resumescreening;

import java.util.List;

public class WeatherResponse {
    private List<WeatherData> weather;
    private MainData main;

    // Getter and setter methods for weather and main
    public List<WeatherData> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherData> weather) {
        this.weather = weather;
    }

    public MainData getMain() {
        return main;
    }

    public void setMain(MainData main) {
        this.main = main;
    }
}

