import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import { Button, Modal, Form, Card, Row, Col } from 'react-bootstrap';
import { authApis, endpoints } from '../configs/Apis';
import { useUser } from '../contexts/UserContext';

import img from '../images/the-cu-dan.png'; // Ảnh cố định cho tất cả các thẻ

const CardsPage = () => {
  const { user } = useUser();
  const [cards, setCards] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [relatives, setRelatives] = useState([]);
  const [selectedRelative, setSelectedRelative] = useState(null);

  // 1️⃣ Lấy danh sách card
  useEffect(() => {
    const fetchCards = async () => {
      if (!user) return;
      try {
        const res = await authApis().get(endpoints.getCardByUserId(user.id));
        setCards(Array.isArray(res.data) ? res.data : []);
      } catch (err) {
        console.error("Lỗi khi load cards:", err);
        setCards([]);
      } finally {
        setLoading(false);
      }
    };
    fetchCards();
  }, [user]);

  // 2️⃣ Mở modal tạo card → load relatives
  const openCreateCardModal = async () => {
    if (!user) return;
    try {
      const res = await authApis().get(endpoints.getRelatives(user.id));
      setRelatives(Array.isArray(res.data) ? res.data : []);
      setSelectedRelative(null);
      setShowModal(true);
    } catch (err) {
      console.error("Lỗi khi load relatives:", err);
    }
  };

  // 3️⃣ Tạo card
  const handleCreateCard = async () => {
    if (!user) return;
    try {
      const payload = {
        userId: user.id,
        useRelative: !!selectedRelative,
        relativeId: selectedRelative || null,
      };
      const res = await authApis().post(endpoints.createCard, payload);
      setCards(prev => [...prev, res.data]);
      setShowModal(false);
    } catch (err) {
      console.error("Lỗi khi tạo card:", err);
    }
  };

  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#c0dbed' }}>
      <Sidebar />
      <div className="content p-4" style={{ marginLeft: '250px', flex: 1 }}>
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h3>Danh sách thẻ ra vào</h3>
          <Button onClick={openCreateCardModal}>Tạo thẻ mới</Button>
        </div>

        <Row xs={1} sm={2} md={3} lg={4} className="g-4">
          {loading ? (
            <p>Đang tải...</p>
          ) : cards.length > 0 ? (
            cards.map((card) => (
              <Col key={card.id}>
                <Card 
                  style={{ 
                    borderRadius: '20px', 
                    overflow: 'hidden', 
                    boxShadow: '0 6px 15px rgba(0,0,0,0.2)',
                    border: 'none'
                  }}
                >
                  <Card.Img 
                    variant="top" 
                    src={img} 
                    style={{ height: '140px', objectFit: 'cover' }} 
                  />
                  <Card.Body className="text-center">
                    <Card.Title className="mt-2 fw-bold text-primary">
                      {card.relativeName ? card.relativeName : user.fullName}
                    </Card.Title>

                    {/* Hiển thị badge trạng thái */}
                    <div className="mb-2">
                      <span className="me-2">
                        <span 
                          className={`badge ${
                            card.status === "ACTIVE" ? "bg-success" : "bg-secondary"
                          }`}
                        >
                          {card.status}
                        </span>
                      </span>
                      <span>
                        <span className="badge bg-info text-dark">
                          {card.relationship || "Chủ hộ"}
                        </span>
                      </span>
                    </div>

                    <Card.Text style={{ fontSize: '0.95rem', color: '#333' }}>
                      <strong>Ngày hết hạn:</strong>{" "}
                      {new Date(card.expirationDate).toLocaleDateString()}
                    </Card.Text>
                  </Card.Body>
                </Card>
              </Col>
            ))
          ) : (
            <p>Chưa có thẻ nào</p>
          )}
        </Row>

        {/* Modal tạo card mới */}
        <Modal show={showModal} onHide={() => setShowModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Tạo thẻ mới</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form.Group>
              <Form.Label>Chọn người thân (nếu có)</Form.Label>
              <Form.Select
                value={selectedRelative || ""}
                onChange={(e) => setSelectedRelative(e.target.value || null)}
              >
                <option value="">Không chọn</option>
                {relatives.map((r) => (
                  <option key={r.id} value={r.id}>
                    {r.fullName} ({r.relationship})
                  </option>
                ))}
              </Form.Select>
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowModal(false)}>Hủy</Button>
            <Button variant="primary" onClick={handleCreateCard}>Tạo thẻ</Button>
          </Modal.Footer>
        </Modal>
      </div>
    </div>
  );
};

export default CardsPage;
