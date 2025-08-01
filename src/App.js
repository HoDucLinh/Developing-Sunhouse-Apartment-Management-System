import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate  } from 'react-router-dom';

import LoginPage from './pages/LoginPage';
import Dashboard from './pages/Dashboard';
import ResidentsPage from './pages/ResidentsPage';
import UtilitiesPage from './pages/UtilitiesPage';
import FeesPage from './pages/FeesPage';
import CardsPage from './pages/CardsPage';
import ComplaintsPage from './pages/ComplaintsPage';
import SurveysPage from './pages/SurveysPage';
import LockerPage from './pages/LockerPage';
import { UserProvider } from './contexts/UserContext';
import Profile from './pages/Profile';

function App() {
  return (
    <Router>
      <UserProvider>
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/residents" element={<ResidentsPage />} />
        <Route path="/utilities" element={<UtilitiesPage />} />
        <Route path="/lockers" element={<LockerPage />} />
        <Route path="/fees" element={<FeesPage />} />
        <Route path="/cards" element={<CardsPage />} />
        <Route path="/complaints" element={<ComplaintsPage />} />
        <Route path="/surveys" element={<SurveysPage />} />
        <Route path="/profile" element={<Profile />} />
      </Routes>
    </UserProvider>
    </Router>
  );
}

export default App;