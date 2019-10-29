package com.example.midtermmayuri;

public class ForecastWeather {

    String time,icon,temp, humid,condition;

    public ForecastWeather(String time, String icon, String temp, String humid, String condition) {
        this.time = time;
        this.icon = icon;
        this.temp = temp;
        this.humid = humid;
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "ForecastWeather{" +
                "time='" + time + '\'' +
                ", icon='" + icon + '\'' +
                ", temp='" + temp + '\'' +
                ", humid='" + humid + '\'' +
                ", condition='" + condition + '\'' +
                '}';
    }
}
