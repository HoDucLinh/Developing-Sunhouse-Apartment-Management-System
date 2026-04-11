import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import '../styles/sidebar.css';
import { authApis, endpoints } from '../configs/Apis';
import { useUser } from '../contexts/UserContext';
import { Container, Card, Row, Col, Form, Spinner, Alert, Badge } from 'react-bootstrap';

const LockerPage = () => {
  const { user } = useUser();
  const [packages, setPackages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [kw, setKw] = useState('');
  const [debouncedKw, setDebouncedKw] = useState('');

  // Debounce search
  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedKw(kw);
    }, 2000);

    return () => clearTimeout(handler);
  }, [kw]);

  useEffect(() => {
    const loadPackages = async () => {
      if (!user?.id) return;

      setLoading(true);
      try {
        const res = await authApis().get(endpoints.getPackages(user.id), {
          params: { kw: debouncedKw }
        });
        if (res.data.success) {
          setPackages(res.data.data);
        } else {
          console.error('Lỗi từ server:', res.data.error);
        }
      } catch (err) {
        console.error('Lỗi khi gọi API packages:', err);
      } finally {
        setLoading(false);
      }
    };

    loadPackages();
  }, [user, debouncedKw]);

  // Hàm hỗ trợ hiển thị trạng thái đẹp hơn
  const getStatusBadge = (status) => {
    switch (status?.toLowerCase()) {
      case 'approved':
      case 'đang hoạt động':
        return <Badge bg="success" pill>Đã nhận</Badge>;
      case 'expired':
      case 'hết hạn':
        return <Badge bg="danger" pill>Hết hạn</Badge>;
      case 'pending':
        return <Badge bg="warning" pill>Chờ xử lý</Badge>;
    }
  };

  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#f8f9fa' }}>
      <Sidebar />

      <Container fluid className="px-5 py-4" style={{ marginLeft: '260px' }}>
        
        {/* Header */}
        <div className="d-flex justify-content-between align-items-center mb-5">
          <div>
            <h1 className="display-6 fw-bold text-dark">Tủ locker / Gói gửi đồ</h1>
            <p className="text-muted mb-0">Quản lý các gói gửi đồ của bạn tại chung cư</p>
          </div>
        </div>

        {/* Search Bar */}
        <div className="mb-5" style={{ maxWidth: '500px' }}>
          <Form.Control
            type="text"
            placeholder="Tìm kiếm theo tên gói hoặc trạng thái..."
            value={kw}
            onChange={(e) => setKw(e.target.value)}
            className="shadow-sm"
          />
          {kw && (
            <small className="text-muted mt-1 d-block">
              Đang tìm kiếm: <strong>{kw}</strong>
            </small>
          )}
        </div>

        {/* Loading */}
        {loading ? (
          <div className="text-center py-5">
            <Spinner animation="border" variant="success" size="lg" />
            <p className="mt-3 text-muted">Đang tải danh sách gói gửi đồ...</p>
          </div>
        ) : packages.length === 0 ? (
          <Alert variant="info" className="text-center py-4">
            Không tìm thấy gói gửi đồ nào. Vui lòng thử từ khóa khác.
          </Alert>
        ) : (
          <>
            <h5 className="mb-4 fw-semibold text-success">
              Danh sách gói gửi đồ ({packages.length})
            </h5>

            <Row className="g-4">
              {packages.map((pkg) => (
                <Col xs={12} sm={6} md={4} lg={3} key={pkg.id}>
                  <Card className="h-100 shadow border-0 rounded-4 overflow-hidden hover-card">
                    <Card.Img
                      variant="top"
                      src={pkg.image || '/default-package.jpg'}
                      alt={pkg.name}
                      style={{ height: '200px', objectFit: 'cover' }}
                    />
                    
                    <Card.Body className="d-flex flex-column">
                      <Card.Title className="fw-bold fs-5 mb-3 text-center">
                        {pkg.name}
                      </Card.Title>

                      <div className="mt-auto">
                        <div className="mb-3 text-center">
                          {getStatusBadge(pkg.status)}
                        </div>

                        <div className="text-center text-muted small">
                          Ngày hết hạn:{" "}
                          <span className="fw-medium text-dark">
                            {pkg.dueDate
                              ? new Date(pkg.dueDate).toLocaleDateString("vi-VN", {
                                  day: "2-digit",
                                  month: "2-digit",
                                  year: "numeric"
                                })
                              : "Không có"}
                          </span>
                        </div>
                      </div>
                    </Card.Body>

                    <Card.Footer className="bg-light border-0 text-center py-3">
                      <small className="text-muted">Mã gói: <strong>#{pkg.id}</strong></small>
                    </Card.Footer>
                  </Card>
                </Col>
              ))}
            </Row>
          </>
        )}
      </Container>
    </div>
  );
};

export default LockerPage; 