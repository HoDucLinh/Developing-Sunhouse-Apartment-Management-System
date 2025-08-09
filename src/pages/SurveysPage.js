import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import { authApis, endpoints } from '../configs/Apis'; // Import từ file bạn đã khai báo
import cookie from 'react-cookies';

const SurveysPage = () => {
  const [surveys, setSurveys] = useState([]);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState("");

  useEffect(() => {
    const loadSurveys = async () => {
      try {
        let token = cookie.load("token"); // Lấy token từ cookie nếu cần
        let res = await authApis(token).get(endpoints.getSurveys, {
          params: { title: keyword } // Nếu muốn tìm kiếm theo title
        });
        setSurveys(res.data);
      } catch (err) {
        console.error("Lỗi khi tải surveys:", err);
      } finally {
        setLoading(false);
      }
    };

    loadSurveys();
  }, [keyword]); // keyword thay đổi thì gọi lại API

  return (
    <div className="d-flex flex-column" style={{ minHeight: '100vh', backgroundColor: '#c0dbed' }}>
      <Sidebar />
      <div className="content p-4" style={{ marginLeft: '250px', flex: 1 }}>
        {/* Search */}
        <div className="search-bar mb-4 d-flex align-items-center justify-content-between" style={{ gap: '10px' }}>
          <input
            type="text"
            className="form-control rounded-pill"
            placeholder="Tìm kiếm..."
            style={{ width: '250px' }}
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
        </div>

        {/* Nội dung */}
        {loading ? (
          <p>Đang tải khảo sát...</p>
        ) : (
          <div className="card-container d-flex flex-wrap gap-3">
            {surveys.length > 0 ? (
              surveys.map((item, index) => (
                <div
                  key={index}
                  className="card"
                  style={{
                    width: '300px',
                    border: 'none',
                    backgroundColor: '#fff',
                    borderRadius: '10px',
                    boxShadow: '0 2px 5px rgba(0,0,0,0.1)'
                  }}
                >
                  <div className="card-body text-center p-3">
                    <h6 className="mb-2">{item.title}</h6>
                    <p className="text-muted mb-2" style={{ fontSize: '0.9rem' }}>
                      {new Date(item.createdAt).toLocaleDateString('vi-VN')}
                    </p>
                    <button
                      className="btn btn-primary w-100"
                      style={{ padding: '5px', fontSize: '0.9rem' }}
                    >
                      {item.type === 'MULTIPLE_CHOICE' ? 'Khảo sát trắc nghiệm' : 'Khảo sát tự luận'}
                    </button>
                  </div>
                </div>
              ))
            ) : (
              <p>Không tìm thấy khảo sát nào.</p>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default SurveysPage;
