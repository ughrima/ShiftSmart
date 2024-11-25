
import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LandingPage from './components/LandingPage'; // The new landing page
import Taskbar from './components/Taskbar';
import ShiftCalendar from './components/ShiftCalendar';
import Report from './components/Report';
import LoginPage from './components/LoginPage';
import SignUpPage from './components/SignUpPage';
import EventSummary from './components/EventSummary';

function App() {
  // State to manage authentication
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  return (
    <Router>
      <div className="App">
        {/* Show Taskbar only if the user is logged in */}
        {isLoggedIn && <Taskbar />}

        <Routes>
          {/* Public Routes */}
          <Route path="/" element={<LandingPage />} /> {/* Default path */}
          <Route
            path="/login"
            element={<LoginPage setIsLoggedIn={setIsLoggedIn} />} // Pass login state
          />
          <Route path="/signup" element={<SignUpPage />} />

          {/* Protected Routes (Require Login) */}
          <Route
            path="/calendar"
            element={isLoggedIn ? <ShiftCalendar /> : <Navigate to="/login" />}
          />
          <Route
            path="/report"
            element={isLoggedIn ? <Report /> : <Navigate to="/login" />}
          />
          <Route
            path="/event-summary"
            element={isLoggedIn ? <EventSummary /> : <Navigate to="/login" />}
          />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
