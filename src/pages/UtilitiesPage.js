import { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import { Container, Row, Col, Card, Table, Badge, Button, Spinner, Alert, Form, Modal } from 'react-bootstrap';
import '../styles/sidebar.css';
import { authApis, endpoints } from '../configs/Apis';
import cookie from 'react-cookies';
import { useUser } from '../contexts/UserContext';
import { PDFDownloadLink, pdf } from "@react-pdf/renderer";
import InvoicePDF from '../components/InvoicePDF';

const UtilitiesPage = () => {
  const { user } = useUser(); 
  const [utilities, setUtilities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState(null);

  const [invoices, setInvoices] = useState([]);
  const [loadingInvoices, setLoadingInvoices] = useState(true);

  const [showModal, setShowModal] = useState(false);
  const [selectedUtility, setSelectedUtility] = useState(null);
  const [months, setMonths] = useState(1);
  const [submitting, setSubmitting] = useState(false);
  const [paymentMethod, setPaymentMethod] = useState("CASH");

  const [showPDF, setShowPDF] = useState(false);
  const [selectedInvoice, setSelectedInvoice] = useState(null);

  const [pdfUrl, setPdfUrl] = useState(null);


  const handleShowPDF = async (invoice) => {
    setSelectedInvoice(invoice);
    setShowPDF(true);

    const blob = await pdf(<InvoicePDF invoice={invoice} user={user} />).toBlob();
    const url = URL.createObjectURL(blob);
    setPdfUrl(url);
  };

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
        paymentProof: "",
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
      console.log("Invoice saved:", res.data);

      setShowModal(false);
    } catch (ex) {
      console.error("Lỗi đăng ký tiện ích:", ex);
      alert("Đăng ký tiện ích thất bại!");
    } finally {
      setSubmitting(false);
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

  // load invoices
  const loadInvoices = async () => {
    if (!user) return;
    try {
      setLoadingInvoices(true);
      let token = cookie.load("token");
      let res = await authApis(token).get(endpoints.getInvoices(user.id));
      setInvoices(res.data);
    } catch (ex) {
      console.error("Lỗi load invoices:", ex);
      setErr("Không thể tải danh sách hoá đơn.");
    } finally {
      setLoadingInvoices(false);
    }
  };

  useEffect(() => {
    loadInvoices();
  }, [user]);

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

        {/* Hóa đơn dịch vụ */}
        <h5 className="mb-3 mt-5">Hoá đơn - Dịch vụ</h5>
        {loadingInvoices ? (
          <Spinner animation="border" />
        ) : invoices.length === 0 ? (
          <Alert variant="info">Bạn chưa có hoá đơn nào.</Alert>
        ) : (
          <Table striped bordered hover responsive className="shadow-sm">
            <thead>
              <tr>
                <th>Mã hoá đơn</th>
                <th>Thành tiền</th>
                <th>Phương thức thanh toán</th>
                <th>Trạng thái</th>
                <th>Minh chứng</th>
                <th>Hành động</th>
              </tr>
            </thead>
            <tbody>
              {invoices.map((inv) => (
                <tr key={inv.id}>
                  <td>{inv.id}</td>
                  <td>{inv.totalAmount} VND</td>
                  <td>{inv.paymentMethod}</td>
                  <td>{statusBadge(inv.status)}</td>
                  <td>{inv.paymentProof ? inv.paymentProof : "Chưa có"}</td>
                  <td>
                    <Button
                      variant="outline-success"
                      size="sm"
                      onClick={() => handleShowPDF(inv)}
                    >
                      Xem / In
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        )}
        <Modal show={showPDF} onHide={() => setShowPDF(false)} fullscreen>
          <Modal.Header closeButton>
            <Modal.Title>Xem và In hóa đơn</Modal.Title>
          </Modal.Header>
          <Modal.Body>
          {pdfUrl && (
            <iframe
              src={pdfUrl}
              title="Invoice Preview"
              style={{ width: "100%", height: "90vh", border: "none" }}
            />
          )}
        </Modal.Body>
        </Modal>

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
