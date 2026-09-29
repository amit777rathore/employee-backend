package com.firstproject.employeebackend.dto;

public class WeatherSummaryDTO {

    private String city;
    private String country;
    private double temperature;
    private String condition;
    private int humidity;

    public WeatherSummaryDTO() {
    }

    public WeatherSummaryDTO(
            String city,
            String country,
            double temperature,
            String condition,
            int humidity) {

        this.city = city;
        this.country = country;
        this.temperature = temperature;
        this.condition = condition;
        this.humidity = humidity;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getCondition() {
        return condition;
    }

    public int getHumidity() {
        return humidity;
    }
}