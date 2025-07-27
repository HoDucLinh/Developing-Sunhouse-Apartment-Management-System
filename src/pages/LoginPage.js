import React, { useState } from 'react';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';
import logoImg from '../assets/illustration.jpg'; // Cập nhật đúng đường dẫn ảnh

const LoginPage = () => {
  return (
    <Container fluid className="vh-100 d-flex justify-content-center align-items-center bg-light">
      <Card className="shadow-lg" style={{ width: '900px', borderRadius: '12px' }}>
        <Row className="g-0">
          {/* Form bên trái */}
          <Col md={6} className="p-5 bg-white rounded-start">
            <div className="mb-4 d-flex justify-content-center">
              <i className="bi bi-house-door-fill fs-1 text-primary"></i>
            </div>

            {/* Tiêu đề đăng nhập */}
            <h4 className="text-center mb-4"><strong>ĐĂNG NHẬP</strong></h4>

            <Form>
              <Form.Group className="mb-3">
                <Form.Label>Số điện thoại</Form.Label>
                <Form.Control type="text" placeholder="Nhập số điện thoại" />
              </Form.Group>

              <Form.Group className="mb-2">
                <Form.Label>Mật khẩu</Form.Label>
                <Form.Control type="password" placeholder="Nhập mật khẩu" />
              </Form.Group>

              <div className="text-end mb-3">
                <a href="#" className="text-decoration-none text-secondary">Quên mật khẩu?</a>
              </div>

              <Button className="w-100 mb-3" style={{ backgroundColor: '#D9D9FF', color: '#000' }}>
                Đăng nhập
              </Button>
            </Form>
          </Col>

          {/* Hình ảnh bên phải */}
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
    </Container>
  );
};

export default LoginPage;
