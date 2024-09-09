package com.java.flightservice.FlightAPI.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.java.flightservice.FlightAPI.Entity.FlightSummary;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class MockAmadeusApiService {
    private List<FlightSummary> flightSummaries;

    public MockAmadeusApiService() {
        this.flightSummaries = new ArrayList<>(); // Initialize as an empty list
        this.flightSummaries = getData();
    }


    public List<FlightSummary> getPaginatedData(int page, int size) {
        if (flightSummaries == null || flightSummaries.isEmpty()) {
            return new ArrayList<>();
        }
        int start = Math.min(page * size, flightSummaries.size());
        int end = Math.min((page + 1) * size, flightSummaries.size());


        return flightSummaries.subList(start, end);
    }
    public List<FlightSummary> getData() {
        try {
            // Reading the JSON file
            FileReader reader = new FileReader("src/main/resources/output.json");

            // Parsing JSON
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray flightOffers = jsonObject.getAsJsonArray("data");

            for (int i = 0; i < flightOffers.size(); i++) {
                JsonObject flightOffer = flightOffers.get(i).getAsJsonObject();

                // Extracting summary data
                FlightSummary flightSummary = new FlightSummary();

                // Parse the flight ID
                long flightId = flightOffer.has("id") ? flightOffer.get("id").getAsLong() : 0L;
                flightSummary.setId(flightId); // Store the id in the FlightSummary

                JsonArray itineraries = flightOffer.getAsJsonArray("itineraries");
                JsonObject firstItinerary = itineraries != null && itineraries.size() > 0 ? itineraries.get(0).getAsJsonObject() : null;
                if (firstItinerary == null) {
                    continue;  // Skip this flight offer if no itineraries are found
                }

                JsonArray segments = firstItinerary.getAsJsonArray("segments");
                if (segments == null || segments.size() == 0) {
                    continue;  // Skip if no segments found
                }

                JsonObject firstSegment = segments.get(0).getAsJsonObject();
                JsonObject lastSegment = segments.get(segments.size() - 1).getAsJsonObject();

                flightSummary.setInitialDepartureTime(
                        firstSegment.getAsJsonObject("departure").get("at").getAsString());
                flightSummary.setFinalArrivalTime(
                        lastSegment.getAsJsonObject("arrival").get("at").getAsString());
                flightSummary.setDepartureAirportCode(
                        firstSegment.getAsJsonObject("departure").get("iataCode").getAsString());
                flightSummary.setArrivalAirportCode(
                        lastSegment.getAsJsonObject("arrival").get("iataCode").getAsString());

                // Handling Airline Code and Name
                flightSummary.setAirlineCode(firstSegment.get("carrierCode").getAsString());
                JsonObject operating = firstSegment.getAsJsonObject("operating");
                if (operating != null && operating.has("carrierCode")) {
                    flightSummary.setOperatingAirlineCode(operating.get("carrierCode").getAsString());
                } else {
                    flightSummary.setOperatingAirlineCode("Unknown");  // or any default value
                }

                // Handle remaining fields with necessary checks
                flightSummary.setTotalDuration(firstItinerary.get("duration").getAsString());

                // Extract price data (if available)
                JsonObject price = flightOffer.has("price") ? flightOffer.getAsJsonObject("price") : null;
                if (price != null) {
                    flightSummary.setTotalPrice(price.get("grandTotal").getAsDouble());
                } else {
                    flightSummary.setTotalPrice(0.0); // or set to a default value
                }

                // Extract traveler pricing data
                JsonArray travelerPricings = flightOffer.getAsJsonArray("travelerPricings");
                if (travelerPricings != null && travelerPricings.size() > 0) {
                    JsonObject travelerPricing = travelerPricings.get(0).getAsJsonObject();
                    flightSummary.setPricePerTraveler(travelerPricing.getAsJsonObject("price").get("total").getAsDouble());
                } else {
                    flightSummary.setPricePerTraveler(0.0); // or set to a default value
                }

                // Extract segments
                List<FlightSummary.Segment> flightSegments = new ArrayList<>();
                for (int j = 0; j < segments.size(); j++) {
                    JsonObject segment = segments.get(j).getAsJsonObject();
                    FlightSummary.Segment flightSegment = new FlightSummary.Segment();

                    flightSegment.setDepartureAirportCode(
                            segment.getAsJsonObject("departure").get("iataCode").getAsString());
                    flightSegment.setDepartureTime(
                            segment.getAsJsonObject("departure").get("at").getAsString());
                    flightSegment.setArrivalAirportCode(
                            segment.getAsJsonObject("arrival").get("iataCode").getAsString());
                    flightSegment.setArrivalTime(
                            segment.getAsJsonObject("arrival").get("at").getAsString());
                    flightSegment.setAirlineCode(segment.get("carrierCode").getAsString());
                    flightSegment.setFlightNumber(segment.get("number").getAsString());

                    JsonObject segmentOperating = segment.getAsJsonObject("operating");
                    if (segmentOperating != null && segmentOperating.has("carrierCode")) {
                        flightSegment.setOperatingAirlineCode(segmentOperating.get("carrierCode").getAsString());
                    } else {
                        flightSegment.setOperatingAirlineCode("Unknown");  // or any default value
                    }

                    JsonObject aircraft = segment.getAsJsonObject("aircraft");
                    if (aircraft != null && aircraft.has("code")) {
                        flightSegment.setAircraftType(aircraft.get("code").getAsString());
                    } else {
                        flightSegment.setAircraftType("Unknown");  // or any default value
                    }

                    flightSegments.add(flightSegment);
                }
                flightSummary.setSegments(flightSegments);

                flightSummaries.add(flightSummary);
            }

            return flightSummaries;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
