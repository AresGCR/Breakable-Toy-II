package com.java.flightservice.FlightAPI.Controller;

import com.java.flightservice.FlightAPI.Entity.FlightSummary;
import com.java.flightservice.FlightAPI.Service.AmadeusApiService;
import com.java.flightservice.FlightAPI.Service.MockAmadeusApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FlightController {

    private final AmadeusApiService amadeusApiService;
    private final MockAmadeusApiService mockAmadeusApiService;

    @Autowired
    public FlightController(AmadeusApiService amadeusApiService, MockAmadeusApiService mockAmadeusApiService) {
        this.amadeusApiService = amadeusApiService;
        this.mockAmadeusApiService = mockAmadeusApiService;
    }

    @GetMapping("/fetch-flight-offers")
    public String fetchFlightOffers(@RequestHeader("Authorization") String token) {
        String accessToken = token.replace("Bearer ", "");
        String response = amadeusApiService.fetchFlightOffers(accessToken);
        return response;
    }

    @GetMapping("/mock-fetch-flight-summary")
    public FlightSummary mockFetchFlightSummary() {
        List<FlightSummary> flightSummaries = mockAmadeusApiService.getData();
        if (flightSummaries != null && !flightSummaries.isEmpty()) {
            return flightSummaries.get(1);
        } else {
            return null;
        }
    }

}
