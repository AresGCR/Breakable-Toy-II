package com.java.flightservice.FlightAPI.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FlightSummary{
    private long id;
    private String initialDepartureTime;
    private String finalArrivalTime;
    private String departureAirportCode;
    private String arrivalAirportCode;
    private String airlineCode;
    private String operatingAirlineCode;
    private String totalDuration;
    private double totalPrice;
    private double pricePerTraveler;
    private List<Segment> segments;

    @Getter
    @Setter
    public static class Segment {
        private String departureTime;
        private String departureAirportCode;
        private String arrivalTime;
        private String arrivalAirportCode;
        private String airlineCode;
        private String flightNumber;
        private String operatingAirlineCode;
        private String aircraftType;
    }
}

