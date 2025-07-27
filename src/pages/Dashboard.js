import React from 'react';
import Sidebar from '../components/Sidebar';
import { Container, Row, Col, Card, Form } from 'react-bootstrap';
import '../styles/sidebar.css';

const Dashboard = () => {
  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#c0dbed' }}>
      <Sidebar />

      <Container fluid className="py-4 px-5" style={{ marginLeft: '220px'}}>
        {/* Search & Avatar */}
        <Row className="mb-4">
          <Col md={6}>
            <Form.Control type="text" placeholder="Tìm kiếm..." className="rounded-pill" />
          </Col>
          <Col md={6} className="text-end">
            <img
              src="https://www.vietnamworks.com/hrinsider/wp-content/uploads/2023/12/anh-den-ngau-012.jpg"
              alt="avatar"
              className="rounded-circle"
              width={40}
              height={40}
            />
          </Col>
        </Row>

        {/* Welcome Box */}
        <Card className="mb-4 shadow-sm border-0 rounded-4">
          <Card.Body className="d-flex justify-content-between align-items-center">
            <div>
              <h4><strong>Xin chào, User! 👋</strong></h4>
              <p className="mb-0">
                Căn hộ: P.1203 – Block B<br />
                Diện tích: 80m² – Tầng 12<br />
                Sống từ: 01/03/2023
              </p>
            </div>
            <img
              src="https://www.vietnamworks.com/hrinsider/wp-content/uploads/2023/12/anh-den-ngau-012.jpg"
              alt="rocket"
              style={{ height: '100px' }}
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
