package com.firstproject.employeebackend.service;

import com.firstproject.employeebackend.client.WeatherClient;
import com.firstproject.employeebackend.dto.WeatherRequestDTO;
import com.firstproject.employeebackend.dto.WeatherResponseDTO;
import com.firstproject.employeebackend.dto.WeatherSummaryDTO;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private final WeatherClient weatherClient;

    public WeatherService(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public WeatherSummaryDTO getWeather(String city) {

        WeatherResponseDTO response =
                weatherClient.getWeather(city);

        return new WeatherSummaryDTO(
                response.getLocation().getName(),
                response.getLocation().getCountry(),
                response.getCurrent().getTemp_c(),
                response.getCurrent().getCondition().getText(),
                response.getCurrent().getHumidity()
        );
    }

    public String sendWeatherRequest(WeatherRequestDTO request) {

        return weatherClient.sendWeatherRequest(request);
    }
}