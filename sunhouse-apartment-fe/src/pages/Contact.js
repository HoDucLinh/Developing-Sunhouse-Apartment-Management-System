import React, { useState, useEffect } from "react";
import { Container, Form, Button, Row, Col, Card } from "react-bootstrap";
import HeaderHome from "../components/HeaderHome";
import { publicApi, endpoints } from "../configs/Apis";
import {
  FiUser,
  FiMail,
  FiPhone,
  FiMessageCircle,
  FiMapPin,
} from "react-icons/fi";
import { FaCalendarCheck, FaBuilding } from "react-icons/fa";
import { SiZalo } from "react-icons/si";

const Contact = () => {
  const [apartment, setApartment] = useState(null);
  const [loading, setLoading] = useState(true);
  const [formData, setFormData] = useState({
    fullName: "",
    email: "",
    phone: "",
    note: "",
    appointmentTime: "",
  });

  useEffect(() => {
    const fetchApartment = async () => {
      try {
        const res = await publicApi.get(
          endpoints.getApartmentInfo(1)
        );

        console.log("Apartment response:", res.data);
        setApartment(res.data);

      } catch (error) {
        console.error("Error fetching apartment:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchApartment();
  }, []);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await publicApi.post(endpoints.createAppointment, formData);
      alert("Đặt lịch thành công!");
      setFormData({
        fullName: "",
        email: "",
        phone: "",
        note: "",
        appointmentTime: "",
      });
    } catch (error) {
      console.error(error);
      alert("Đặt lịch thất bại!");
    }
  };

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
              {loading ? (
              <p>Đang tải thông tin...</p>
              ) : apartment ? (
                <>
                  <p>
                    <FiUser className="me-2" />
                    <strong>{apartment.name}</strong>
                  </p>

                  <p>
                    <FiPhone className="me-2" />
                    <strong>Hotline:</strong> {apartment.hotline}
                  </p>

                  <p>
                    <FiMail className="me-2" />
                    <strong>Email:</strong> {apartment.email}
                  </p>

                  <p>
                    <FiMapPin className="me-2" />
                    <strong>Địa chỉ:</strong> {apartment.address}
                  </p>
                </>
              ) : (
                <p>Không có dữ liệu quản lý</p>
              )}
            </Card>
          </Col>

          {/* Cột 2: Form đặt lịch */}
          <Col md={6}>
            <Card className="p-4 shadow-sm border-0 rounded-4">
              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                  <Form.Label>
                    <FiUser className="me-2" />
                    Họ và tên
                  </Form.Label>
                  <Form.Control
                    type="text"
                    name="fullName"
                    value={formData.fullName}
                    onChange={handleChange}
                    placeholder="Nhập họ và tên"
                    required
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>
                    <FiMail className="me-2" />
                    Email
                  </Form.Label>
                  <Form.Control
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    placeholder="Nhập email"
                    required
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>
                    <FiPhone className="me-2" />
                    Số điện thoại
                  </Form.Label>
                  <Form.Control
                    type="tel"
                    name="phone"
                    value={formData.phone}
                    onChange={handleChange}
                    placeholder="Nhập số điện thoại"
                    required
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>
                    <FiMessageCircle className="me-2" />
                    Ghi chú
                  </Form.Label>
                  <Form.Control
                    as="textarea"
                    name="note"
                    rows={2}
                    value={formData.note}
                    onChange={handleChange}
                    placeholder="Ghi chú về dịch vụ hoặc thời gian mong muốn"
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>
                    <FaCalendarCheck className="me-2" />
                    Thời gian muốn gặp
                  </Form.Label>
                  <Form.Control
                    type="datetime-local"
                    name="appointmentTime"
                    value={formData.appointmentTime}
                    onChange={handleChange}
                    required
                  />
                </Form.Group>

                <div className="text-center mt-3">
                  <Button variant="primary" className="px-4" type="submit">
                    <FaCalendarCheck className="me-2" />
                    Đặt lịch
                  </Button>
                </div>
              </Form>
            </Card>
          </Col>
        </Row>
      </Container>
      <a
        href="https://zalo.me/0379086077"
        target="_blank"
        rel="noopener noreferrer"
        style={{
          position: "fixed",
          bottom: "20px",
          left: "20px",
          backgroundColor: "#0068ff",
          color: "white",
          borderRadius: "50%",
          width: "55px",
          height: "55px",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          boxShadow: "0 4px 10px rgba(0,0,0,0.2)",
          zIndex: 9999,
          cursor: "pointer",
        }}
      >
        <SiZalo size={30} />
      </a>
    </div>
  );
};

export default Contact;
