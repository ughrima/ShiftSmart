import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import  Login from '../src/components/LoginPage';
import  Report from '../src/components/Report';
import  EventSummary from '../src/components/EventSummary';

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/Report" element={<Report />} />
          <Route path="/EventSummary" element={<EventSummary />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
