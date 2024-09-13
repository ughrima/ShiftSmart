import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import  Login from '../src/components/LoginPage';
import  Report from '../src/components/Report';
import  EventSummary from '../src/components/EventSummary';
import Taskbar from '../src/components/Taskbar';
function App() {
  return (
    <Router>
      <div className="App">
         <Taskbar />
      </div>
    </Router>
  );
}

export default App;
