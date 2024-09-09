import "../Styles/FlightDetail.css";

import { useLocation, useNavigate } from "react-router-dom";

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

export default function FlightDetail() {
  const location = useLocation();
  const navigate = useNavigate();

  // Extract flight data from location state
  const flight = location.state?.flight as FlightData | undefined;

  if (!flight) {
    return <div>No flight data available. Please select a flight from the list.</div>;
  }

  return (
    <div className="flight-detail-container">
      {/* Left Section for Segments */}
      <div className="flight-segment-section">
        {flight.segments.map((segment: Segment, index: number) => (
          <div key={index} className="segment-box">
            <div className="segment-header">Segment {index + 1}</div>
            <div className="segment-info">
              {new Date(segment.departureTime).toLocaleDateString()} {new Date(segment.departureTime).toLocaleTimeString()} - {new Date(segment.arrivalTime).toLocaleDateString()} {new Date(segment.arrivalTime).toLocaleTimeString()}
            </div>
            <div className="segment-info">
              {segment.departureAirportCode} ({segment.departureAirportCode}) - {segment.arrivalAirportCode} ({segment.arrivalAirportCode})
            </div>
            <div className="segment-info">
              {segment.airlineCode} ({segment.flightNumber})
            </div>
            <div className="travelers-fare-details">Travelers fare details</div>
          </div>
        ))}
      </div>

      {/* Right Section for Pricing */}
      <div className="pricing-section">
        <div className="price-breakdown">
          <div className="price-breakdown-title">Price Breakdown</div>
          <div className="price-breakdown-item">Base: </div>
          <div className="price-breakdown-item">Fees: </div>
          <div className="price-breakdown-item">Total: {flight.totalPrice}</div>
          
          {/* Per Traveler Box inside Price Breakdown */}
          <div className="per-traveler-details">
            <strong>Per Traveler:</strong> {flight.pricePerTraveler}
          </div>
        </div>
      </div>
    </div>
  );
}


