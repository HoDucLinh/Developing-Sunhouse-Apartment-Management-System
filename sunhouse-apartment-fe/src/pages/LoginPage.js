import React, { useState } from 'react';
import { Container, Row, Col, Card, Form, Button, Alert } from 'react-bootstrap';
import logoImg from '../assets/illustration.jpg';
import { endpoints, publicApi, authApis } from '../configs/Apis';
import cookie from 'react-cookies';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../contexts/UserContext';
import { Modal } from 'react-bootstrap';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const { setUser } = useUser();
  const [showModal, setShowModal] = useState(false);
  const [forgotUsername, setForgotUsername] = useState('');
  const [forgotEmail, setForgotEmail] = useState('');
  const [forgotError, setForgotError] = useState(null);
  const [forgotMessage, setForgotMessage] = useState(null);

  const handleLogin = async (e) => {
    e.preventDefault();
    setError(null);

    try {
      const res = await publicApi.post(endpoints.login, {
        username: username.trim(),
        password
      }, {
        withCredentials: true
      });

      const { token, username: returnedUsername } = res.data;

      cookie.save('token', token, { path: '/' });
      cookie.save('username', returnedUsername, { path: '/' });

      const profile = await authApis().get(endpoints.profile);
      setUser(profile.data);

      navigate('/dashboard');
    } catch (err) {
      console.error('Login error:', err);
      const errorMsg = err.response?.data?.message || 'Đăng nhập thất bại. Vui lòng kiểm tra lại username hoặc mật khẩu!';
      setError(errorMsg);
    }
  };
  const handleForgotPassword = async (e) => {
    e.preventDefault();
    setForgotError(null);
    setForgotMessage(null);
    try {
      const res = await publicApi.put(
        `${endpoints.forgotPassword}?username=${encodeURIComponent(forgotUsername.trim())}&email=${encodeURIComponent(forgotEmail.trim())}`
      );
      setForgotMessage(res.data.error || 'Yêu cầu đã được gửi. Vui lòng kiểm tra email của bạn!');
      setForgotUsername('');
      setForgotEmail('');
    } catch (err) {
      const errorMsg = err.response?.data?.error || "Thông tin không chính xác!";
      setForgotError(errorMsg);
    }
  };

  return (
    <Container fluid className="vh-100 d-flex justify-content-center align-items-center bg-light">
      <Card className="shadow-lg" style={{ width: '900px', borderRadius: '12px' }}>
        <Row className="g-0">
          <Col md={6} className="p-5 bg-white rounded-start">
            <div className="mb-4 d-flex justify-content-center">
              <i className="bi bi-house-door-fill fs-1 text-primary"></i>
            </div>
            <h4 className="text-center mb-4"><strong>ĐĂNG NHẬP</strong></h4>

            {error && <Alert variant="danger">{error}</Alert>}

            <Form onSubmit={handleLogin}>
              <Form.Group className="mb-3">
                <Form.Label>Tên đăng nhập</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Nhập tên đăng nhập"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                />
              </Form.Group>

              <Form.Group className="mb-2">
                <Form.Label>Mật khẩu</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="Nhập mật khẩu"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </Form.Group>

              <div className="text-end mb-3">
                <span
                  role="button"
                  className="text-decoration-none text-secondary"
                  onClick={() => {
                    setShowModal(true);
                    setForgotError(null);
                    setForgotMessage(null);
                  }}
                >
                  Quên mật khẩu?
                </span>
              </div>

              <Button type="submit" className="w-100 mb-3" style={{ backgroundColor: '#D9D9FF', color: '#000' }}>
                Đăng nhập
              </Button>
            </Form>
          </Col>

          <Col md={6} className="bg-white p-0 d-flex align-items-center justify-content-center rounded-end">
            <img
              src={logoImg}
              alt="illustration"
              className="img-fluid p-4"
              style={{ maxHeight: '450px' }}
            />
          </Col>
        </Row>
      </Card>
      <Modal show={showModal} onHide={() => setShowModal(false)} centered>
      <Modal.Header closeButton>
        <Modal.Title>Quên mật khẩu</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        {forgotError && <Alert variant="danger">{forgotError}</Alert>}
        {forgotMessage && <Alert variant="success">{forgotMessage}</Alert>}

        <Form onSubmit={handleForgotPassword}>
          <Form.Group className="mb-3">
            <Form.Label>Tên đăng nhập</Form.Label>
            <Form.Control
              type="text"
              value={forgotUsername}
              onChange={(e) => setForgotUsername(e.target.value)}
              required
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Email</Form.Label>
            <Form.Control
              type="email"
              value={forgotEmail}
              onChange={(e) => setForgotEmail(e.target.value)}
              required
            />
          </Form.Group>

          <Button type="submit" className="w-100">
            Gửi yêu cầu
          </Button>
        </Form>
      </Modal.Body>
    </Modal>
    </Container>
  );
};

export default LoginPage;
