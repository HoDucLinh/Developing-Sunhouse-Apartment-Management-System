// src/pages/UtilitiesPage.js
import React from 'react';
import Sidebar from '../components/Sidebar';
import { Container, Row, Col, Card, Table, Badge, Button } from 'react-bootstrap';
import '../styles/sidebar.css';

const UtilitiesPage = () => {
  const utilities = Array(6).fill({ name: 'Hồ Bơi', icon: 'https://cdn-icons-png.flaticon.com/512/808/808484.png' });

  const bills = [
    { name: 'Tiền điện', amount: '560.000', status: 'Chưa thanh toán' },
    { name: 'Vệ sinh', amount: '70.000', status: 'Đã thanh toán' },
    { name: 'Phòng gym', amount: '120.000', status: 'Đã thanh toán' },
    { name: 'Phí giữ xe', amount: '80.000', status: 'Chưa thanh toán' },
    { name: 'Tiền nước', amount: '185.000', status: 'Đã thanh toán' }
  ];

  const registeredServices = [
    { name: 'Phòng gym', fee: '120.000', status: 'Chờ duyệt', expire: '31-07-2025' },
    { name: 'Khu vui chơi', fee: '100.000', status: 'Thành viên', expire: '31-07-2025' },
    { name: 'Hồ vui chơi', fee: '120.000', status: 'Thành viên', expire: '31-07-2025' },
  ];

  const statusBadge = (status) => {
    switch (status) {
      case 'Đã thanh toán':
        return <Badge bg="success">✔ {status}</Badge>;
      case 'Chưa thanh toán':
        return <Badge bg="danger">✘ {status}</Badge>;
      case 'Chờ duyệt':
        return <Badge bg="danger">✘ {status}</Badge>;
      default:
        return <Badge bg="primary">{status}</Badge>;
    }
  };

  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#c0dbed' }}>
      <Sidebar />
      <Container fluid className="main-content bg-light-purple px-5 py-4" style={{ marginLeft: '220px'}}>
        {/* Tiện ích */}
        <h5 className="mb-3">Tiện ích</h5>
        <Row className="g-3 mb-4">
          {utilities.map((u, idx) => (
            <Col xs={6} md={2} key={idx}>
              <Card className="text-center hover-shadow">
                <Card.Body>
                  <img src={u.icon} alt={u.name} width={48} className="mb-2" />
                  <Card.Text>{u.name}</Card.Text>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>

        {/* Hóa đơn dịch vụ */}
        <h5 className="mb-3">Hoá đơn - Dịch vụ</h5>
        <Table striped bordered hover responsive className="shadow-sm">
          <thead>
            <tr>
              <th>Hoá đơn</th>
              <th>Thành tiền</th>
              <th>Trạng thái</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            {bills.map((b, idx) => (
              <tr key={idx}>
                <td>{b.name}</td>
                <td>{b.amount}</td>
                <td>{statusBadge(b.status)}</td>
                <td><Button variant="outline-primary" size="sm">Xem hoá đơn</Button></td>
              </tr>
            ))}
          </tbody>
        </Table>

        {/* Dịch vụ đã đăng ký */}
        <h5 className="mt-5 mb-3">Dịch vụ đã đăng ký</h5>
        <Table bordered hover responsive className="shadow-sm">
          <thead>
            <tr>
              <th>Loại dịch vụ</th>
              <th>Phí hàng tháng</th>
              <th>Trạng thái</th>
              <th>Ngày đến hạn</th>
            </tr>
          </thead>
          <tbody>
            {registeredServices.map((s, idx) => (
              <tr key={idx}>
                <td>{s.name}</td>
                <td>{s.fee}</td>
                <td>{statusBadge(s.status)}</td>
                <td>{s.expire}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      </Container>
    </div>
  );
};

export default UtilitiesPage;
