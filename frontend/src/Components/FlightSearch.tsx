import "../Styles/FlightSearch.css";
import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { SyncLoader } from "react-spinners";
import debounce from "lodash.debounce";
import { searchAirports } from "../Utils/api"; // Import the searchAirports function

export default function FlightSearch() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false); // Loading state
  const [error, setError] = useState<string | null>(null);

  // States for airport search
  const [departureQuery, setDepartureQuery] = useState("");
  const [arrivalQuery, setArrivalQuery] = useState("");
  const [departureOptions, setDepartureOptions] = useState<string[]>([]);
  const [arrivalOptions, setArrivalOptions] = useState<string[]>([]);
  const [selectedDeparture, setSelectedDeparture] = useState<string>("");
  const [selectedArrival, setSelectedArrival] = useState<string>("");

  // Debounced function to search airports dynamically
  const handleAirportSearch = debounce((query: string, setOptions: (options: string[]) => void) => {
    if (query) {
      searchAirports(query).then((options) => {
        setOptions(options);
      });
    }
  }, 500); // 500ms delay

  useEffect(() => {
    if (departureQuery !== selectedDeparture) {
      handleAirportSearch(departureQuery, setDepartureOptions);
    }
  }, [departureQuery]);

  useEffect(() => {
    if (arrivalQuery !== selectedArrival) {
      handleAirportSearch(arrivalQuery, setArrivalOptions);
    }
  }, [arrivalQuery]);

  const handleSearch = async (e: any) => {
    e.preventDefault();
    setError(null);
    setLoading(true); // Start loading

    const departureDateValue = e.target.departureDate.value;
    const returnDateValue = e.target.returnDate.value;
    const currency = e.target.currency.value;
    const nonstop = e.target.nonstop.checked;

    // Validate that departure and arrival airports are selected
    if (!selectedDeparture || !selectedArrival) {
      setError("Please select both departure and arrival airports.");
      setLoading(false); // Stop loading
      return;
    }

    if (!departureDateValue) {
      setError("Please select a departure date.");
      setLoading(false); // Stop loading
      return;
    }

    try {
      // Artificial delay to show the loading spinner
      await new Promise((resolve) => setTimeout(resolve, 1000));
      // Navigate to the flights page with the selected values
      navigate("/flights", {
        state: {
          originLocationCode: selectedDeparture, // Use selectedDeparture IATA code
          destinationLocationCode: selectedArrival, // Use selectedArrival IATA code
          departureDate: departureDateValue,
          returnDate: returnDateValue || null,
          currency,
          nonstop,
        },
      });
    } catch (error) {
      console.error("Error initiating flight search:", error);
    } finally {
      setLoading(false); // Stop loading
    }
  };

  const handleSelectDeparture = (iataCodeWithCity: string) => {
    const iataCode = iataCodeWithCity.split(" ")[0];
    setSelectedDeparture(iataCode); // Set the selected IATA code
    setDepartureQuery(iataCode); // Display the IATA code in the input
    setDepartureOptions([]);
  };

  const handleSelectArrival = (iataCodeWithCity: string) => {
    const iataCode = iataCodeWithCity.split(" ")[0];
    setSelectedArrival(iataCode); // Set the selected IATA code
    setArrivalQuery(iataCode); // Display the IATA code in the input
    setArrivalOptions([]);
  };

  const handleDepartureChange = (e: any) => {
    setSelectedDeparture(""); // Clear the selection only when the user starts typing again
    setDepartureQuery(e.target.value);
  };

  const handleArrivalChange = (e: any) => {
    setSelectedArrival(""); // Clear the selection only when the user starts typing again
    setArrivalQuery(e.target.value);
  };

  if (loading) {
    return (
      <div className="loading">
        <SyncLoader size={25} color={"#082f59"} loading={loading} />
      </div>
    );
  }

  return (
    <div className="scan-grid">
      <h1 className="scan-title">Search for a flight</h1>
      <div className="scan-input">
        <form className="scan-input" onSubmit={handleSearch}>
          <label htmlFor="departure">From</label>
          <div className="input-with-dropdown">
            <input
              type="text"
              name="departure"
              placeholder="Departure"
              value={departureQuery}
              onChange={handleDepartureChange}
            />
            {departureOptions.length > 0 && (
              <ul className="dropdown-options">
                {departureOptions.map((iataCodeWithCity, index) => (
                  <li key={index} onClick={() => handleSelectDeparture(iataCodeWithCity)}>
                    {iataCodeWithCity}
                  </li>
                ))}
              </ul>
            )}
          </div>

          <label htmlFor="arrival">To</label>
          <div className="input-with-dropdown">
            <input
              type="text"
              name="arrival"
              placeholder="Destination"
              value={arrivalQuery}
              onChange={handleArrivalChange}
            />
            {arrivalOptions.length > 0 && (
              <ul className="dropdown-options">
                {arrivalOptions.map((iataCodeWithCity, index) => (
                  <li key={index} onClick={() => handleSelectArrival(iataCodeWithCity)}>
                    {iataCodeWithCity}
                  </li>
                ))}
              </ul>
            )}
          </div>

          <label htmlFor="departureDate">Departure Date</label>
          <input type="date" id="departureDate" name="departureDate" placeholder="Departure Date" />

          <label htmlFor="returnDate">Return Date (Optional)</label>
          <input type="date" name="returnDate" id="returnDate" placeholder="Return Date" />

          <label htmlFor="currency">Currency</label>
          <select name="currency" id="currency">
            <option value="USD">USD</option>
            <option value="EUR">EUR</option>
            <option value="MXN">MXN</option>
          </select>

          <div className="nonstop-checkbox">
            <label>
              <input type="checkbox" name="nonstop" /> Nonstop
            </label>
          </div>

          {error && <p className="error-message">{error}</p>}

          <button className="scan-button" type="submit">
            Search
          </button>
        </form>
      </div>
    </div>
  );
}
