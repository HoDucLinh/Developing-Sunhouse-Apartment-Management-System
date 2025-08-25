import React, { useEffect, useState, useRef } from "react";
import { Container, Row, Col, Form, Button, Card, InputGroup } from "react-bootstrap";
import { db } from "../configs/firebaseConfig"; 
import { ref, push, onValue } from "firebase/database";
import Sidebar from "../components/Sidebar";
import Header from "../components/Header";
import { useUser } from "../contexts/UserContext";
import { FiSend } from "react-icons/fi";

const ChatRoom = ({ roomId = "room1" }) => {
  const { user } = useUser();
  const [messages, setMessages] = useState([]);
  const [newMsg, setNewMsg] = useState("");
  const messagesEndRef = useRef(null);

  // Lắng nghe tin nhắn trong room
  useEffect(() => {
    const messagesRef = ref(db, `chatrooms/${roomId}/messages`);
    onValue(messagesRef, (snapshot) => {
      const data = snapshot.val();
      if (data) {
        const loaded = Object.entries(data).map(([id, msg]) => ({
          id,
          ...msg,
        }));
        // sắp xếp theo thời gian
        loaded.sort((a, b) => a.timestamp - b.timestamp);
        setMessages(loaded);
      } else {
        setMessages([]);
      }
    });
  }, [roomId]);

  // Tự động scroll xuống cuối khi có tin nhắn mới
  useEffect(() => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [messages]);

    const sendMessage = () => {
        if (!newMsg.trim()) return;

        const messagesRef = ref(db, `chatrooms/${roomId}/messages`);
        push(messagesRef, {
            senderId: user?.id,
            sender: user?.fullName || "Người dùng",
            avatar: user?.avatar || "https://via.placeholder.com/40",
            text: newMsg,
            timestamp: Date.now(),
        });

        setNewMsg("");
    };

  return (
    <div className="d-flex" style={{ minHeight: "100vh", backgroundColor: "#eaf2f8" }}>
      <Sidebar />
      <Container fluid className="py-4 px-5" style={{ marginLeft: "220px" }}>
        <Header user={user} />

        <Row className="mt-4">
          <Col>
            <Card className="shadow-lg" style={{ borderRadius: "16px" }}>
              <Card.Header className="bg-primary text-white fw-bold">
                💬 Chat Room ({roomId})
              </Card.Header>
              <Card.Body
                style={{
                  height: "65vh",
                  overflowY: "auto",
                  backgroundColor: "#fdfdfd",
                }}
              >
                {messages.length === 0 && (
                  <p className="text-center text-muted">Chưa có tin nhắn nào...</p>
                )}
                {messages.map((msg) => (
                  <div
                    key={msg.id}
                    className={`d-flex mb-3 ${
                      msg.sender === user?.fullName ? "justify-content-end" : "justify-content-start"
                    }`}
                  >
                    {msg.sender !== user?.fullName && (
                      <img
                        src={msg.avatar}
                        alt="avatar"
                        className="rounded-circle me-2"
                        style={{ width: "40px", height: "40px" }}
                      />
                    )}
                    <div
                      style={{
                        maxWidth: "60%",
                        backgroundColor: msg.sender === user?.fullName ? "#007bff" : "#f1f1f1",
                        color: msg.sender === user?.fullName ? "white" : "black",
                        padding: "10px 14px",
                        borderRadius: "12px",
                      }}
                    >
                      <div className="fw-bold mb-1" style={{ fontSize: "0.85rem" }}>
                        {msg.sender}
                      </div>
                      <div>{msg.text}</div>
                    </div>
                    {msg.sender === user?.fullName && (
                      <img
                        src={msg.avatar}
                        alt="avatar"
                        className="rounded-circle ms-2"
                        style={{ width: "40px", height: "40px" }}
                      />
                    )}
                  </div>
                ))}
                <div ref={messagesEndRef} />
              </Card.Body>
              <Card.Footer>
                <InputGroup>
                  <Form.Control
                    type="text"
                    placeholder="Nhập tin nhắn..."
                    value={newMsg}
                    onChange={(e) => setNewMsg(e.target.value)}
                    onKeyDown={(e) => e.key === "Enter" && sendMessage()}
                  />
                  <Button variant="primary" onClick={sendMessage}>
                    <FiSend />
                  </Button>
                </InputGroup>
              </Card.Footer>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default ChatRoom;
