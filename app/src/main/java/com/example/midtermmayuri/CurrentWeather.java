package com.example.midtermmayuri;

import java.util.zip.CheckedInputStream;

public class CurrentWeather {

    String City, temp,tempMin, tempMax, desc, humidity, windSpeed,icon;

    public CurrentWeather(String City, String temp, String tempMin, String tempMax, String desc, String humidity, String windSpeed,String icon) {
        this.City = City;
        this.temp = temp;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.desc = desc;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "CurrentWeather{" +
                "temp='" + temp + '\'' +
                ", tempMin='" + tempMin + '\'' +
                ", tempMax='" + tempMax + '\'' +
                ", desc='" + desc + '\'' +
                ", humidity='" + humidity + '\'' +
                ", windSpeed='" + windSpeed + '\'' +
                '}';
    }
}
