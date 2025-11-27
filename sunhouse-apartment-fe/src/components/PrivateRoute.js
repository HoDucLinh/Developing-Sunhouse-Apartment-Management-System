import { Navigate, Outlet } from 'react-router-dom';
import { useUser } from '../contexts/UserContext';

const PrivateRoute = () => {
  const { user } = useUser(); // kiểm tra user có đăng nhập chưa

  return user ? <Outlet /> : <Navigate to="/login" />;
};

export default PrivateRoute;