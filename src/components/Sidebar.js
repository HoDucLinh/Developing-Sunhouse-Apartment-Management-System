import React from 'react';
import { Nav } from 'react-bootstrap';
import { Link,useNavigate } from 'react-router-dom';
import { FiHome, FiTool, FiMessageSquare, FiLogOut, FiPackage, FiUser, FiUsers, FiFileText  } from 'react-icons/fi';
import { FaPoll } from 'react-icons/fa';
import '../styles/sidebar.css';
import { useUser } from '../contexts/UserContext';


const Sidebar = () => {
  const { logout } = useUser();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();              // Xóa token + user
    navigate('/login');    // Chuyển về trang đăng nhập
  };

  return(
  <div className="sidebar d-flex flex-column text-white p-3">
    <div className="text-center mb-4">
      <i className="bi bi-building fs-3"></i>
    </div>
    <Nav className="flex-column gap-2">
      <Nav.Link as={Link} to="/dashboard" className="text-white d-flex align-items-center gap-2">
        <FiHome /> <span>Trang chủ</span>
      </Nav.Link>
      <Nav.Link as={Link} to="/utilities" className="text-white d-flex align-items-center gap-2">
        <FiTool /> <span>Tiện ích</span>
      </Nav.Link>
      <Nav.Link as={Link} to="/invoices" className="text-white d-flex align-items-center gap-2">
        <FiFileText /> <span>Hóa đơn</span>
      </Nav.Link>
      <Nav.Link as={Link} to="/lockers" className="text-white d-flex align-items-center gap-2">
        <FiPackage /> <span>Tủ đồ</span>
      </Nav.Link>
      <Nav.Link as={Link} to="/cards" className="text-white d-flex align-items-center gap-2">
        <FiPackage /> <span>Thẻ ra vào</span>
      </Nav.Link>
      <Nav.Link as={Link} to="/complaints" className="text-white d-flex align-items-center gap-2">
        <FiMessageSquare /> <span>Phản ánh</span>
      </Nav.Link>
      <Nav.Link as={Link} to="/surveys" className="text-white d-flex align-items-center gap-2">
        <FaPoll /> <span>Khảo sát</span>
      </Nav.Link>
      <Nav.Link as={Link} to="/profile" className="text-white d-flex align-items-center gap-2">
        <FiUser /> <span>Thông tin</span>
      </Nav.Link>
      <Nav.Link as={Link} to="/relative" className="text-white d-flex align-items-center gap-2">
        <FiUsers /> <span>Người thân</span>
      </Nav.Link>
      <Nav.Link as={Link} to="/chat" className="text-white d-flex align-items-center gap-2">
        <FiUsers /> <span>Chat Room</span>
      </Nav.Link>
      <div className="mt-auto pt-3 border-top">
        <Nav.Link
          as="button"
          className="text-white d-flex align-items-center gap-2 btn btn-link text-start"
          onClick={handleLogout}
        >
          <FiLogOut /> <span>Đăng xuất</span>
        </Nav.Link>
      </div>
    </Nav>
  </div>
  )
}

export default Sidebar;
