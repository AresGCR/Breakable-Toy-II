import axios from "axios";

// Segment interface
export interface Segment {
  departureTime: string;
  departureAirportCode: string;
  arrivalTime: string;
  arrivalAirportCode: string;
  airlineCode: string;
  flightNumber: string;
  operatingAirlineCode: string;
  aircraftType: string;
}

// FlightData interface
export interface FlightData {
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

export const fetchFlightOffers = (
  originLocationCode: string,
  destinationLocationCode: string,
  departureDate: string,
  returnDate?: string, // Optional for round-trip
  currencyCode: string = "USD",
  nonStop: boolean = false,
  adults: number = 1,
  page: number = 0,  // Pagination
  size: number = 5   // Pagination
): Promise<FlightData[]> => {
  // Use the same backend endpoint for both one-way and round-trip offers
  const apiUrl = `http://localhost:8080/fetch-flight-offers`;

  // Build the query params
  const params = {
    originLocationCode,
    destinationLocationCode,
    departureDate,
    ...(returnDate && { returnDate }), // Include returnDate if it's a round-trip
    currencyCode,
    nonStop,
    adults,
    page,  // Add pagination parameters
    size
  };

  // Log the params for debugging
  console.log("API URL:", apiUrl);
  console.log("Request Params:", params);

  // Make the request to the backend
  return axios
    .get(apiUrl, { params })
    .then((res: any) => {
      const flights = res.data;

      // Map the flight offers into the FlightData interface
      const flightDataList: FlightData[] = flights.map((flight: any) => ({
        id: flight.id,
        initialDepartureTime: flight.initialDepartureTime,
        finalArrivalTime: flight.finalArrivalTime,
        departureAirportCode: flight.departureAirportCode,
        arrivalAirportCode: flight.arrivalAirportCode,
        airlineCode: flight.airlineCode,
        operatingAirlineCode: flight.operatingAirlineCode,
        totalDuration: flight.totalDuration,
        totalPrice: flight.totalPrice,
        pricePerTraveler: flight.pricePerTraveler,
        segments: flight.segments.map((segment: any) => ({
          departureTime: segment.departureTime,
          departureAirportCode: segment.departureAirportCode,
          arrivalTime: segment.arrivalTime,
          arrivalAirportCode: segment.arrivalAirportCode,
          airlineCode: segment.airlineCode,
          flightNumber: segment.flightNumber,
          operatingAirlineCode: segment.operatingAirlineCode,
          aircraftType: segment.aircraftType,
        })),
      }));

      return flightDataList;
    })
    .catch((error) => {
      console.error("Error fetching flight data:", error.response?.data || error.message);
      return [];
    });
};


// Search airports dynamically based on the provided keyword
export const searchAirports = (keyword: string): Promise<string[]> => {
  return axios
    .get(`http://localhost:8080/search-locations`, {
      params: { keyword },
    })
    .then((res) => res.data)
    .catch((error) => {
      console.error("Error fetching airport data:", error);
      return [];
    });
};
