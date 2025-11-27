import { createContext, useContext, useEffect, useState } from 'react';
import { authApis, endpoints } from '../configs/Apis';

const UserContext = createContext();

export const UserProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  // Fetch user profile
  useEffect(() => {
    const fetchProfile = async () => {
      const token = localStorage.getItem('token') || sessionStorage.getItem('token');
      if (!token) return;
      
      try {
        const res = await authApis().get(endpoints.profile);
        setUser(res.data);
      } catch (err) {
        console.error('Lỗi khi lấy thông tin người dùng:', err);
      }
    };

    fetchProfile();
  }, []);
  // Hàm đăng xuất
  const logout = () => {
    setUser(null);
    localStorage.removeItem('token'); // nếu bạn đang lưu token ở localStorage
    sessionStorage.removeItem('token'); // nếu lưu ở sessionStorage
  };

  return (
    <UserContext.Provider value={{ user, setUser, logout }}>
      {children}
    </UserContext.Provider>
  );
};

// Custom hook để dùng context
export const useUser = () => useContext(UserContext);
