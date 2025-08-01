import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import Header from '../components/Header';
import { Container, Row, Col, Card, Form } from 'react-bootstrap';
import { authApis,endpoints } from '../configs/Apis';
import '../styles/sidebar.css';
import { useUser } from '../contexts/UserContext';

const Dashboard = () => {

  const { user } = useUser();
  
  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#c0dbed' }}>
      <Sidebar />
      <Container fluid className="py-4 px-5" style={{ marginLeft: '220px'}}>
        {user && <Header user={user} />}
        {/* Welcome Box */}
        <Card className="mb-4 shadow-sm border-0 rounded-4">
          <Card.Body className="d-flex justify-content-between align-items-center">
            <div>
              <Card className="mb-4 shadow-sm border-0 rounded-4">
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

        {/* Tổng quan */}
        <h5 className="mb-3">Tổng quan</h5>
        <Row className="g-4">
          {[
            { title: 'Hoá đơn chưa thanh toán', count: 2 },
            { title: 'Đơn hàng chờ nhận', count: 4 },
            { title: 'Phản ánh chưa xử lý', count: 0 },
            { title: 'Khảo sát chưa hoàn thành', count: 1 },
          ].map((item, idx) => (
            <Col key={idx} xs={12} sm={6} md={3}>
              <Card className="info-card text-center h-100">
                <Card.Body>
                  <Card.Title className="mb-2">{item.title}</Card.Title>
                  <h2 className="text-primary">{item.count}</h2>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      </Container>
    </div>
  );
};

export default Dashboard;
