package com.java.flightservice.FlightAPI.Entity;

import java.util.List;

public class FlightSummary {
    private String initialDepartureTime;
    private String initialDepartureAirportCode;
    private String initialDepartureAirportName;

    private String finalArrivalTime;
    private String finalArrivalAirportCode;
    private String finalArrivalAirportName;

    private String airlineName;
    private String airlineCode;

    private String operatingAirlineName;
    private String operatingAirlineCode;

    private String totalDuration;

    private List<Stop> stops;

    private String totalPrice;
    private String pricePerTraveler;

    private List<FlightSegment> segments;

    // Inner class Stop
    public static class Stop {
        private String airportName;
        private String airportCode;
        private String layoverTime;

        // Getters and Setters
        public String getAirportName() { return airportName; }
        public void setAirportName(String airportName) { this.airportName = airportName; }

        public String getAirportCode() { return airportCode; }
        public void setAirportCode(String airportCode) { this.airportCode = airportCode; }

        public String getLayoverTime() { return layoverTime; }
        public void setLayoverTime(String layoverTime) { this.layoverTime = layoverTime; }
    }

    // Getters and Setters for FlightSummary
    public String getInitialDepartureTime() { return initialDepartureTime; }
    public void setInitialDepartureTime(String initialDepartureTime) { this.initialDepartureTime = initialDepartureTime; }

    public String getInitialDepartureAirportCode() { return initialDepartureAirportCode; }
    public void setInitialDepartureAirportCode(String initialDepartureAirportCode) { this.initialDepartureAirportCode = initialDepartureAirportCode; }

    public String getInitialDepartureAirportName() { return initialDepartureAirportName; }
    public void setInitialDepartureAirportName(String initialDepartureAirportName) { this.initialDepartureAirportName = initialDepartureAirportName; }

    public String getFinalArrivalTime() { return finalArrivalTime; }
    public void setFinalArrivalTime(String finalArrivalTime) { this.finalArrivalTime = finalArrivalTime; }

    public String getFinalArrivalAirportCode() { return finalArrivalAirportCode; }
    public void setFinalArrivalAirportCode(String finalArrivalAirportCode) { this.finalArrivalAirportCode = finalArrivalAirportCode; }

    public String getFinalArrivalAirportName() { return finalArrivalAirportName; }
    public void setFinalArrivalAirportName(String finalArrivalAirportName) { this.finalArrivalAirportName = finalArrivalAirportName; }

    public String getAirlineName() { return airlineName; }
    public void setAirlineName(String airlineName) { this.airlineName = airlineName; }

    public String getAirlineCode() { return airlineCode; }
    public void setAirlineCode(String airlineCode) { this.airlineCode = airlineCode; }

    public String getOperatingAirlineName() { return operatingAirlineName; }
    public void setOperatingAirlineName(String operatingAirlineName) { this.operatingAirlineName = operatingAirlineName; }

    public String getOperatingAirlineCode() { return operatingAirlineCode; }
    public void setOperatingAirlineCode(String operatingAirlineCode) { this.operatingAirlineCode = operatingAirlineCode; }

    public String getTotalDuration() { return totalDuration; }
    public void setTotalDuration(String totalDuration) { this.totalDuration = totalDuration; }

    public List<Stop> getStops() { return stops; }
    public void setStops(List<Stop> stops) { this.stops = stops; }

    public String getTotalPrice() { return totalPrice; }
    public void setTotalPrice(String totalPrice) { this.totalPrice = totalPrice; }

    public String getPricePerTraveler() { return pricePerTraveler; }
    public void setPricePerTraveler(String pricePerTraveler) { this.pricePerTraveler = pricePerTraveler; }

    // Getter and Setter for Segments
    public List<FlightSegment> getSegments() { return segments; }
    public void setSegments(List<FlightSegment> segments) { this.segments = segments; }
}
