import { useState, useEffect } from "react";
import { fetchFlightOffers } from "../Utils/api";
import "../Styles/Flights.css";
import { SyncLoader } from "react-spinners";
import { useNavigate, useLocation } from "react-router-dom";

// Segment and FlightData interfaces
interface Segment {
  departureTime: string;
  departureAirportCode: string;
  arrivalTime: string;
  arrivalAirportCode: string;
  airlineCode: string;
  flightNumber: string;
  operatingAirlineCode: string;
  aircraftType: string;
}

interface FlightData {
  id: number;
  initialDepartureTime: string;
  finalArrivalTime: string;
  departureAirportCode: string;
  arrivalAirportCode: string;
  airlineCode: string;
  operatingAirlineCode: string;
  totalDuration: string;
  totalPrice: number;
  pricePerTraveler: number;
  segments: Segment[];
}

// Helper function to convert ISO 8601 duration (PT8H17M) into hours and minutes
const formatDuration = (isoDuration: string) => {
  const hoursMatch = isoDuration.match(/(\d+)H/);
  const minutesMatch = isoDuration.match(/(\d+)M/);

  const hours = hoursMatch ? parseInt(hoursMatch[1], 10) : 0;
  const minutes = minutesMatch ? parseInt(minutesMatch[1], 10) : 0;

  return `${hours}h ${minutes}m`;
};

export default function Flights() {
  const location = useLocation(); // Access the state passed from FlightSearch
  const { originLocationCode, destinationLocationCode, departureDate, returnDate, currency, nonstop } =
    location.state || {}; // Extract parameters

  const [flights, setFlights] = useState<FlightData[] | null>(null);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0); // Page state
  const [size] = useState(5); // Default size of 5 records per page
  const navigate = useNavigate(); // Hook for navigation

  useEffect(() => {
    setLoading(true);
    fetchFlightOffers(originLocationCode, destinationLocationCode, departureDate, returnDate, currency, nonstop, 1, page, size)
      .then((flightData: FlightData[]) => {
        setFlights(flightData);
      })
      .catch((error) => {
        console.error("Error fetching flight data:", error);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [page, size, originLocationCode, destinationLocationCode, departureDate, returnDate, currency, nonstop]);

  const handleFlightClick = (flight: FlightData) => {
    navigate("/flight-detail", { state: { flight } });
  };

  const handleNextPage = () => {
    setPage((prevPage) => prevPage + 1);
  };

  const handlePrevPage = () => {
    if (page > 0) setPage((prevPage) => prevPage - 1);
  };

  const handleReturn = () => {
    navigate("/");
  };

  if (loading) {
    return (
      <div className="loading">
        <SyncLoader size={25} color={"#ffffff"} loading={loading} />
      </div>
    );
  }

  if (!flights || flights.length === 0) {
    return <div>No flight data available.</div>;
  }

  return (
    <section className="flight-container">
      <button className="return-button" onClick={handleReturn}>
        Return
      </button>

      {flights.map((flight) => {
        const segments = flight.segments;
        let stop = null;

        if (segments.length > 1) {
          const firstSegment = segments[0];
          const secondSegment = segments[1];
          const stopDurationMs =
            new Date(secondSegment.departureTime).getTime() -
            new Date(firstSegment.arrivalTime).getTime();
          const stopDurationHours = Math.floor(
            stopDurationMs / (1000 * 60 * 60)
          );
          const stopDurationMinutes = Math.floor(
            (stopDurationMs % (1000 * 60 * 60)) / (1000 * 60)
          );

          stop = (
            <div className="stop-details">
              <strong>
                Stop at {firstSegment.arrivalAirportCode} for {stopDurationHours}
                h {stopDurationMinutes}m
              </strong>
            </div>
          );
        }

        return (
          <div
            key={flight.id}
            className="flight-box"
            onClick={() => handleFlightClick(flight)} // Pass the entire flight data via state
          >
            <div className="flight-info">
              <span>
                {new Date(flight.initialDepartureTime).toLocaleTimeString([], {
                  hour: "2-digit",
                  minute: "2-digit",
                })}{" "}
                -{" "}
                {new Date(flight.finalArrivalTime).toLocaleTimeString([], {
                  hour: "2-digit",
                  minute: "2-digit",
                })}
              </span>
            </div>

            <div className="route-info">
              <span>
                {flight.departureAirportCode} - {flight.arrivalAirportCode}
              </span>
            </div>

            <div className="middle-section">
              <div className="flight-duration">
                <strong>Total Duration: </strong>
                <span>{formatDuration(flight.totalDuration)}</span>
              </div>

              {stop && <div className="stop">{stop}</div>}
            </div>

            <div className="price-info">
              <div className="total-price">
                <span>
                  {flight.totalPrice !== null
                    ? `$${flight.totalPrice} MXN`
                    : "N/A"}{" "}
                  total
                </span>
              </div>
              <div className="price-per-traveler">
                <span>
                  {flight.pricePerTraveler !== null
                    ? `$${flight.pricePerTraveler} MXN`
                    : "N/A"}{" "}
                  per Traveler
                </span>
              </div>
            </div>

            <div className="airline-info">
              <span>{flight.airlineCode}</span>
            </div>
          </div>
        );
      })}

      <div className="pagination-controls">
        <button onClick={handlePrevPage} disabled={page === 0}>
          Previous
        </button>
        <button onClick={handleNextPage}>Next</button>
      </div>
    </section>
  );
}
