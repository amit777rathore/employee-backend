package com.firstproject.employeebackend.dto;

public class WeatherCurrentDTO {

    private double temp_c;
    private int humidity;
    private WeatherConditionDTO condition;

    public WeatherCurrentDTO() {
    }

    public double getTemp_c() {
        return temp_c;
    }

    public int getHumidity() {
        return humidity;
    }

    public WeatherConditionDTO getCondition() {
        return condition;
    }
}