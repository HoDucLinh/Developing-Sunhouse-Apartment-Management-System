import React, { useEffect, useState } from "react";
import { Container, Row, Col, Form, Button, Card, Alert, Badge } from "react-bootstrap";
import { FiUserPlus, FiPhone, FiUsers, FiUser, FiSave, FiUserCheck } from "react-icons/fi";
import { authApis, endpoints } from "../configs/Apis";
import { useUser } from "../contexts/UserContext";
import Sidebar from "../components/Sidebar";
import Header from "../components/Header";

const Relative = () => {
  const { user } = useUser();
  const [relatives, setRelatives] = useState([]);
  const [form, setForm] = useState({
    fullName: "",
    phone: "",
    relationship: "PARENT",
  });
  const [message, setMessage] = useState(null);
  const [success, setSuccess] = useState(false);

  const loadRelatives = async () => {
    try {
      const res = await authApis().get(endpoints.getRelatives(user.id));
      setRelatives(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const addRelative = async (e) => {
    e.preventDefault();
    try {
      const payload = { ...form, userId: user.id };
      const res = await authApis().post(endpoints.addRelative, payload);
      
      setSuccess(true);
      setMessage("Thêm người thân thành công!");
      setForm({ fullName: "", phone: "", relationship: "PARENT" });
      
      // Tự động ẩn thông báo sau 3 giây
      setTimeout(() => {
        setMessage(null);
        setSuccess(false);
      }, 3000);

      loadRelatives();
    } catch (err) {
      console.error(err);
      setSuccess(false);
      setMessage("Lỗi khi thêm người thân. Vui lòng thử lại.");
    }
  };

  useEffect(() => {
    if (user?.id) loadRelatives();
  }, [user]);

  // Hàm hiển thị mối quan hệ đẹp hơn
  const getRelationshipLabel = (relationship) => {
    switch (relationship) {
      case "OWNER":
        return { label: "Chủ hộ", color: "success" };
      case "PARENT":
        return { label: "Cha/Mẹ", color: "primary" };
      case "SPOUSE":
        return { label: "Vợ/Chồng", color: "info" };
      case "CHILD":
        return { label: "Con cái", color: "warning" };
      default:
        return { label: relationship, color: "secondary" };
    }
  };

  return (
    <div className="d-flex" style={{ minHeight: "100vh", backgroundColor: "#f8f9fa" }}>
      <Sidebar />
      <Container fluid className="px-5 py-4" style={{ marginLeft: "220px" }}>
        <Header user={user} />

        <div className="d-flex justify-content-between align-items-center mb-5">
          <div>
            <h1 className="display-6 fw-bold text-dark">Người thân trong hộ</h1>
            <p className="text-muted mb-0">Quản lý thông tin người thân cư trú cùng căn hộ</p>
          </div>
          <div className="text-muted">
            Tổng số người thân: <strong>{relatives.length}</strong>
          </div>
        </div>

        <Row className="g-5">
          {/* Danh sách người thân */}
          <Col lg={7}>
            <Card className="shadow border-0 rounded-4 h-100">
              <Card.Header className="bg-white border-0 py-4">
                <h5 className="mb-0 fw-semibold">
                  <FiUsers className="me-2" /> Danh sách người thân
                </h5>
              </Card.Header>
              <Card.Body className="pt-0">
                {relatives.length === 0 ? (
                  <Alert variant="info" className="text-center py-4">
                    Chưa có thông tin người thân nào. Hãy thêm người thân vào bên phải.
                  </Alert>
                ) : (
                  <div className="d-flex flex-column gap-3">
                    {relatives.map((r) => {
                      const rel = getRelationshipLabel(r.relationship);
                      return (
                        <Card key={r.id} className="shadow-sm border-0 hover-card">
                          <Card.Body className="d-flex align-items-center py-4">
                            <div
                              className="rounded-circle bg-primary text-white d-flex justify-content-center align-items-center flex-shrink-0"
                              style={{ width: "52px", height: "52px", fontSize: "1.4rem" }}
                            >
                              <FiUser />
                            </div>

                            <div className="ms-4 flex-grow-1">
                              <div className="fw-bold fs-5 mb-1">{r.fullName}</div>
                              <div className="text-muted mb-2">{r.phone}</div>
                            </div>

                            <div>
                              <Badge 
                                bg={rel.color} 
                                pill 
                                className="px-3 py-2 fs-6"
                              >
                                {rel.label}
                              </Badge>
                            </div>
                          </Card.Body>
                        </Card>
                      );
                    })}
                  </div>
                )}
              </Card.Body>
            </Card>
          </Col>

          {/* Form thêm người thân */}
          <Col lg={5}>
            <Card className="shadow border-0 rounded-4 sticky-top" style={{ top: "20px" }}>
              <Card.Header className="bg-white border-0 py-4">
                <h5 className="mb-0 fw-semibold">
                  <FiUserPlus className="me-2" /> Thêm người thân mới
                </h5>
              </Card.Header>
              <Card.Body className="p-4">
                {message && (
                  <Alert variant={success ? "success" : "danger"} className="mb-4">
                    {message}
                  </Alert>
                )}

                <Form onSubmit={addRelative}>
                  <Form.Group className="mb-4">
                    <Form.Label className="fw-medium">
                      <FiUser className="me-2" /> Họ và tên
                    </Form.Label>
                    <Form.Control
                      type="text"
                      value={form.fullName}
                      onChange={(e) => setForm({ ...form, fullName: e.target.value })}
                      placeholder="Nhập họ và tên đầy đủ"
                      required
                      size="lg"
                      className="rounded-3"
                    />
                  </Form.Group>

                  <Form.Group className="mb-4">
                    <Form.Label className="fw-medium">
                      <FiPhone className="me-2" /> Số điện thoại
                    </Form.Label>
                    <Form.Control
                      type="text"
                      value={form.phone}
                      onChange={(e) => setForm({ ...form, phone: e.target.value })}
                      placeholder="Nhập số điện thoại"
                      size="lg"
                      className="rounded-3"
                    />
                  </Form.Group>

                  <Form.Group className="mb-3">
                    <Form.Label className="fw-medium">
                      <FiUsers className="me-2" /> Mối quan hệ
                    </Form.Label>
                    <Form.Select
                      value={form.relationship}
                      onChange={(e) => setForm({ ...form, relationship: e.target.value })}
                      size="lg"
                      className="rounded-3"
                    >
                      <option value="OWNER">Chủ hộ</option>
                      <option value="PARENT">Cha/Mẹ</option>
                      <option value="SPOUSE">Vợ/Chồng</option>
                      <option value="CHILD">Con cái</option>
                      <option value="OTHER">Khác</option>
                    </Form.Select>
                  </Form.Group>

                  <Button 
                    variant="success" 
                    type="submit" 
                    size="lg" 
                    className="w-100 py-3 rounded-3 fw-medium"
                  >
                    <FiSave className="me-2" /> Thêm người thân
                  </Button>
                </Form>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Relative;