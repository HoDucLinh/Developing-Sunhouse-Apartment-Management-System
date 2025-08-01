import React from "react";
import { Container, Form, Button, Row, Col, Card } from "react-bootstrap";
import HeaderHome from "../components/HeaderHome";
import {
  FiUser,
  FiMail,
  FiPhone,
  FiMessageCircle,
  FiMapPin,
} from "react-icons/fi";
import { FaCalendarCheck, FaBuilding } from "react-icons/fa";

const Contact = () => {
  return (
    <div style={{ marginTop: "80px" }}>
      <HeaderHome />

      <Container className="mt-4">
        <h2 className="text-center mb-4 text-primary">
          <FaCalendarCheck className="me-2" />
          Đặt Lịch Hẹn & Liên Hệ Quản Lý
        </h2>
        <Row>
          {/* Cột 1: Thông tin quản lý */}
          <Col md={6} className="mb-4">
            <Card className="p-4 shadow-sm border-0 rounded-4 h-100">
              <h4 className="text-primary mb-3">
                <FaBuilding className="me-2" />
                Thông tin người quản lý chung cư
              </h4>
              <p>
                <FiUser className="me-2" />
                <strong>Nguyễn Văn A</strong> – Trưởng ban quản lý
              </p>
              <p>
                <FiPhone className="me-2" />
                <strong>Hotline:</strong> 0909 123 456
              </p>
              <p>
                <FiMail className="me-2" />
                <strong>Email:</strong> quanly@sunhouse.vn
              </p>
              <p>
                <FiMapPin className="me-2" />
                <strong>Địa chỉ:</strong> Tầng 1, Tòa nhà A, KDC Sunhouse, TP.HCM
              </p>
            </Card>
          </Col>

          {/* Cột 2: Form đặt lịch */}
          <Col md={6}>
            <Card className="p-4 shadow-sm border-0 rounded-4">
              <Form>
                <Form.Group className="mb-3">
                  <Form.Label>
                    <FiUser className="me-2" />
                    Họ và tên
                  </Form.Label>
                  <Form.Control type="text" placeholder="Nhập họ và tên" />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>
                    <FiMail className="me-2" />
                    Email
                  </Form.Label>
                  <Form.Control type="email" placeholder="Nhập email" />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>
                    <FiPhone className="me-2" />
                    Số điện thoại
                  </Form.Label>
                  <Form.Control type="tel" placeholder="Nhập số điện thoại" />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>
                    <FiMessageCircle className="me-2" />
                    Ghi chú
                  </Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={2}
                    placeholder="Ghi chú về dịch vụ hoặc thời gian mong muốn"
                  />
                </Form.Group>

                <div className="text-center mt-3">
                  <Button variant="primary" className="px-4">
                    <FaCalendarCheck className="me-2" />
                    Đặt lịch
                  </Button>
                </div>
              </Form>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Contact;
