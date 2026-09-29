package com.firstproject.employeebackend.dto;

public class WeatherResponseDTO {

    private WeatherLocationDTO location;
    private WeatherCurrentDTO current;

    public WeatherResponseDTO() {
    }

    public WeatherLocationDTO getLocation() {
        return location;
    }

    public WeatherCurrentDTO getCurrent() {
        return current;
    }
}