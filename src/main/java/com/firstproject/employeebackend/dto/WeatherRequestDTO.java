package com.firstproject.employeebackend.dto;

public class WeatherRequestDTO {

    private String city;
    private String country;

    public WeatherRequestDTO() {
    }

    public WeatherRequestDTO(String city, String country) {
        this.city = city;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}