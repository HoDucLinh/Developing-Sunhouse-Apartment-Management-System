import React, { useCallback, useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import { Container, Row, Col, Card, Button, Spinner, Modal, Form, Badge, Alert } from 'react-bootstrap';
import { FiPlus, FiEdit, FiTrash2, FiClock } from 'react-icons/fi';
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
  const [searchKeyword, setSearchKeyword] = useState('');

  const loadFeedbacks = async () => {
    if (!user) return;

    setLoading(true);
    try {
      const params = new URLSearchParams();
      if (searchKeyword.trim() !== '') {
        params.append('kw', searchKeyword.trim());
      }

      const res = await authApis().get(`${endpoints.getfeedbacks(user.id)}?${params.toString()}`);
      setComplaints(res.data);
    } catch (err) {
      console.error('Lỗi khi tải phản ánh:', err);
    } finally {
      setLoading(false);
    }
  };

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

  useEffect(() => {
    if (user) loadFeedbacks();
  }, [user, searchKeyword]); // Tự động load khi searchKeyword thay đổi

  const handleCreate = () => {
    setShowModal(true);
  };

  const handleEdit = (complaint) => {
    setEditingComplaint(complaint);
    setEditedContent(complaint.content);
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

  // Hàm hiển thị trạng thái đẹp hơn
  const getStatusBadge = (status) => {
    switch (status) {
      case 'PENDING':
        return <Badge bg="warning" pill>⏳ Chờ xử lý</Badge>;
      case 'PROCESSING':
        return <Badge bg="info" pill>🔄 Đang xử lý</Badge>;
      case 'RESOLVED':
        return <Badge bg="success" pill>✅ Đã xử lý</Badge>;
      case 'REJECTED':
        return <Badge bg="danger" pill>❌ Bị từ chối</Badge>;
      default:
        return <Badge bg="secondary" pill>{status}</Badge>;
    }
  };

  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#f8f9fa' }}>
      <Sidebar />

      <Container fluid className="px-5 py-4" style={{ marginLeft: '250px' }}>
        
        {/* Header */}
        <div className="d-flex justify-content-between align-items-center mb-5">
          <div>
            <h1 className="display-6 fw-bold text-dark">Phản ánh & Khiếu nại</h1>
            <p className="text-muted mb-0">Gửi và quản lý các phản ánh của cư dân</p>
          </div>
          <Button variant="success" size="lg" onClick={handleCreate}>
            <FiPlus className="me-2" /> Tạo phản ánh mới
          </Button>
        </div>

        {/* Search */}
        <div className="mb-4" style={{ maxWidth: '420px' }}>
          <Form.Control
            type="text"
            placeholder="Tìm kiếm nội dung phản ánh..."
            value={searchKeyword}
            onChange={(e) => setSearchKeyword(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === 'Enter') loadFeedbacks();
            }}
            size="lg"
            className="shadow-sm"
          />
        </div>

        {/* Loading & Content */}
        {loading ? (
          <div className="text-center py-5">
            <Spinner animation="border" variant="success" size="lg" />
            <p className="mt-3 text-muted">Đang tải danh sách phản ánh...</p>
          </div>
        ) : complaints.length === 0 ? (
          <Alert variant="info" className="text-center py-5">
            Bạn chưa có phản ánh nào. Hãy tạo phản ánh mới bằng nút bên trên.
          </Alert>
        ) : (
          <Row className="g-4">
            {complaints.map((complaint) => (
              <Col key={complaint.id} xs={12} sm={6} md={6} lg={4}>
                <Card className="h-100 shadow border-0 rounded-4 overflow-hidden hover-card">
                  <Card.Body className="d-flex flex-column">
                    <div className="d-flex justify-content-between align-items-start mb-3">
                      <Card.Title className="fw-bold fs-5 mb-0">
                        Phản ánh #{complaint.id}
                      </Card.Title>
                      {getStatusBadge(complaint.status)}
                    </div>

                    <Card.Text className="flex-grow-1 text-muted mb-4" style={{ fontSize: '1.02rem' }}>
                      {complaint.content}
                    </Card.Text>

                    <div className="mt-auto">
                      <div className="d-flex align-items-center text-muted small mb-3">
                        <FiClock className="me-2" />
                        {moment(complaint.createdAt).format('HH:mm DD/MM/YYYY')}
                      </div>

                      <div className="d-flex gap-2">
                        <Button 
                          variant="outline-primary" 
                          size="sm" 
                          className="flex-grow-1"
                          onClick={() => handleEdit(complaint)}
                        >
                          <FiEdit className="me-1" /> Sửa
                        </Button>
                        <Button 
                          variant="outline-danger" 
                          size="sm" 
                          className="flex-grow-1"
                          onClick={() => handleDelete(complaint.id)}
                        >
                          <FiTrash2 className="me-1" /> Xóa
                        </Button>
                      </div>
                    </div>
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>
        )}

        {/* Modal Tạo phản ánh mới */}
        <Modal show={showModal} onHide={() => setShowModal(false)} centered size="lg">
          <Modal.Header closeButton>
            <Modal.Title>Tạo phản ánh mới</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group className="mb-3">
                <Form.Label className="fw-medium">Nội dung phản ánh</Form.Label>
                <Form.Control
                  as="textarea"
                  rows={5}
                  value={newContent}
                  onChange={(e) => setNewContent(e.target.value)}
                  placeholder="Mô tả chi tiết vấn đề bạn muốn phản ánh..."
                />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowModal(false)}>
              Hủy
            </Button>
            <Button 
              variant="success" 
              onClick={handleSubmit} 
              disabled={submitting || !newContent.trim()}
            >
              {submitting ? 'Đang gửi...' : 'Gửi phản ánh'}
            </Button>
          </Modal.Footer>
        </Modal>

        {/* Modal Chỉnh sửa phản ánh */}
        <Modal show={!!editingComplaint} onHide={() => setEditingComplaint(null)} centered size="lg">
          <Modal.Header closeButton>
            <Modal.Title>Chỉnh sửa phản ánh #{editingComplaint?.id}</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group className="mb-3">
                <Form.Label className="fw-medium">Nội dung phản ánh</Form.Label>
                <Form.Control
                  as="textarea"
                  rows={5}
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
            <Button 
              variant="primary" 
              onClick={handleUpdate}
              disabled={!editedContent.trim()}
            >
              Cập nhật
            </Button>
          </Modal.Footer>
        </Modal>
      </Container>
    </div>
  );
};

export default ComplaintsPage;