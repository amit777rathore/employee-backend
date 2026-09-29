package com.firstproject.employeebackend.controller;

import com.firstproject.employeebackend.dto.WeatherRequestDTO;
import com.firstproject.employeebackend.dto.WeatherResponseDTO;
import com.firstproject.employeebackend.dto.WeatherSummaryDTO;
import com.firstproject.employeebackend.service.WeatherService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
public class WeatherController {


    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{city}")
    public WeatherSummaryDTO getWeather(@PathVariable String city) {

        return weatherService.getWeather(city);
    }

    @PostMapping("/test")
    public String testPost(@RequestBody WeatherRequestDTO request) {

        return weatherService.sendWeatherRequest(request);
    }
}