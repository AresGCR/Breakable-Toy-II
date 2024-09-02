import "../Styles/FlightSearch.css";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { SyncLoader } from "react-spinners";

export default function FlightSearch() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const handleSearch = async (e: any) => {
    e.preventDefault();
    setLoading(true);
    try {
      await new Promise((resolve) => setTimeout(resolve, 1000));
      navigate("/flights");
    } catch (error) {
      console.error("Error initiating flight search:", error);
    } finally {
      setLoading(false);
    }
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
          <label htmlFor="Departure">From</label>
          <input type="text" name="departure" placeholder="Departure" />
          <label htmlFor="Arrival">To</label>
          <input type="text" name="arrival" placeholder="Destination" />
          <label htmlFor="Departure">Departure Date</label>
          <input type="date" id="departureDate" name="departure" placeholder="Departure Date" />
          <label htmlFor="Arrival"> Arrival Date </label>
          <input type="date" name="arrival" id="arrivalDate" placeholder="Arrival Date" />
          <button className="scan-button" type="submit">
            Search
          </button>
        </form>
      </div>
    </div>
  );
}
