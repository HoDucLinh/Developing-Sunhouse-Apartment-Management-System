import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import { Container, Row, Col, Card, Button, Spinner, Modal, Form } from 'react-bootstrap';
import { FiPlus, FiEdit, FiTrash2 } from 'react-icons/fi';
import { useUser } from '../contexts/UserContext';
import { authApis, endpoints } from '../configs/Apis';
import moment from 'moment';

const ComplaintsPage = () => {
  const { user } = useUser();
  const [complaints, setComplaints] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [newContent, setNewContent] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [editingComplaint, setEditingComplaint] = useState(null);
  const [editedContent, setEditedContent] = useState('');

  const handleSubmit = async () => {
    if (!newContent.trim()) return;

    setSubmitting(true);
    try {
      const res = await authApis().post(
        endpoints.createFeedback,
        {
          userId: user.id,
          content: newContent,
        },
        {
          headers: {
            'Content-Type': 'application/json'
          }
        }
      );
      // Reload phản ánh sau khi tạo thành công
      setComplaints([...complaints, res.data]);
      setShowModal(false);
      setNewContent('');
    } catch (err) {
      console.error('Lỗi khi tạo phản ánh:', err);
    } finally {
      setSubmitting(false);
    }
  };

  const handleUpdate = async () => {
    if (!editedContent.trim() || !editingComplaint) return;

    try {
      const res = await authApis().put(
        endpoints.updateFeedback(editingComplaint.id),
        { content: editedContent }
      );

      // Cập nhật lại danh sách
      setComplaints((prev) =>
        prev.map((c) =>
          c.id === editingComplaint.id ? res.data : c
        )
      );

      setEditingComplaint(null);
      setEditedContent('');
    } catch (err) {
      console.error('Lỗi khi cập nhật phản ánh:', err);
    }
  };

  // Lấy danh sách phản ánh từ API
  useEffect(() => {
    const loadFeedbacks = async () => {
      if (!user) return;

      try {
        const res = await authApis().get(endpoints.getfeedbacks(user.id));
        setComplaints(res.data);
      } catch (err) {
        console.error('Lỗi khi tải phản ánh:', err);
      } finally {
        setLoading(false);
      }
    };

    loadFeedbacks();
  }, [user]);

  const handleCreate = () => {
    console.log("Tạo phản ánh mới");
    setShowModal(true);
  };

  const handleEdit = (id) => {
    const complaint = complaints.find((c) => c.id === id);
    if (complaint) {
      setEditingComplaint(complaint);
      setEditedContent(complaint.content);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("Bạn có chắc chắn muốn xóa phản ánh này không?")) {
      try {
        await authApis().delete(endpoints.deleteFeedback(id));
        setComplaints((prev) => prev.filter((c) => c.id !== id));
      } catch (err) {
        console.error("Lỗi khi xóa phản ánh:", err);
      }
    }
  };

  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#e5f1fb' }}>
      <Sidebar />
      <Container fluid className="py-4 px-5" style={{ marginLeft: '250px' }}>
        {/* Header */}
        <Row className="align-items-center justify-content-between mb-4">
          <Col xs="auto">
            <input
              type="text"
              className="form-control"
              placeholder="Tìm kiếm khiếu nại..."
              style={{ width: '250px' }}
            />
          </Col>
        </Row>

        {/* Complaints list */}
        {loading ? (
          <div className="d-flex justify-content-center align-items-center" style={{ height: '50vh' }}>
            <Spinner animation="border" />
          </div>
        ) : (
          <Row className="g-4">
            {complaints.length === 0 ? (
              <p>Không có phản ánh nào.</p>
            ) : (
              complaints.map((complaint) => (
                <Col key={complaint.id} xs={12} sm={6} md={4}>
                  <Card className="border-0 shadow-sm rounded-4 p-3">
                    <Card.Body>
                      <Card.Title className="fs-6 fw-bold">Phản ánh #{complaint.id}</Card.Title>
                      <Card.Text className="mb-1">{complaint.content}</Card.Text>
                      <Card.Text className="mb-1 text-muted">
                        Ngày gửi: {moment(complaint.createdAt).format('HH:mm DD/MM/YYYY')}
                      </Card.Text>
                      <Card.Text className="mb-2">
                        Trạng thái: <strong>{complaint.status === 'PENDING' ? 'Chờ xử lý' : complaint.status}</strong>
                      </Card.Text>

                      <div className="d-flex gap-2">
                        <Button variant="primary" size="sm">
                          Xem chi tiết
                        </Button>
                        <Button variant="warning" size="sm" onClick={() => handleEdit(complaint.id)}>
                          <FiEdit /> Sửa
                        </Button>
                        <Button variant="danger" size="sm" onClick={() => handleDelete(complaint.id)}>
                          <FiTrash2 /> Xóa
                        </Button>
                      </div>
                    </Card.Body>
                  </Card>
                </Col>
              ))
            )}
          </Row>
        )}

        {/* Floating + button */}
        <Button
          variant="primary"
          className="rounded-circle position-fixed"
          style={{
            bottom: '30px',
            right: '30px',
            width: '60px',
            height: '60px',
            fontSize: '24px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            boxShadow: '0 4px 8px rgba(0,0,0,0.2)',
          }}
          onClick={handleCreate}
        >
          <FiPlus />
        </Button>
      </Container>
      <Modal show={showModal} onHide={() => setShowModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>Tạo phản ánh mới</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group>
              <Form.Label>Nội dung phản ánh</Form.Label>
              <Form.Control
                as="textarea"
                rows={4}
                value={newContent}
                onChange={(e) => setNewContent(e.target.value)}
                placeholder="Nhập nội dung..."
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Hủy
          </Button>
          <Button variant="primary" onClick={handleSubmit} disabled={submitting}>
            {submitting ? 'Đang gửi...' : 'Gửi phản ánh'}
          </Button>
        </Modal.Footer>
      </Modal>
      <Modal show={!!editingComplaint} onHide={() => setEditingComplaint(null)} centered>
        <Modal.Header closeButton>
          <Modal.Title>Chỉnh sửa phản ánh</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group>
              <Form.Label>Nội dung phản ánh</Form.Label>
              <Form.Control
                as="textarea"
                rows={4}
                value={editedContent}
                onChange={(e) => setEditedContent(e.target.value)}
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setEditingComplaint(null)}>
            Hủy
          </Button>
          <Button variant="primary" onClick={handleUpdate}>
            Cập nhật
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default ComplaintsPage;
