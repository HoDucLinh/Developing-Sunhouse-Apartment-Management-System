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
  const [errUtilities, setErrUtilities] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
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
      if (ex.response && ex.response.data) {
        let errMsg = typeof ex.response.data === "string"
          ? ex.response.data
          : (ex.response.data.error || JSON.stringify(ex.response.data));
        alert(errMsg);
      } else {
        alert("Đăng ký tiện ích thất bại!");
      }
    } finally {
      setSubmitting(false);
    }
  };

  const loadUtilities = async (kw = "") => {
    try {
      let token = cookie.load("token");
      let res = await authApis(token).get(endpoints.getUtilities(kw));
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
      const sortedUtilities = [...res.data.data].sort((a, b) => {
        return new Date(a.startDate) - new Date(b.startDate);   // cũ nhất lên trên, mới nhất xuống dưới
      });

      setUserUtilities(sortedUtilities);
    } catch (ex) {
      console.error("Lỗi load tiện ích của user:", ex);
      setErrUtilities("Không thể tải tiện ích của user.");
    }
  }, [user]);

  useEffect(() => {
    loadUserUtilities();
  }, [loadUserUtilities]);

  const handleSearch = (e) => {
    e.preventDefault();
    loadUtilities(searchTerm);
  };

  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#f8f9fa' }}>
      <Sidebar />

      <Container fluid className="px-5 py-4" style={{ marginLeft: '220px' }}>
        
        {/* Header */}
        <div className="d-flex justify-content-between align-items-center mb-5">
          <div>
            <h1 className="display-6 fw-bold text-dark">Tiện ích chung cư</h1>
            <p className="text-muted mb-0">Quản lý và đăng ký các dịch vụ tiện ích</p>
          </div>
        </div>

        {/* Search Bar */}
        <Form className="mb-5" onSubmit={handleSearch}>
          <div className="d-flex gap-2" style={{ maxWidth: '500px' }}>
            <Form.Control
              type="text"
              placeholder="Tìm kiếm dịch vụ tiện ích..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="shadow-sm"
            />
            <Button type="submit" variant="success" className="px-4">
              Tìm kiếm
            </Button>
          </div>
        </Form>

        {/* Danh sách tiện ích */}
        <h5 className="mb-4 fw-semibold text-success">Danh sách tiện ích có sẵn</h5>

        {loading ? (
          <div className="text-center py-5">
            <Spinner animation="border" variant="success" />
            <p className="mt-3 text-muted">Đang tải danh sách tiện ích...</p>
          </div>
        ) : errUtilities ? (
          <Alert variant="danger">{errUtilities}</Alert>
        ) : (
          <Row className="g-4">
            {utilities.map((u) => (
              <Col xs={12} sm={6} md={4} lg={3} key={u.id}>
                <Card className="h-100 shadow border-0 rounded-4 overflow-hidden hover-card">
                  <Card.Img
                    variant="top"
                    src={u.image}
                    alt={u.name}
                    style={{ height: "210px", objectFit: "cover" }}
                  />
                  <Card.Body className="d-flex flex-column">
                    <Card.Title className="fw-bold fs-5">{u.name}</Card.Title>
                    <Card.Text className="text-muted flex-grow-1">
                      {u.description}
                    </Card.Text>

                    <div className="mt-auto pt-3">
                      <div className="d-flex justify-content-between align-items-end mb-3">
                        <div>
                          <small className="text-muted">Giá/{u.usageDays} ngày</small>
                          <h5 className="fw-bold text-success mb-0">
                            {u.price.toLocaleString('vi-VN')} ₫
                          </h5>
                        </div>
                      </div>
                      <Button 
                        variant="success" 
                        className="w-100 rounded-3 py-2 fw-medium"
                        onClick={() => handleRegisterClick(u)}
                      >
                        Đăng ký ngay
                      </Button>
                    </div>
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>
        )}

        {/* Dịch vụ đã đăng ký */}
        <div className="mt-5 pt-5 border-top">
          <h5 className="mb-4 fw-semibold text-dark">Dịch vụ đã đăng ký của bạn</h5>
          
          <Card className="shadow border-0 rounded-4">
            <Card.Body className="p-0">
              <Table responsive hover className="mb-0">
                <thead className="table-light">
                  <tr>
                    <th>Loại dịch vụ</th>
                    <th>Phí</th>
                    <th>Trạng thái</th>
                    <th>Ngày đăng ký</th>
                    <th>Ngày hết hạn</th>
                  </tr>
                </thead>
                <tbody>
                  {userUtilities.length === 0 ? (
                    <tr>
                      <td colSpan="5" className="text-center py-5 text-muted">
                        Bạn chưa đăng ký tiện ích nào.
                      </td>
                    </tr>
                  ) : (
                    userUtilities.map((u, idx) => (
                      <tr key={idx}>
                        <td className="fw-medium">{u.feeName}</td>
                        <td className="fw-medium">
                          {u.feeAmount.toLocaleString('vi-VN')} ₫
                        </td>
                        <td>
                          {new Date(u.endDate) >= new Date() ? (
                            <Badge bg="success" pill>Đang sử dụng</Badge>
                          ) : (
                            <Badge bg="danger" pill>Hết hạn</Badge>
                          )}
                        </td>
                        <td>
                          {new Date(u.startDate).toLocaleDateString("vi-VN")}
                        </td>
                        <td>
                          {new Date(u.endDate).toLocaleDateString("vi-VN")}
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </Table>
            </Card.Body>
          </Card>
        </div>

        {/* Modal đăng ký */}
        <Modal 
          show={showModal} 
          onHide={() => setShowModal(false)} 
          centered 
          size="md"
        >
          <Modal.Header closeButton className="border-0 pb-2">
            <Modal.Title className="fw-bold">Đăng ký tiện ích</Modal.Title>
          </Modal.Header>
          <Modal.Body className="pt-2">
            {selectedUtility && (
              <div>
                <div className="text-center mb-4">
                  <h4 className="fw-bold">{selectedUtility.name}</h4>
                  <p className="text-muted mb-1">{selectedUtility.description}</p>
                </div>

                <Form.Group className="mb-4">
                  <Form.Label className="fw-medium">Phương thức thanh toán</Form.Label>
                  <Form.Select 
                    value={paymentMethod} 
                    onChange={(e) => setPaymentMethod(e.target.value)}
                    className="rounded-3"
                  >
                    <option value="CASH">Tiền mặt</option>
                    <option value="TRANSFER">Chuyển khoản</option>
                  </Form.Select>
                </Form.Group>

                <div className="bg-light p-4 rounded-3">
                  <div className="d-flex justify-content-between mb-2">
                    <span>Giá mỗi tháng:</span>
                    <strong>{selectedUtility.price.toLocaleString('vi-VN')} ₫</strong>
                  </div>
                  <div className="d-flex justify-content-between">
                    <span className="fw-bold">Tổng tiền ({months} tháng):</span>
                    <strong className="text-success fs-5">
                      {(selectedUtility.price * months).toLocaleString('vi-VN')} ₫
                    </strong>
                  </div>
                </div>
              </div>
            )}
          </Modal.Body>
          <Modal.Footer className="border-0 pt-2">
            <Button variant="secondary" onClick={() => setShowModal(false)} className="px-4">
              Hủy
            </Button>
            <Button 
              variant="success" 
              onClick={handleRegisterSubmit} 
              disabled={submitting}
              className="px-4"
            >
              {submitting ? (
                <>
                  <Spinner animation="border" size="sm" className="me-2" /> Đang xử lý...
                </>
              ) : "Xác nhận đăng ký"}
            </Button>
          </Modal.Footer>
        </Modal>
      </Container>
    </div>
  );
};

export default UtilitiesPage;