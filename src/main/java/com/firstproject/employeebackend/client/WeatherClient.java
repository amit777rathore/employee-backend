package com.firstproject.employeebackend.client;

import com.firstproject.employeebackend.dto.WeatherRequestDTO;
import com.firstproject.employeebackend.dto.WeatherResponseDTO;
import com.firstproject.employeebackend.exception.ExternalApiException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class WeatherClient {

    private static final Logger log =
            LoggerFactory.getLogger(WeatherClient.class);

    private final RestClient weatherRestClient;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherClient(RestClient weatherRestClient) {
        this.weatherRestClient = weatherRestClient;
    }

    @CircuitBreaker(name = "weatherApi",
            fallbackMethod = "weatherFallback")
    @Retry(name = "weatherApi")
    public WeatherResponseDTO getWeather(String city) {
        log.info(">>> RETRY TEST: getWeather() executed");
        throw new RuntimeException("Temporary 500 test");

//        return weatherRestClient
//                .get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/v1/current-invalid.json")
//                        .queryParam("key", apiKey)
//                        .queryParam("q", city)
//                        .build())
//                .header("Accept", "application/json")
//                .retrieve()
//                .body(WeatherResponseDTO.class);
    }

    public String sendWeatherRequest(WeatherRequestDTO request) {

        log.debug("Weather API request received for city={}", request.getCity());

        log.info("Calling Weather API for city: {}", request.getCity());

        try {

            return weatherRestClient
                    .post()
                    .uri("https://this-domain-does-not-exist-12345.com/post")
                    .header("Content-Type", "application/json")
                    .body(request)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError(),
                            (req, res) -> {
                                throw new ExternalApiException(
                                        "Weather API returned client error: "
                                                + res.getStatusCode()
                                );
                            }
                    )
                    .onStatus(
                            status -> status.is5xxServerError(),
                            (req, res) -> {
                                throw new ExternalApiException(
                                        "Weather API returned server error: "
                                                + res.getStatusCode()
                                );
                            }
                    )
                    .body(String.class);

        } catch (ExternalApiException ex) {
            log.error("Unable to connect to Weather API--", ex);
            throw ex;

        } catch (Exception ex) {
            log.error("Unable to connect to Weather API", ex);
            throw new ExternalApiException(
                    "Unable to connect to Weather API"
            );
        }
    }

    private WeatherResponseDTO weatherFallback(
            String city,
            Exception ex
    ) {
        System.out.println("Weather API fallback triggered for city: " + city);

        throw new ExternalApiException(
                "Weather service is temporarily unavailable. Please try again later."
        );
    }
}