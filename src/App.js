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
import Home from './pages/home';
import About from './pages/About';
import PrivateRoute from './components/PrivateRoute';
import Contact from './pages/Contact';
import Relative from './pages/Relative';
import Invoice from './pages/Invoice';

function App() {
  return (
    <Router>
      <UserProvider>
      <Routes>
        {/* Các route không cần đăng nhập */}
        <Route path="/" element={<Navigate to="/home" replace />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/about" element={<About />} />
        <Route path="/contact" element={<Contact />} />
        <Route path="/home" element={<Home />} />

        {/* Các route cần đăng nhập (dùng PrivateRoute bao bọc) */}
        <Route element={<PrivateRoute />}>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/residents" element={<ResidentsPage />} />
          <Route path="/utilities" element={<UtilitiesPage />} />
          <Route path="/lockers" element={<LockerPage />} />
          <Route path="/fees" element={<FeesPage />} />
          <Route path="/cards" element={<CardsPage />} />
          <Route path="/complaints" element={<ComplaintsPage />} />
          <Route path="/surveys" element={<SurveysPage />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/relative" element={<Relative />} />
          <Route path="/invoices" element={<Invoice />} />
        </Route>
      </Routes>
    </UserProvider>
    </Router>
  );
}

export default App;