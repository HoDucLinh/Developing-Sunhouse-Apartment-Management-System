import React, { useEffect, useState } from "react";
import { Container, Row, Col, Form, Button, Card, Alert } from "react-bootstrap";
import { FiUserPlus, FiPhone, FiUsers,FiUser, FiSave } from "react-icons/fi";
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
      setMessage("Thêm người thân thành công!");
      setForm({ fullName: "", phone: "", relationship: "PARENT" });
      loadRelatives();
    } catch (err) {
      console.error(err);
      setMessage("Lỗi khi thêm người thân.");
    }
  };

  useEffect(() => {
    if (user?.id) loadRelatives();
  }, [user]);

  return (
    <div className="d-flex" style={{ minHeight: "100vh", backgroundColor: "#eef7fb" }}>
      <Sidebar />
      <Container fluid className="py-4 px-5" style={{ marginLeft: "220px" }}>
        <Header user={user} />
        <Row>
          <Col md={6}>
            <h4 className="mb-3"><FiUsers className="me-2" />Danh sách người thân</h4>
            {relatives.length === 0 ? (
            <Alert variant="info">Chưa có người thân nào.</Alert>
            ) : (
            <div className="d-flex flex-column gap-3">
                {relatives.map((r) => (
                <Card key={r.id} className="shadow-sm border-0">
                    <Card.Body className="d-flex align-items-center">
                    <div
                        className="rounded-circle bg-primary text-white d-flex justify-content-center align-items-center"
                        style={{ width: "40px", height: "40px" }}
                    >
                        <FiUser />
                    </div>
                    <div className="ms-3">
                        <div className="fw-bold fs-5">{r.fullName}</div>
                        <div className="text-muted">{r.phone} &nbsp;|&nbsp; {r.relationship}</div>
                    </div>
                    </Card.Body>
                </Card>
                ))}
            </div>
            )}
          </Col>

          <Col md={6}>
            <h4 className="mb-3"><FiUserPlus className="me-2" />Thêm người thân</h4>
            {message && <Alert variant="success">{message}</Alert>}
            <Card className="p-3 shadow-sm">
              <Form onSubmit={addRelative}>
                <Form.Group className="mb-3">
                  <FiUser /> <Form.Label>Họ tên</Form.Label>
                  <Form.Control
                    type="text"
                    value={form.fullName}
                    onChange={(e) => setForm({ ...form, fullName: e.target.value })}
                    placeholder="Nhập họ tên"
                    required
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <FiPhone /> <Form.Label>Số điện thoại</Form.Label>
                  <Form.Control
                    type="text"
                    value={form.phone}
                    onChange={(e) => setForm({ ...form, phone: e.target.value })}
                    placeholder="Nhập số điện thoại"
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <FiUsers /> <Form.Label>Mối quan hệ</Form.Label>
                  <Form.Select
                    value={form.relationship}
                    onChange={(e) => setForm({ ...form, relationship: e.target.value })}
                  >
                    <option value="OWNER">Chủ hộ</option>
                    <option value="PARENT">Cha/Mẹ</option>
                  </Form.Select>
                </Form.Group>

                <Button variant="primary" type="submit">
                  <FiSave className="me-2" />Lưu người thân
                </Button>
              </Form>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Relative;
