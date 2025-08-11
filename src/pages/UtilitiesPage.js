// src/pages/UtilitiesPage.js
import { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import { Container, Row, Col, Card, Table, Badge, Button, Spinner, Alert } from 'react-bootstrap';
import '../styles/sidebar.css';
import { authApis, endpoints } from '../configs/Apis';
import cookie from 'react-cookies';
import { Link } from 'react-router-dom';

const UtilitiesPage = () => {
  const [utilities, setUtilities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState(null);


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

  useEffect(() => {
    const loadUtilities = async () => {
      try {
        let token = cookie.load("token");
        let res = await authApis(token).get(endpoints.getUtilities);
        setUtilities(res.data.data);
      } catch (ex) {
        console.error("Lỗi load tiện ích:", ex);
        setErr("Không thể tải danh sách tiện ích. Vui lòng thử lại sau.");
      } finally {
        setLoading(false);
      }
    };
    loadUtilities();
  }, []);

  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#c0dbed' }}>
      <Sidebar />
      <Container fluid className="main-content bg-light-purple px-5 py-4" style={{ marginLeft: '220px'}}>
        {/* Tiện ích */}
        <h5 className="mb-3">Tiện ích</h5>
        {loading ? (
          <Spinner animation="border" />
        ) : err ? (
          <Alert variant="danger">{err}</Alert>
        ) : (
          <Row className="g-4">
          {utilities.map((u) => (
            <Col xs={12} sm={6} md={4} lg={3} key={u.id}>
              <Link to={`/utilities/${u.id}`} className="text-decoration-none">
                <Card className="h-100 shadow-sm border-0 hover-shadow">
                  <Card.Img
                    variant="top"
                    src={u.image}
                    alt={u.name}
                    style={{ height: "200px", objectFit: "cover" }}
                  />
                  <Card.Body>
                    <Card.Title>{u.name}</Card.Title>
                    <Card.Text className="text-muted">
                      {u.description}
                    </Card.Text>
                  </Card.Body>
                </Card>
              </Link>
            </Col>
          ))}
        </Row>
        )}

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
