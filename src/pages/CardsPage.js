import React from 'react';
import Sidebar from '../components/Sidebar';
import { Container, Table, Button } from 'react-bootstrap';


const CardsPage = () => {
  const accessCardItems = [
    {
      name: 'Nguyễn Văn B',
      role: 'Anh trai',
      room: 'Biển số: 92 - NI S4S',
      status: 'Hiển thị lần cuối: 31/08/2025',
      action: ['Xem chi tiết', 'Gia hạn', 'Xóa'],
      buttonColors: ['btn-primary', 'btn-success', 'btn-danger'],
      image: 'https://www.vietnamworks.com/hrinsider/wp-content/uploads/2023/12/anh-den-ngau.jpeg', // Placeholder cho ảnh
    },
    {
      name: 'Lê Thị C',
      role: 'Mẹ',
      room: 'Biển số: 02 - NL04S',
      status: 'Đang chờ duyệt',
      action: ['Xem chi tiết'],
      buttonColors: ['btn-primary'],
      image: 'https://www.vietnamworks.com/hrinsider/wp-content/uploads/2023/12/anh-den-ngau-012.jpg', // Placeholder cho ảnh
    },
    {
      name: 'Trần Văn D',
      role: 'Bạn bè',
      room: 'Biển số: 92 - NI S5S',
      status: 'Đã kiểm',
      action: ['Xem chi tiết', 'Gia hạn', 'Xóa'],
      buttonColors: ['btn-primary', 'btn-success', 'btn-danger'],
      image: 'https://www.vietnamworks.com/hrinsider/wp-content/uploads/2023/12/anh-den-ngau-016.jpg', // Placeholder cho ảnh
    },
    {
      title: '', // Thẻ trống với icon
      status: '',
      action: '',
      buttonColors: '',
      isEmpty: true,
    },
  ];

  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#c0dbed' }}>
      <Sidebar />
      <div className="content p-4" style={{ marginLeft: '250px', flex: 1 }}>
        <div className="search-bar mb-4 d-flex align-items-center justify-content-between" style={{ gap: '10px' }}>
          <input
            type="text"
            className="form-control rounded-pill"
            placeholder="Tìm kiếm..."
            style={{ width: '250px' }}
          />
          <img
            src="https://www.vietnamworks.com/hrinsider/wp-content/uploads/2023/12/anh-den-ngau-012.jpg"
            alt="avatar"
            className="rounded-circle rounded-circle me-4"
            width={40}
            height={40}
          />
        </div>
        <div className="card-container d-flex flex-wrap gap-3">
          {accessCardItems.map((item, index) => (
            <div key={index} className="card" style={{ width: '300px', border: 'none', backgroundColor: '#fff', borderRadius: '10px', boxShadow: '0 2px 5px rgba(0,0,0,0.1)', padding: '10px' }}>
              {item.isEmpty ? (
                <div className="card-body text-center p-4 d-flex align-items-center justify-content-center" style={{ height: '200px' }}>
                  <span className="spinner-grow text-primary" style={{ width: '50px', height: '50px' }} role="status">
                    <span className="visually-hidden">Loading...</span>
                  </span>
                  <span className="position-absolute" style={{ fontSize: '24px', color: '#6a0dad' }}>+</span>
                </div>
              ) : (
                <div className="card-body text-center">
                  <img src={item.image} alt={item.name} className="rounded-circle mb-2" style={{ width: '50px', height: '50px' }} />
                  <h6 className="mb-1">{item.name}</h6>
                  <p className="text-muted mb-1" style={{ fontSize: '0.9rem' }}>Mối quan hệ: {item.role}</p>
                  <p className="text-muted mb-1" style={{ fontSize: '0.9rem' }}>{item.room}</p>
                  <p className="text-muted mb-2" style={{ fontSize: '0.9rem' }}>{item.status}</p>
                  <div className="d-flex justify-content-center gap-2">
                    {item.action.map((action, idx) => (
                      <button key={idx} className={`btn ${item.buttonColors[idx]} btn-sm`} style={{ padding: '2px 10px', fontSize: '0.8rem' }}>
                        {action}
                      </button>
                    ))}
                  </div>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};


export default CardsPage;