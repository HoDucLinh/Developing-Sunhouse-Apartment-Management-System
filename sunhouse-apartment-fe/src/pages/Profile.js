import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Form, Button, Image, Modal, Card, Alert } from 'react-bootstrap';
import Sidebar from '../components/Sidebar';
import Header from '../components/Header';
import { useUser } from '../contexts/UserContext';
import { FiUser, FiMail, FiPhone, FiHome, FiGrid, FiLock, FiCamera } from 'react-icons/fi';
import { authApis, endpoints } from '../configs/Apis';

const Profile = () => {
  const { user, setUser } = useUser();
  const [previewAvatar, setPreviewAvatar] = useState(null);
  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    phone: '',
    file: null,
  });
  const [showPasswordModal, setShowPasswordModal] = useState(false);
  const [passwordData, setPasswordData] = useState({
    oldPassword: '',
    newPassword: '',
  });

  useEffect(() => {
    if (user) {
      setFormData({
        fullName: user.fullName || '',
        email: user.email || '',
        phone: user.phone || '',
        file: null,
      });
    }
  }, [user]);

  // Dọn dẹp preview URL khi component unmount
  useEffect(() => {
    return () => {
      if (previewAvatar) {
        URL.revokeObjectURL(previewAvatar);
      }
    };
  }, [previewAvatar]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setFormData((prev) => ({
        ...prev,
        file,
      }));
      const previewUrl = URL.createObjectURL(file);
      setPreviewAvatar(previewUrl);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const data = new FormData();
      data.append('fullName', formData.fullName);
      data.append('email', formData.email);
      data.append('phone', formData.phone);
      if (formData.file) data.append('file', formData.file);

      await authApis().put(endpoints.editProfile(user.id), data, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });

      const res = await authApis().get(endpoints.profile);
      setUser(res.data);

      alert('Cập nhật thông tin thành công!');
    } catch (error) {
      console.error("Lỗi cập nhật:", error);
      alert('Có lỗi xảy ra khi cập nhật hồ sơ.');
    }
  };

  const handlePasswordChange = async () => {
    try {
      await authApis().put(endpoints.changePassword(user.id), null, {
        params: {
          oldPassword: passwordData.oldPassword,
          newPassword: passwordData.newPassword,
        },
      });
      alert('Đổi mật khẩu thành công!');
      setShowPasswordModal(false);
      setPasswordData({ oldPassword: '', newPassword: '' });
    } catch (err) {
      alert('Đổi mật khẩu thất bại! Vui lòng kiểm tra lại mật khẩu cũ.');
    }
  };

  if (!user) return <p className="text-center mt-5">Đang tải thông tin người dùng...</p>;

  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#f8f9fa' }}>
      <Sidebar />
      <Container fluid className="px-5 py-4" style={{ marginLeft: '220px' }}>
        <Header user={user} />

        <Card className="shadow border-0 rounded-4 mt-4">
          <Card.Body className="p-5">
            
            {/* Avatar Section */}
            <div className="text-center mb-5">
              <div style={{ position: 'relative', display: 'inline-block' }}>
                <Image
                  src={previewAvatar || user.avatar || '/default-avatar.png'}
                  roundedCircle
                  width={150}
                  height={150}
                  className="border border-4 border-white shadow-sm"
                  style={{ objectFit: 'cover' }}
                />
                
                {/* Nút chỉnh sửa avatar */}
                <label
                  htmlFor="avatarUpload"
                  style={{
                    position: 'absolute',
                    bottom: '8px',
                    right: '8px',
                    backgroundColor: '#0d6efd',
                    borderRadius: '50%',
                    padding: '10px',
                    cursor: 'pointer',
                    boxShadow: '0 4px 8px rgba(0,0,0,0.2)',
                    transition: 'all 0.2s ease',
                  }}
                  className="hover-scale"
                >
                  <FiCamera size={20} color="white" />
                </label>

                <input
                  id="avatarUpload"
                  type="file"
                  accept="image/*"
                  onChange={handleFileChange}
                  style={{ display: 'none' }}
                />
              </div>
              <h4 className="mt-3 fw-bold">{user.fullName}</h4>
              <p className="text-muted">Cập nhật thông tin cá nhân của bạn</p>
            </div>

            <Form onSubmit={handleSubmit}>
              <Row className="g-4">
                {/* Họ và tên */}
                <Col md={6}>
                  <Form.Group>
                    <Form.Label className="fw-medium">
                      <FiUser className="me-2" /> Họ và tên
                    </Form.Label>
                    <Form.Control
                      type="text"
                      name="fullName"
                      value={formData.fullName}
                      onChange={handleInputChange}
                      className="rounded-3"
                      size="lg"
                    />
                  </Form.Group>
                </Col>

                {/* Email */}
                <Col md={6}>
                  <Form.Group>
                    <Form.Label className="fw-medium">
                      <FiMail className="me-2" /> Email
                    </Form.Label>
                    <Form.Control
                      type="email"
                      name="email"
                      value={formData.email}
                      onChange={handleInputChange}
                      className="rounded-3"
                      size="lg"
                    />
                  </Form.Group>
                </Col>

                {/* Số điện thoại */}
                <Col md={6}>
                  <Form.Group>
                    <Form.Label className="fw-medium">
                      <FiPhone className="me-2" /> Số điện thoại
                    </Form.Label>
                    <Form.Control
                      type="text"
                      name="phone"
                      value={formData.phone}
                      onChange={handleInputChange}
                      className="rounded-3"
                      size="lg"
                    />
                  </Form.Group>
                </Col>

                {/* Thông tin căn hộ */}
                <Col md={12}>
                  <Card className="bg-light border-0">
                    <Card.Body>
                      <h6 className="fw-semibold mb-3 text-success">
                        <FiHome className="me-2" /> Thông tin căn hộ
                      </h6>
                      <Row>
                        <Col md={6}>
                          <Form.Group className="mb-3">
                            <Form.Label>Số căn hộ</Form.Label>
                            <Form.Control 
                              type="text" 
                              value={user.room?.roomNumber || 'Chưa cập nhật'} 
                              readOnly 
                              className="bg-white"
                            />
                          </Form.Group>
                        </Col>
                        <Col md={6}>
                          <Form.Group className="mb-3">
                            <Form.Label>Tầng</Form.Label>
                            <Form.Control 
                              type="text" 
                              value={user.room?.floorId || 'Chưa cập nhật'} 
                              readOnly 
                              className="bg-white"
                            />
                          </Form.Group>
                        </Col>
                      </Row>
                    </Card.Body>
                  </Card>
                </Col>
              </Row>

              {/* Buttons */}
              <div className="d-flex justify-content-center gap-3 mt-5">
                <Button 
                  type="submit" 
                  variant="success" 
                  size="lg" 
                  className="px-5 py-3 rounded-3 fw-medium"
                >
                  Lưu thay đổi
                </Button>
                
                <Button 
                  variant="outline-primary" 
                  size="lg" 
                  className="px-5 py-3 rounded-3 fw-medium"
                  onClick={() => setShowPasswordModal(true)}
                >
                  <FiLock className="me-2" /> Đổi mật khẩu
                </Button>
              </div>
            </Form>
          </Card.Body>
        </Card>

        {/* Modal Đổi mật khẩu */}
        <Modal 
          show={showPasswordModal} 
          onHide={() => setShowPasswordModal(false)} 
          centered
        >
          <Modal.Header closeButton>
            <Modal.Title>Đổi mật khẩu</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group className="mb-4">
                <Form.Label className="fw-medium">Mật khẩu cũ</Form.Label>
                <Form.Control
                  type="password"
                  value={passwordData.oldPassword}
                  onChange={(e) => setPasswordData({ 
                    ...passwordData, 
                    oldPassword: e.target.value 
                  })}
                  placeholder="Nhập mật khẩu hiện tại"
                  className="rounded-3"
                />
              </Form.Group>

              <Form.Group>
                <Form.Label className="fw-medium">Mật khẩu mới</Form.Label>
                <Form.Control
                  type="password"
                  value={passwordData.newPassword}
                  onChange={(e) => setPasswordData({ 
                    ...passwordData, 
                    newPassword: e.target.value 
                  })}
                  placeholder="Nhập mật khẩu mới"
                  className="rounded-3"
                />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button 
              variant="secondary" 
              onClick={() => setShowPasswordModal(false)}
            >
              Hủy
            </Button>
            <Button 
              variant="primary" 
              onClick={handlePasswordChange}
              disabled={!passwordData.oldPassword || !passwordData.newPassword}
            >
              Xác nhận đổi mật khẩu
            </Button>
          </Modal.Footer>
        </Modal>
      </Container>
    </div>
  );
};

export default Profile;