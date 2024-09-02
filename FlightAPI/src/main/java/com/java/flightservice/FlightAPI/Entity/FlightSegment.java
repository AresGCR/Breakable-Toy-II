package com.java.flightservice.FlightAPI.Entity;

import java.util.List;

public class FlightSegment {
    private String departureTime;
    private String departureAirportCode;
    private String departureAirportName;

    private String arrivalTime;
    private String arrivalAirportCode;
    private String arrivalAirportName;

    private String airlineName;
    private String airlineCode;
    private String flightNumber;

    private String operatingAirlineName;
    private String operatingAirlineCode;

    private String aircraftType;

    private List<TravelerFare> travelerFares;

    // Inner class TravelerFare
    public static class TravelerFare {
        private String cabin;
        private String flightClass;
        private List<Amenity> amenities;

        // Inner class Amenity
        public static class Amenity {
            private String name;
            private boolean chargeable;

            // Getters and Setters
            public String getName() { return name; }
            public void setName(String name) { this.name = name; }

            public boolean isChargeable() { return chargeable; }
            public void setChargeable(boolean chargeable) { this.chargeable = chargeable; }
        }

        // Getters and Setters
        public String getCabin() { return cabin; }
        public void setCabin(String cabin) { this.cabin = cabin; }

        public String getFlightClass() { return flightClass; }
        public void setFlightClass(String flightClass) { this.flightClass = flightClass; }

        public List<Amenity> getAmenities() { return amenities; }
        public void setAmenities(List<Amenity> amenities) { this.amenities = amenities; }
    }

    // Getters and Setters
    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public String getDepartureAirportCode() { return departureAirportCode; }
    public void setDepartureAirportCode(String departureAirportCode) { this.departureAirportCode = departureAirportCode; }

    public String getDepartureAirportName() { return departureAirportName; }
    public void setDepartureAirportName(String departureAirportName) { this.departureAirportName = departureAirportName; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public String getArrivalAirportCode() { return arrivalAirportCode; }
    public void setArrivalAirportCode(String arrivalAirportCode) { this.arrivalAirportCode = arrivalAirportCode; }

    public String getArrivalAirportName() { return arrivalAirportName; }
    public void setArrivalAirportName(String arrivalAirportName) { this.arrivalAirportName = arrivalAirportName; }

    public String getAirlineName() { return airlineName; }
    public void setAirlineName(String airlineName) { this.airlineName = airlineName; }

    public String getAirlineCode() { return airlineCode; }
    public void setAirlineCode(String airlineCode) { this.airlineCode = airlineCode; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getOperatingAirlineName() { return operatingAirlineName; }
    public void setOperatingAirlineName(String operatingAirlineName) { this.operatingAirlineName = operatingAirlineName; }

    public String getOperatingAirlineCode() { return operatingAirlineCode; }
    public void setOperatingAirlineCode(String operatingAirlineCode) { this.operatingAirlineCode = operatingAirlineCode; }

    public String getAircraftType() { return aircraftType; }
    public void setAircraftType(String aircraftType) { this.aircraftType = aircraftType; }

    public List<TravelerFare> getTravelerFares() { return travelerFares; }
    public void setTravelerFares(List<TravelerFare> travelerFares) { this.travelerFares = travelerFares; }
}
