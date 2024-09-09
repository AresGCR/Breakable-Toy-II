package com.java.flightservice.FlightAPI.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.flightservice.FlightAPI.Entity.FlightSummary;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class AmadeusApiService {

    private final RestTemplate restTemplate;


    private String apiKey="o892wW5kpRyt32lhgIEqd5jeHGhfer3N";


    private String apiSecret="osp1CAExFtgjTrOt";

    public AmadeusApiService() {
        this.restTemplate = new RestTemplate();
    }

    // Get token from Amadeus API using client credentials
    public String getToken() {
        String apiUrl = "https://test.api.amadeus.com/v1/security/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", apiKey);
        body.add("client_secret", apiSecret);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
        return extractAccessTokenFromResponse(response.getBody());
    }

    // Helper method to extract access token from JSON response
    private String extractAccessTokenFromResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            return root.path("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse access token from response", e);
        }
    }

    public List<String> searchLocations(String keyword, String token) {
        String apiUrl = "https://test.api.amadeus.com/v1/reference-data/locations?subType=AIRPORT&keyword=" + keyword;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);  // Set the token in the Authorization header
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

        return extractIataCodesWithCities(response.getBody());
    }

    // Helper method to extract IATA codes and city names from the JSON response
    private List<String> extractIataCodesWithCities(String responseBody) {
        List<String> iataCodesWithCities = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode data = root.path("data");

            for (JsonNode location : data) {
                String iataCode = location.path("iataCode").asText();
                String cityName = location.path("address").path("cityName").asText();

                // Combine the IATA code with the city name
                iataCodesWithCities.add(iataCode + " (" + cityName + ")");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse IATA codes and city names from response", e);
        }
        return iataCodesWithCities;
    }
    // Method to search for flight offers
    public String fetchFlightOffers(String origin, String destination, String departureDate, String returnDate, boolean nonStop, String currency, String token) {
        // Base URL
        String apiUrl = "https://test.api.amadeus.com/v2/shopping/flight-offers";

        // Add query parameters dynamically
        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?originLocationCode=").append(origin);
        urlBuilder.append("&destinationLocationCode=").append(destination);
        urlBuilder.append("&departureDate=").append(departureDate);
        if (returnDate != null && !returnDate.isEmpty()) {
            urlBuilder.append("&returnDate=").append(returnDate);  // Optional return date for round trips
        }
        urlBuilder.append("&adults=1");  // Assuming 1 adult for simplicity
        urlBuilder.append("&nonStop=").append(nonStop);
        urlBuilder.append("&currencyCode=").append(currency);

        String finalUrl = urlBuilder.toString();
        System.out.println("Constructed URL: " + finalUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);  // Set the token in the Authorization header

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(finalUrl, HttpMethod.GET, entity, String.class);

        return response.getBody(); // Return the API response
    }

public List<FlightSummary> fetchAndParseFlightOffers(String origin, String destination, String departureDate, String returnDate, boolean nonStop, String currency) {
    String token = getToken();
    String apiUrl = buildFlightOffersUrl(origin, destination, departureDate, returnDate, nonStop, currency);
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

    return parseFlightOffers(response.getBody());
}

private String buildFlightOffersUrl(String origin, String destination, String departureDate, String returnDate, boolean nonStop, String currency) {
    StringBuilder url = new StringBuilder("https://test.api.amadeus.com/v2/shopping/flight-offers?");
    url.append("originLocationCode=").append(origin)
            .append("&destinationLocationCode=").append(destination)
            .append("&departureDate=").append(departureDate)
            .append("&adults=1")
            .append("&nonStop=").append(nonStop)
            .append("&currencyCode=").append(currency);

    if (returnDate != null && !returnDate.isEmpty()) {
        url.append("&returnDate=").append(returnDate);
    }

    return url.toString();
}

private List<FlightSummary> parseFlightOffers(String responseBody) {
    List<FlightSummary> flightSummaries = new ArrayList<>();
    JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
    JsonArray flightOffers = jsonObject.getAsJsonArray("data");

    for (int i = 0; i < flightOffers.size(); i++) {
        JsonObject flightOffer = flightOffers.get(i).getAsJsonObject();
        FlightSummary flightSummary = new FlightSummary();

        // Parse FlightSummary from JSON
        long flightId = flightOffer.has("id") ? flightOffer.get("id").getAsLong() : 0L;
        flightSummary.setId(flightId);

        JsonArray itineraries = flightOffer.getAsJsonArray("itineraries");
        JsonObject firstItinerary = itineraries.get(0).getAsJsonObject();

        JsonArray segments = firstItinerary.getAsJsonArray("segments");
        JsonObject firstSegment = segments.get(0).getAsJsonObject();
        JsonObject lastSegment = segments.get(segments.size() - 1).getAsJsonObject();

        flightSummary.setInitialDepartureTime(firstSegment.getAsJsonObject("departure").get("at").getAsString());
        flightSummary.setFinalArrivalTime(lastSegment.getAsJsonObject("arrival").get("at").getAsString());
        flightSummary.setDepartureAirportCode(firstSegment.getAsJsonObject("departure").get("iataCode").getAsString());
        flightSummary.setArrivalAirportCode(lastSegment.getAsJsonObject("arrival").get("iataCode").getAsString());
        flightSummary.setAirlineCode(firstSegment.get("carrierCode").getAsString());

        JsonObject operating = firstSegment.getAsJsonObject("operating");
        flightSummary.setOperatingAirlineCode(operating != null && operating.has("carrierCode") ? operating.get("carrierCode").getAsString() : "Unknown");

        flightSummary.setTotalDuration(firstItinerary.get("duration").getAsString());

        JsonObject price = flightOffer.getAsJsonObject("price");
        flightSummary.setTotalPrice(price.get("grandTotal").getAsDouble());

        JsonArray travelerPricings = flightOffer.getAsJsonArray("travelerPricings");
        flightSummary.setPricePerTraveler(travelerPricings.get(0).getAsJsonObject().getAsJsonObject("price").get("total").getAsDouble());

        // Parse flight segments
        List<FlightSummary.Segment> flightSegments = new ArrayList<>();
        for (int j = 0; j < segments.size(); j++) {
            JsonObject segment = segments.get(j).getAsJsonObject();
            FlightSummary.Segment flightSegment = new FlightSummary.Segment();

            flightSegment.setDepartureAirportCode(segment.getAsJsonObject("departure").get("iataCode").getAsString());
            flightSegment.setDepartureTime(segment.getAsJsonObject("departure").get("at").getAsString());
            flightSegment.setArrivalAirportCode(segment.getAsJsonObject("arrival").get("iataCode").getAsString());
            flightSegment.setArrivalTime(segment.getAsJsonObject("arrival").get("at").getAsString());
            flightSegment.setAirlineCode(segment.get("carrierCode").getAsString());

            flightSegments.add(flightSegment);
        }
        flightSummary.setSegments(flightSegments);

        flightSummaries.add(flightSummary);
    }

    return flightSummaries;
}
}




