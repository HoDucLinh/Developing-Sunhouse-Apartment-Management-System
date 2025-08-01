import { Row, Col, Form } from 'react-bootstrap';

const Header = ({ user }) => (
  <Row className="align-items-center mb-4 px-3">
    {/* Ô tìm kiếm */}
    <Col md={6}>
      <Form.Control
        type="text"
        placeholder="🔍 Tìm kiếm..."
        className="rounded-pill px-4 shadow-sm"
        style={{ maxWidth: '100%' }}
      />
    </Col>

    {/* Tên và avatar bên phải */}
    <Col className="d-flex justify-content-end align-items-center gap-3">
      <strong className="text-dark">{user?.fullName || 'Khách'}</strong>
      <img
        src={user?.avatar || 'https://via.placeholder.com/40'}
        alt="avatar"
        className="rounded-circle border shadow-sm"
        width={40}
        height={40}
      />
    </Col>
  </Row>
);

export default Header;
