package com.java.flightservice.FlightAPI.Controller;

import com.java.flightservice.FlightAPI.Entity.FlightSummary;
import com.java.flightservice.FlightAPI.Service.AmadeusApiService;
import com.java.flightservice.FlightAPI.Service.MockAmadeusApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    // Endpoint to search for airport locations using the provided keyword
    @GetMapping("/search-locations")
    public ResponseEntity<List<String>> searchLocations(@RequestParam String keyword) {
        // Get the access token from Amadeus API
        String token = amadeusApiService.getToken();

        // Search for locations using the keyword and token
        List<String> iataCodesWithCities = amadeusApiService.searchLocations(keyword, token);

        // Return the list of IATA codes and cities, or no content if no data found
        if (iataCodesWithCities != null && !iataCodesWithCities.isEmpty()) {
            return ResponseEntity.ok(iataCodesWithCities);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    // Updated mock-fetch-flight-summary with pagination
    @GetMapping("/mock-fetch-flight-summary")
    public List<FlightSummary> mockFetchFlightSummary(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return mockAmadeusApiService.getPaginatedData(page, size);
    }
    @GetMapping("/fetch-flight-offers")
    public ResponseEntity<List<FlightSummary>> fetchFlightOffers(
            @RequestParam String originLocationCode,
            @RequestParam String destinationLocationCode,
            @RequestParam String departureDate,
            @RequestParam(required = false) String returnDate,
            @RequestParam(defaultValue = "false") boolean nonStop,
            @RequestParam(defaultValue = "EUR") String currency,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        List<FlightSummary> allFlightSummaries = amadeusApiService.fetchAndParseFlightOffers(
                originLocationCode, destinationLocationCode, departureDate, returnDate, nonStop, currency
        );


        int start = Math.min(page * size, allFlightSummaries.size());
        int end = Math.min((page + 1) * size, allFlightSummaries.size());
        List<FlightSummary> paginatedSummaries = allFlightSummaries.subList(start, end);

        return ResponseEntity.ok(paginatedSummaries);
    }
}
