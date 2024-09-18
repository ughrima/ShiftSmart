import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Taskbar from './components/Taskbar'; 
import ShiftCalendar from './components/ShiftCalendar'; 
import Report from './components/Report'; 
import LoginPage from './components/LoginPage'; 
import SignUpPage from './components/SignUpPage'; 
import EventSummary from './components/EventSummary'; 

function App() {
  return (
    <Router>
      <div className="App">
        <Taskbar />
        <Routes>
          <Route path="/" element={<ShiftCalendar />} />  {/* Default path */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignUpPage />} />
          <Route path="/report" element={<Report />} />
          <Route path="/event-summary" element={<EventSummary />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
