import { NavLink, useNavigate } from "react-router-dom";
import { FiHome, FiTool, FiMessageSquare, FiLogOut, FiPackage, FiUser, FiUsers, FiFileText } from "react-icons/fi";
import { FaPoll } from "react-icons/fa";
import "../styles/sidebar.css";
import { useUser } from "../contexts/UserContext";

const Sidebar = () => {
  const { logout } = useUser();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <div className="sidebar d-flex flex-column p-3">
      <div className="text-center mb-3">
        <FiHome size={30} color="#0d6efd" />
        <div className="fw-bold mt-2">Chung cư</div>
      </div>
      <NavLink to="/dashboard" className="nav-link d-flex align-items-center gap-2">
        <FiHome /> <span>Trang chủ</span>
      </NavLink>
      <NavLink to="/utilities" className="nav-link d-flex align-items-center gap-2">
        <FiTool /> <span>Tiện ích</span>
      </NavLink>
      <NavLink to="/invoices" className="nav-link d-flex align-items-center gap-2">
        <FiFileText /> <span>Hóa đơn</span>
      </NavLink>
      <NavLink to="/lockers" className="nav-link d-flex align-items-center gap-2">
        <FiPackage /> <span>Tủ đồ</span>
      </NavLink>
      <NavLink to="/cards" className="nav-link d-flex align-items-center gap-2">
        <FiPackage /> <span>Thẻ ra vào</span>
      </NavLink>
      <NavLink to="/complaints" className="nav-link d-flex align-items-center gap-2">
        <FiMessageSquare /> <span>Phản ánh</span>
      </NavLink>
      <NavLink to="/surveys" className="nav-link d-flex align-items-center gap-2">
        <FaPoll /> <span>Khảo sát</span>
      </NavLink>
      <NavLink to="/profile" className="nav-link d-flex align-items-center gap-2">
        <FiUser /> <span>Thông tin</span>
      </NavLink>
      <NavLink to="/relative" className="nav-link d-flex align-items-center gap-2">
        <FiUsers /> <span>Người thân</span>
      </NavLink>
      <NavLink to="/chat" className="nav-link d-flex align-items-center gap-2">
        <FiUsers /> <span>Chat Room</span>
      </NavLink>

      <div className="mt-auto pt-3 border-top">
        <button
          onClick={handleLogout}
          className="w-100 d-flex align-items-center gap-2 btn btn-link text-start text-black"
        >
          <FiLogOut /> <span>Đăng xuất</span>
        </button>
      </div>
    </div>
  );
};

export default Sidebar;
