import { Route, Routes } from "react-router-dom";
import FlightSearch from "../src/Components/FlightSearch";
import Flights from "../src/Components/Flights";
import FlightDetail from "../src/Components/FlightDetail";

function App() {
  return (
    <div>
      <Routes>
        <Route path="/" element={<FlightSearch />} />
        <Route path="/flights" element={<Flights />} />
        {/* FlightDetail route does not need the ID in the URL */}
        <Route path="/flight-detail" element={<FlightDetail />} />
      </Routes>
    </div>
  );
}

export default App;
