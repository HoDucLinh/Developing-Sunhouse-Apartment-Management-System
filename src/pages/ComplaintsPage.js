import React from 'react';
import Sidebar from '../components/Sidebar';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';

const ComplaintsPage = () => {
  const complaints = [
    {
      id: 1,
      resident: 'Nguyễn Văn A',
      room: 'P101',
      content: 'Tiếng ồn lớn sau 22h',
      status: 'Đang xử lý',
    },
    {
      id: 2,
      resident: 'Trần Thị B',
      room: 'P202',
      content: 'Thang máy bị hỏng',
      status: 'Đã xử lý',
    },
  ];

  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#e5f1fb' }}>
      <Sidebar />
      <Container fluid className="py-4 px-5" style={{ marginLeft: '250px' }}>
        <Row className="align-items-center justify-content-between mb-4">
          <Col xs="auto">
            <input type="text" className="form-control" placeholder="Tìm kiếm khiếu nại..." style={{ width: '250px' }} />
          </Col>
          <Col xs="auto">
            <img
              src="https://www.vietnamworks.com/hrinsider/wp-content/uploads/2023/12/anh-den-ngau-012.jpg"
              alt="avatar"
              className="rounded-circle"
              width={40}
              height={40}
            />
          </Col>
        </Row>

        <Row className="g-4">
          {complaints.map((complaint) => (
            <Col key={complaint.id} xs={12} sm={6} md={4}>
              <Card className="border-0 shadow-sm rounded-4 p-3">
                <Card.Body>
                  <Card.Title className="fs-6 fw-bold">{complaint.resident}</Card.Title>
                  <Card.Text className="mb-1 text-muted">Phòng: {complaint.room}</Card.Text>
                  <Card.Text className="mb-1">{complaint.content}</Card.Text>
                  <Card.Text className="mb-2">
                    Trạng thái: <strong>{complaint.status}</strong>
                  </Card.Text>
                  <Button variant="primary" size="sm">Xem chi tiết</Button>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      </Container>
    </div>
  );
};

export default ComplaintsPage;
