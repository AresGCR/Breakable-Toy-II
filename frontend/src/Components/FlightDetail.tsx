import React from "react";
import { useLocation } from "react-router-dom";
import "../Styles/FlightDetail.css";

export default function FlightDetail() {
  const location = useLocation();
  const flight = location.state?.flight;

  if (!flight) {
    return <div>No flight data available.</div>;
  }

  return (
    <section className="flight-detail-container">
      <div className="flight-detail-box">
        <h3>
          {flight.airlineName} ({flight.airlineCode})
        </h3>
        {flight.operatingAirlineName &&
          flight.operatingAirlineName !== flight.airlineName && (
            <p>
              Operating Airline: {flight.operatingAirlineName} (
              {flight.operatingAirlineCode})
            </p>
          )}
        <p>
          Departure: {flight.departureAirportName} (
          {flight.departureAirportCode}) at{" "}
          {new Date(flight.initialDepartureDateTime).toLocaleTimeString([], {
            hour: "2-digit",
            minute: "2-digit",
          })}
        </p>
        <p>
          Arrival: {flight.arrivalAirportName} (
          {flight.arrivalAirportCode}) at{" "}
          {new Date(flight.finalArrivalDateTime).toLocaleTimeString([], {
            hour: "2-digit",
            minute: "2-digit",
          })}
        </p>
        <p>Total Duration: {flight.totalFlightDuration}</p>
        <h3>Segments</h3>
        {flight.segments.map((segment: any, index: number) => (
          <div key={index} className="segment">
            <h4>Segment {index + 1}</h4>
            <p>
              {segment.departureAirportCode} to {segment.arrivalAirportCode}
            </p>
            <p>
              Flight: {segment.flightNumber} ({segment.airlineCode}), Aircraft:{" "}
              {segment.aircraftType}
            </p>
            <p>
              Departure Time:{" "}
              {new Date(segment.departureTime).toLocaleTimeString([], {
                hour: "2-digit",
                minute: "2-digit",
              })}
            </p>
            <p>
              Arrival Time:{" "}
              {new Date(segment.arrivalTime).toLocaleTimeString([], {
                hour: "2-digit",
                minute: "2-digit",
              })}
            </p>
          </div>
        ))}
        <p>Total Price: {flight.totalPrice !== null ? `$${flight.totalPrice} USD` : "N/A"}</p>
        <p>Price per Traveler: {flight.pricePerTraveler !== null ? `$${flight.pricePerTraveler} USD` : "N/A"}</p>
      </div>
    </section>
  );
}
