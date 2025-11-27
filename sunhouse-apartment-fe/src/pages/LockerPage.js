import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import '../styles/sidebar.css';
import { authApis, endpoints } from '../configs/Apis';
import { useUser } from '../contexts/UserContext';

const LockerPage = () => {
  const { user } = useUser();
  const [packages, setPackages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [kw, setKw] = useState('');
  const [debouncedKw, setDebouncedKw] = useState(''); // giá trị debounce

  // Debounce: Cập nhật debouncedKw sau 3s người dùng ngừng gõ
  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedKw(kw);
    }, 2000); // 3 giây

    return () => {
      clearTimeout(handler); // xóa timeout khi kw thay đổi trước khi đủ 3s
    };
  }, [kw]);

  useEffect(() => {
    const loadPackages = async () => {
      if (!user?.id) return;

      setLoading(true);
      try {
        const res = await authApis().get(endpoints.getPackages(user.id), {
          params: { kw: debouncedKw }
        });
        if (res.data.success) {
          setPackages(res.data.data);
        } else {
          console.error('Lỗi từ server:', res.data.error);
        }
      } catch (err) {
        console.error('Lỗi khi gọi API packages:', err);
      } finally {
        setLoading(false);
      }
    };

    loadPackages();
  }, [user, debouncedKw]); // chỉ gọi API khi debouncedKw thay đổi

  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#c0dbed' }}>
      <Sidebar />
      <div className="content p-4" style={{ marginLeft: '260px', width: 'calc(100% - 260px)' }}>
        <div className="search-bar mb-4 d-flex gap-2">
          <input
            type="text"
            className="form-control"
            placeholder="Tìm kiếm..."
            value={kw}
            onChange={(e) => setKw(e.target.value)}
          />
        </div>

        {loading ? (
          <p>Đang tải dữ liệu...</p>
        ) : packages.length === 0 ? (
          <p>Không có package nào.</p>
        ) : (
          <div className="card-container d-flex flex-wrap gap-3">
            {packages.map((pkg) => (
              <div key={pkg.id} className="card" style={{ width: '200px' }}>
                <img
                  src={pkg.image}
                  className="card-img-top"
                  alt={pkg.name}
                  style={{ borderRadius: '10px 10px 0 0', height: '150px', objectFit: 'cover' }}
                />
                <div className="card-body text-center">
                  <h6>{pkg.name}</h6>
                  <p>
                    Trạng thái: {pkg.status}
                    <br />
                      Ngày hết hạn:{" "}
                      {pkg.dueDate
                        ? new Date(pkg.dueDate).toLocaleDateString("vi-VN", {
                            day: "2-digit",
                            month: "2-digit",
                            year: "numeric"
                          })
                        : "Không có"}
                    <br />
                  </p>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default LockerPage;
