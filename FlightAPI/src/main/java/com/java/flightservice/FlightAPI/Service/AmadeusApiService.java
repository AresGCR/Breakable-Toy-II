package com.java.flightservice.FlightAPI.Service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AmadeusApiService {

    private final RestTemplate restTemplate;

    public AmadeusApiService() {
        this.restTemplate = new RestTemplate();
    }

    public String fetchFlightOffers(String token) {
        String apiUrl = "https://test.api.amadeus.com/v2/shopping/flight-offers?originLocationCode=SYD&destinationLocationCode=BKK&departureDate=2024-09-01&adults=1&nonStop=false&max=250";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);  // Set the token in the Authorization header

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

        return response.getBody();  // Return the API response
    }
}
