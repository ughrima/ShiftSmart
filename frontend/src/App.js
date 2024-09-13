import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Taskbar from './components/Taskbar'; // Adjust path if necessary
import ShiftCalendar from './components/ShiftCalendar'; // Adjust path if necessary

function App() {
  return (
    <Router>
      <div className="App">
        <Taskbar />
        <Routes>
          <Route path="/" element={<ShiftCalendar />} />
          {/* Add additional routes here if needed */}
        </Routes>
      </div>
    </Router>
  );
}

export default App;
