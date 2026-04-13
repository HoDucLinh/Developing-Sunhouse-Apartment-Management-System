import {useEffect, useState} from 'react';
import Sidebar from '../components/Sidebar';
import Header from '../components/Header';
import { Container, Row, Col, Card } from 'react-bootstrap';
import '../styles/sidebar.css';
import { useUser } from '../contexts/UserContext';
import '../styles/dashboard.css';
import { authApis, endpoints } from '../configs/Apis';
import cookie from 'react-cookies';

const Dashboard = () => {
  const { user } = useUser();
  const [invoices, setInvoices] = useState([]);
  const [packages, setPackages] = useState([]);
  

  const loadInvoices = async () => {
          if (!user) return;
          try {
              let token = cookie.load("token");
              let res = await authApis(token).get(endpoints.getInvoices(user.id));
              setInvoices(res.data);
          } catch (ex) {
              console.error("Lỗi load invoices:", ex);
          }
      };

  useEffect(() => {
      loadInvoices();
  }, [user]);

  const unpaidCount = invoices.filter(inv => 
        inv.status?.trim() === 'Chưa thanh toán' || 
        inv.status?.trim() === 'UNPAID').length;

  useEffect(() => {
    const loadPackages = async () => {
      if (!user?.id) return;

      try {
        const res = await authApis().get(endpoints.getPackages(user.id));
        if (res.data.success) {
          setPackages(res.data.data);
        } else {
          console.error('Lỗi từ server:', res.data.error);
        }
      } catch (err) {
        console.error('Lỗi khi gọi API packages:', err);
      }
    };

    loadPackages();
  }, [user]);

  const ungetCount = packages.filter(inv => 
        inv.status?.trim() === 'PENDING').length;

  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#f8f9fa' }}>
      <Sidebar />

      <Container fluid className="py-4 px-5" style={{ marginLeft: '220px' }}>
        {user && <Header user={user} />}

        {/* Welcome Section - Thiết kế hiện đại hơn */}
        <Card className="mb-5 shadow border-0 rounded-4 overflow-hidden">
          <Card.Body className="p-0">
            <div 
              className="welcome-banner d-flex justify-content-between align-items-center p-5"
              style={{
                background: 'linear-gradient(135deg, #4CAF50 0%, #2E7D32 100%)',
                color: 'white',
                position: 'relative',
                overflow: 'hidden'
              }}
            >
              {/* Background decoration */}
              <div 
                style={{
                  position: 'absolute',
                  top: '-50px',
                  right: '-50px',
                  width: '300px',
                  height: '300px',
                  background: 'rgba(255,255,255,0.1)',
                  borderRadius: '50%',
                  zIndex: 0
                }}
              />

              <div className="d-flex align-items-center gap-4" style={{ zIndex: 1 }}>
                <div>
                  <h1 className="display-5 fw-bold mb-2">
                    Xin chào, {user?.fullName} 👋
                  </h1>
                  <p className="fs-5 opacity-90 mb-0">
                    Chào mừng bạn trở lại với Sunhouse Apartment
                  </p>
                </div>
              </div>

              {/* Avatar */}
              <div className="text-end" style={{ zIndex: 1 }}>
                <img
                  src={user?.avatar || '/default-avatar.png'}
                  alt="avatar"
                  style={{ 
                    height: '120px', 
                    width: '120px',
                    objectFit: 'cover',
                    border: '4px solid rgba(255,255,255,0.9)',
                    boxShadow: '0 10px 30px rgba(0,0,0,0.2)'
                  }}
                  className="rounded-circle"
                />
              </div>
            </div>

            {/* Room Information Card */}
            {user?.room && (
              <div className="p-5 bg-white">
                <h5 className="text-success fw-semibold mb-4">
                  Thông tin căn hộ của bạn
                </h5>
                <Row>
                  <Col md={3} className="mb-3">
                    <div className="info-box">
                      <small className="text-muted">Số căn hộ</small>
                      <h4 className="fw-bold text-dark mb-0">
                        {user.room.roomNumber}
                      </h4>
                    </div>
                  </Col>
                  <Col md={3} className="mb-3">
                    <div className="info-box">
                      <small className="text-muted">Tầng</small>
                      <h4 className="fw-bold text-dark mb-0">
                        {user.room.floorId}
                      </h4>
                    </div>
                  </Col>
                  <Col md={3} className="mb-3">
                    <div className="info-box">
                      <small className="text-muted">Diện tích</small>
                      <h4 className="fw-bold text-dark mb-0">
                        {user.room.area} m²
                      </h4>
                    </div>
                  </Col>
                  <Col md={3} className="mb-3">
                    <div className="info-box">
                      <small className="text-muted">Số người</small>
                      <h4 className="fw-bold text-dark mb-0">
                        {user.room.maxPeople - user.room.availableSlots}/{user.room.maxPeople}
                      </h4>
                      <small className="text-success">
                        Còn {user.room.availableSlots} chỗ
                      </small>
                    </div>
                  </Col>
                </Row>
                <h5 className="text-success fw-semibold mb-4">
                  Thông tin khác
                </h5>
                <Row>
                  <Col md={6} className="mb-3">
                    {unpaidCount > 0 && (
                      <div className="info-box">
                          <small className="fw-bold">Hóa đơn chưa thanh toán</small>
                          <h3 className="mb-0 fw-bold">{unpaidCount}</h3>
                      </div>
                    )}
                  </Col>
                  <Col md={6} className="mb-3">
                    {ungetCount > 0 && (
                      <div className="info-box">
                          <small className="fw-bold">Đơn hàng chưa nhận</small>
                          <h3 className="mb-0 fw-bold">{ungetCount}</h3>
                      </div>
                    )}
                  </Col>
                </Row>
              </div>
            )}
          </Card.Body>
        </Card>

        {/* Main Welcome Text */}
        <div className="text-center py-4">
          <h2 className="display-6 fw-bold text-dark mb-3">
            Chào mừng bạn đến với
          </h2>
          <h1 className="display-4 fw-bold text-success mb-4">
            Sunhouse Apartment
          </h1>
          <p className="lead text-muted">
            Nơi an cư lý tưởng – Cuộc sống tiện nghi và hiện đại
          </p>
        </div>
      </Container>
    </div>
  );
};

export default Dashboard;