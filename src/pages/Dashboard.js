import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import Header from '../components/Header';
import { Container, Row, Col, Card, Form } from 'react-bootstrap';
import '../styles/sidebar.css';
import { useUser } from '../contexts/UserContext';
import '../styles/dashboard.css';

const Dashboard = () => {

  const { user } = useUser();
  
  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#EEEEEE' }}>
      <Sidebar />
      <Container fluid className="py-4 px-5" style={{ marginLeft: '220px'}}>
        {user && <Header user={user} />}
        {/* Welcome Box */}
        <Card className="mb-4 shadow-sm border-0 rounded-4">
          <Card.Body className="d-flex justify-content-between align-items-center">
            <div>
              <Card className="mb-4 shadow-sm border-0 rounded-4" style={{backgroundColor:'#E8FFD7'}}>
                <Card.Body className="d-flex justify-content-between align-items-center">
                  <div>
                    <h4><strong>Xin chào, {user?.fullName} 👋</strong></h4>
                    {user?.room && (
                      <p className="mb-0">
                        Căn hộ: {user.room.roomNumber} – Tầng {user.room.floorId}<br />
                        Diện tích: {user.room.area}m²<br />
                        Số người tối đa: {user.room.maxPeople}<br />
                        Số người còn lại: {user.room.availableSlots}
                      </p>
                    )}
                  </div>
                </Card.Body>
              </Card>
            </div>
            <img
              src={user?.avatar}
              alt="avatar"
              style={{ height: '100px' }}
              className="rounded-circle"
            />
          </Card.Body>
        </Card>
        <div className="welcome-text">
          <h1>Chào mừng bạn đến với chung cư Sunhouse Apartment</h1>
        </div>
      </Container>
    </div>
  );
};

export default Dashboard;
