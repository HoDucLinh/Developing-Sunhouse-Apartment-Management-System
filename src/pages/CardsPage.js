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

  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#c0dbed' }}>
      <Sidebar />
      <div className="content p-4" style={{ marginLeft: '250px', flex: 1 }}>
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h3>Danh sách thẻ ra vào</h3>
          <Button onClick={() => alert("Vui lòng liên hệ ban quản trị để được tạo thẻ mới !!!")}>Tạo thẻ mới</Button>
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
                    <Card.Title className="mt-2 fw-bold text-primary">
                      {card.cardId}
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
                          {card.relationship || "Owner"}
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
      </div>
    </div>
  );
};

export default CardsPage;
