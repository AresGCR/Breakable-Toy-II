import { useState, useEffect } from "react";
import { getFlight } from "../Utils/api";
import "../Styles/Flights.css";
import { SyncLoader } from "react-spinners";

interface FlightData {
  flightId: string;
  initialDepartureDateTime: string;
  finalArrivalDateTime: string;
  departureAirportName: string;
  departureAirportCode: string;
  arrivalAirportName: string;
  arrivalAirportCode: string;
  airlineName: string;
  airlineCode: string;
  operatingAirlineName?: string; // Optional if different from the main airline
  operatingAirlineCode?: string; // Optional if different from the main airline
  totalFlightDuration: string;
  stops?: Array<{
    airportName: string;
    airportCode: string;
    layoverTime: string;
  }>;
  totalPrice: number | null;
  pricePerTraveler: number | null;
  segments: Array<{
    departureTime: string;
    departureAirportCode: string;
    arrivalTime: string;
    arrivalAirportCode: string;
    airlineCode: string;
    flightNumber: string;
    operatingAirlineCode: string;
    aircraftType: string;
  }>;
}

export default function Flights() {
  const [flight, setFlight] = useState<FlightData | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getFlight()
      .then((flightData: FlightData) => {
        setFlight(flightData); // Store the single flight object
      })
      .catch((error) => {
        console.error("Error fetching flight data:", error);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  if (loading) {
    return (
      <div className="loading">
        <SyncLoader size={25} color={"#082f59"} loading={loading} />
      </div>
    );
  }

  if (!flight) {
    return <div>No flight data available.</div>;
  }

  return (
    <section className="flight-container">
      <div className="flight-box">
        <div className="flight-details">
          <div className="flight-times">
            <span>
              {new Date(flight.initialDepartureDateTime).toLocaleTimeString([], {
                hour: '2-digit',
                minute: '2-digit',
              })}{" "}
              -{" "}
              {new Date(flight.finalArrivalDateTime).toLocaleTimeString([], {
                hour: '2-digit',
                minute: '2-digit',
              })}
            </span>
          </div>
          <div className="flight-route">
            <span>
              {flight.departureAirportName} ({flight.departureAirportCode}) -{" "}
              {flight.arrivalAirportName} ({flight.arrivalAirportCode})
            </span>
          </div>
          <div className="flight-duration">
            <span>{flight.totalFlightDuration} (Nonstop)</span>
          </div>
          <div className="flight-prices">
            <div className="total-price">
              <span>
                {flight.totalPrice !== null ? `$${flight.totalPrice} USD` : "N/A"} total
              </span>
            </div>
            <div className="price-per-traveler">
              <span>
                {flight.pricePerTraveler !== null
                  ? `$${flight.pricePerTraveler} USD`
                  : "N/A"}{" "}
                per Traveler
              </span>
            </div>
          </div>
          <div className="flight-airline">
            <span>
              {flight.airlineName} ({flight.airlineCode})
            </span>
          </div>
        </div>
      </div>
    </section>
  );
}
