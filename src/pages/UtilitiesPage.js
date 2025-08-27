import { useEffect, useState, useCallback } from 'react';
import Sidebar from '../components/Sidebar';
import { Container, Row, Col, Card, Table, Badge, Button, Spinner, Alert, Form, Modal } from 'react-bootstrap';
import '../styles/sidebar.css';
import { authApis, endpoints } from '../configs/Apis';
import cookie from 'react-cookies';
import { useUser } from '../contexts/UserContext';


const UtilitiesPage = () => {
  const { user } = useUser(); 
  const [utilities, setUtilities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState(null);

  const [errUtilities, setErrUtilities] = useState(null);

  const [showModal, setShowModal] = useState(false);
  const [selectedUtility, setSelectedUtility] = useState(null);
  const [months, setMonths] = useState(1);
  const [submitting, setSubmitting] = useState(false);
  const [paymentMethod, setPaymentMethod] = useState("CASH");
  const [userUtilities, setUserUtilities] = useState([]);

  const handleRegisterClick = (utility) => {
    setSelectedUtility(utility);
    setMonths(1);
    setShowModal(true);
  };

  const handleRegisterSubmit = async () => {
    if (!selectedUtility) return;

    setSubmitting(true);
    try {
      let token = cookie.load("token");

      let req = {
        userId: user.id,
        paymentMethod: paymentMethod,
        paymentProof: null,
        totalAmount: selectedUtility.price * months,
        details: [
          {
            feeId: selectedUtility.id,
            amount: selectedUtility.price * months,
            note: `Đăng ký ${months} tháng`
          }
        ]
      };

      let res = await authApis(token).post(endpoints.registerUtility, req);

      alert("Đăng ký tiện ích thành công!");

      setShowModal(false);

      await loadUserUtilities();
    } catch (ex) {
      console.error("Lỗi đăng ký tiện ích:", ex);
      alert("Đăng ký tiện ích thất bại!");
    } finally {
      setSubmitting(false);
    }
  };

  const loadUtilities = async () => {
    try {
      let token = cookie.load("token");
      let res = await authApis(token).get(endpoints.getUtilities);
      setUtilities(res.data.data);
    } catch (ex) {
      console.error("Lỗi load tiện ích:", ex);
      setErrUtilities("Không thể tải danh sách tiện ích. Vui lòng thử lại sau.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadUtilities();
  }, []);

  const loadUserUtilities = useCallback(async () => {
    if (!user) return;
    try {
      let token = cookie.load("token");
      let res = await authApis(token).get(endpoints.getUtilitiesOfUser(user.id));
      setUserUtilities(res.data.data);
    } catch (ex) {
      console.error("Lỗi load tiện ích của user:", ex);
      setErrUtilities("Không thể tải tiện ích của user.");
    }
  }, [user]);

  //hiển thị các dịch vụ đã đăng kí
  useEffect(() => {
    loadUserUtilities();
  }, [loadUserUtilities]);

  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#c0dbed' }}>
      <Sidebar />
      <Container fluid className="main-content bg-light-purple px-5 py-4" style={{ marginLeft: '220px'}}>
        {/* Tiện ích */}
        <h5 className="mb-3">Tiện ích</h5>
        {loading ? (
          <Spinner animation="border" />
        ) : err ? (
          <Alert variant="danger">{errUtilities}</Alert>
        ) : (
          <Row className="g-4">
          {utilities.map((u) => (
            <Col xs={12} sm={6} md={4} lg={3} key={u.id}>
              <Card className="h-100 shadow-sm border-0 hover-shadow">
                <Card.Img
                  variant="top"
                  src={u.image}
                  alt={u.name}
                  style={{ height: "200px", objectFit: "cover" }}
                />
                <Card.Body className='text-center'>
                  <Card.Title>{u.name}</Card.Title>
                  <Card.Text className="text-muted">
                    {u.description}
                  </Card.Text>
                  <Button onClick={() => handleRegisterClick(u)}>Đăng ký ngay</Button>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
        )}
        {/* Modal đăng ký */}
        <Modal show={showModal} onHide={() => setShowModal(false)} centered>
          <Modal.Header closeButton>
            <Modal.Title>Đăng ký tiện ích</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {selectedUtility && (
              <>
                <p><strong>{selectedUtility.name}</strong></p>
                <Form.Group className="mb-3">
                  <Form.Label>Số tháng đăng ký</Form.Label>
                  <Form.Control 
                    type="number" 
                    min="1" 
                    value={months} 
                    onChange={(e) => setMonths(e.target.value)} 
                  />
                </Form.Group>

                {/* Chọn phương thức thanh toán */}
                <Form.Group className="mb-3">
                  <Form.Label>Phương thức thanh toán</Form.Label>
                  <Form.Select 
                    value={paymentMethod} 
                    onChange={(e) => setPaymentMethod(e.target.value)}
                  >
                    <option value="CASH">Tiền mặt</option>
                    <option value="TRANSFER">MOMO</option>
                  </Form.Select>
                </Form.Group>

                <p><strong>Tổng tiền: </strong> {selectedUtility.price * months} VND</p>
              </>
            )}
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowModal(false)}>
              Hủy
            </Button>
            <Button variant="primary" onClick={handleRegisterSubmit} disabled={submitting}>
              {submitting ? "Đang xử lý..." : "Xác nhận"}
            </Button>
          </Modal.Footer>
        </Modal>

        {/* Dịch vụ đã đăng ký */}
        <h5 className="mt-5 mb-3">Dịch vụ đã đăng ký</h5>
        <Table bordered hover responsive className="shadow-sm">
          <thead>
            <tr>
              <th>Loại dịch vụ</th>
              <th>Phí hàng tháng</th>
              <th>Trạng thái</th>
              <th>Ngày đăng kí</th>
            </tr>
          </thead>
          <tbody>
            {userUtilities.length === 0 ? (
              <tr>
                <td colSpan="4" className="text-center">Bạn chưa đăng ký tiện ích nào.</td>
              </tr>
            ) : (
              userUtilities.map((u, idx) => (
                <tr key={idx}>
                  <td>{u.name}</td>
                  <td>{u.price} VND</td>
                  <td><Badge bg="success">Đang sử dụng</Badge></td>
                  <td>
                    {(() => {
                      const [year, month, day] = u.createdDate.split("-");
                      return new Date(year, month - 1, day).toLocaleDateString("vi-VN");
                    })()}
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </Table>
      </Container>
    </div>
  );
};

export default UtilitiesPage;
