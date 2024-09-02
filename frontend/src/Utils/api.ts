import axios from "axios";

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

export const getFlight = (): Promise<FlightData> => {
  return axios
    .get(`http://localhost:8080/mock-fetch-flight-summary`)
    .then((res: any) => {
      const flight = res.data;

      // Ensure the returned object matches FlightData exactly
      const flightData: FlightData = {
        flightId: flight.flightId || "Unknown", // Add a default or expected value
        initialDepartureDateTime: flight.initialDepartureTime,
        finalArrivalDateTime: flight.finalArrivalTime,
        departureAirportName: flight.initialDepartureAirportName || "Unknown",
        departureAirportCode: flight.initialDepartureAirportCode || "Unknown",
        arrivalAirportName: flight.finalArrivalAirportName || "Unknown",
        arrivalAirportCode: flight.finalArrivalAirportCode || "Unknown",
        airlineName: flight.airlineName || "Unknown",
        airlineCode: flight.airlineCode || "Unknown",
        operatingAirlineName: flight.operatingAirlineName || flight.airlineName,
        operatingAirlineCode: flight.operatingAirlineCode || flight.airlineCode,
        totalFlightDuration: flight.totalDuration,
        stops: flight.stops || [],
        totalPrice: flight.totalPrice || null,
        pricePerTraveler: flight.pricePerTraveler || null,
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
      };

      return flightData;
    });
};
