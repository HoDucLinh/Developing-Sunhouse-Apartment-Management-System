import { Container, Row, Col, Card, Form } from 'react-bootstrap';

const Header = ({ user }) => (
  <Row className="mb-4">
    <Col md={6}>
      <Form.Control type="text" placeholder="Tìm kiếm..." className="rounded-pill" />
    </Col>
    <Col md={6} className="text-end">
      <img
        src={user?.avatar || "https://via.placeholder.com/40"}
        alt="avatar"
        className="rounded-circle"
        width={40}
        height={40}
      />
    </Col>
  </Row>
);

export default Header;