import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import LoginPage from './pages/LoginPage';
import Dashboard from './pages/Dashboard';
import ResidentsPage from './pages/ResidentsPage';
import UtilitiesPage from './pages/UtilitiesPage';
import FeesPage from './pages/FeesPage';
import CardsPage from './pages/CardsPage';
import ComplaintsPage from './pages/ComplaintsPage';
import SurveysPage from './pages/SurveysPage';
import LockerPage from './pages/LockerPage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/residents" element={<ResidentsPage />} />
        <Route path="/utilities" element={<UtilitiesPage />} />
        <Route path="/lockers" element={<LockerPage />} />
        <Route path="/fees" element={<FeesPage />} />
        <Route path="/cards" element={<CardsPage />} />
        <Route path="/complaints" element={<ComplaintsPage />} />
        <Route path="/surveys" element={<SurveysPage />} />
      </Routes>
    </Router>
  );
}

export default App;