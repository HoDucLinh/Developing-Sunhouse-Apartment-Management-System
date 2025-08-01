import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Form, Button, Image, Modal } from 'react-bootstrap';
import Sidebar from '../components/Sidebar';
import Header from '../components/Header';
import { useUser } from '../contexts/UserContext';
import { FiUser, FiMail, FiPhone, FiHome, FiGrid, FiLock } from 'react-icons/fi';
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
  //Dọn dẹp preview URL khi unmount 
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
      ...prev, // giữ lại các giá trị cũ không bị ghi đè
      [name]: value,
    }));
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setFormData((prev) => ({
      ...prev,
      file,
    }));

    if (file) {
      const previewUrl = URL.createObjectURL(file);
      setPreviewAvatar(previewUrl);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault(); // ngắn reload trang
    try {
      const data = new FormData();
      data.append('fullName', formData.fullName);
      data.append('email', formData.email);
      data.append('phone', formData.phone);
      if (formData.file) data.append('file', formData.file);

      await authApis().put(endpoints.editProfile(user.id), data, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });

      // 🔁 Gọi lại API lấy thông tin mới
      const res = await authApis().get(endpoints.profile);
      setUser(res.data); // Cập nhật context => toàn app phản ánh đúng

      alert('Cập nhật thành công!');
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
    } catch (err) {
      alert('Đổi mật khẩu thất bại!');
    }
  };

  if (!user) return <p className="text-center mt-5">Đang tải thông tin người dùng...</p>;

  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#c0dbed' }}>
      <Sidebar />
      <Container fluid className="py-4 px-5" style={{ marginLeft: '220px' }}>
        <Header user={user} />

        <Row className="mb-4 text-center">
          <Col>
            <div style={{ position: 'relative', display: 'inline-block' }}>
              <Image
                src={previewAvatar || user.avatar}
                roundedCircle
                width={120}
                height={120}
                className="border border-3"
              />
              
              {/* Icon camera góc phải dưới */}
              <label
                htmlFor="avatarUpload"
                style={{
                  position: 'absolute',
                  bottom: 0,
                  right: 0,
                  backgroundColor: '#007bff',
                  borderRadius: '50%',
                  padding: '6px',
                  cursor: 'pointer',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                }}
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="16" height="16" fill="white"
                  className="bi bi-camera"
                  viewBox="0 0 16 16"
                >
                  <path d="M10.5 8a2.5 2.5 0 1 1-5 0 2.5 2.5 0 0 1 5 0z"/>
                  <path d="M5.318 1a1 1 0 0 0-.894.553L3.382 3H2a2 2 0 0 0-2 2v7a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V5a2 2 0 0 0-2-2h-1.382l-1.042-1.447A1 1 0 0 0 10.682 1H5.318zM2 4h1.618a1 1 0 0 0 .894-.553L5.382 2h5.236l.87 1.447a1 1 0 0 0 .894.553H14a1 1 0 0 1 1 1v7a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V5a1 1 0 0 1 1-1z"/>
                </svg>
              </label>

              {/* input file ẩn */}
              <input
                id="avatarUpload"
                type="file"
                accept="image/*"
                onChange={handleFileChange}
                style={{ display: 'none' }}
              />
            </div>
          </Col>
        </Row>


        <Form onSubmit={handleSubmit}>
          <Form.Group as={Row} className="mb-3" controlId="fullName">
            <Form.Label column sm={2}><FiUser className="me-2" />Họ và tên</Form.Label>
            <Col sm={10}>
              <Form.Control type="text" name="fullName" value={formData.fullName} onChange={handleInputChange} />
            </Col>
          </Form.Group>

          <Form.Group as={Row} className="mb-3" controlId="email">
            <Form.Label column sm={2}><FiMail className="me-2" />Email</Form.Label>
            <Col sm={10}>
              <Form.Control type="email" name="email" value={formData.email} onChange={handleInputChange} />
            </Col>
          </Form.Group>

          <Form.Group as={Row} className="mb-3" controlId="phone">
            <Form.Label column sm={2}><FiPhone className="me-2" />Số điện thoại</Form.Label>
            <Col sm={10}>
              <Form.Control type="text" name="phone" value={formData.phone} onChange={handleInputChange} />
            </Col>
          </Form.Group>

          <Form.Group as={Row} className="mb-3">
            <Form.Label column sm={2}><FiHome className="me-2" />Căn hộ</Form.Label>
            <Col sm={4}>
              <Form.Control type="text" value={user.room?.roomNumber || 'Chưa cập nhật'} readOnly />
            </Col>
            <Form.Label column sm={2}><FiGrid className="me-2" />Tầng</Form.Label>
            <Col sm={4}>
              <Form.Control type="text" value={user.room?.floorId || 'Chưa cập nhật'} readOnly />
            </Col>
          </Form.Group>

          <div className="text-center d-flex justify-content-center gap-3">
            <Button type="submit" variant="primary">Lưu thông tin</Button>
            <Button variant="warning" onClick={() => setShowPasswordModal(true)}>
              <FiLock className="me-1" /> Đổi mật khẩu
            </Button>
          </div>
        </Form>

        {/* Modal đổi mật khẩu */}
        <Modal show={showPasswordModal} onHide={() => setShowPasswordModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Đổi mật khẩu</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group className="mb-3">
                <Form.Label>Mật khẩu cũ</Form.Label>
                <Form.Control
                  type="password"
                  value={passwordData.oldPassword}
                  onChange={(e) => setPasswordData({ ...passwordData, oldPassword: e.target.value })}
                />
              </Form.Group>
              <Form.Group>
                <Form.Label>Mật khẩu mới</Form.Label>
                <Form.Control
                  type="password"
                  value={passwordData.newPassword}
                  onChange={(e) => setPasswordData({ ...passwordData, newPassword: e.target.value })}
                />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowPasswordModal(false)}>
              Hủy
            </Button>
            <Button variant="primary" onClick={handlePasswordChange}>
              Lưu
            </Button>
          </Modal.Footer>
        </Modal>
      </Container>
    </div>
  );
};

export default Profile;
