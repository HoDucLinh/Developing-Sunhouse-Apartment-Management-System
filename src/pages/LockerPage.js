
import React from 'react';
import Sidebar from '../components/Sidebar';
import '../styles/sidebar.css';

const LockerPage = () => {
  const items = [
    { id: 'DH001', time: '04:09 PM, 26/07/2025', weight: '2kg', img: 'https://hoseiki.vn/wp-content/uploads/2025/03/avatar-vo-tri-cute-24.jpg' },
    { id: 'DH002', time: '04:20 PM, 26/07/2025', weight: '0.5kg', img: 'https://hoseiki.vn/wp-content/uploads/2025/03/avatar-vo-tri-cute-26.jpg' },
    { id: 'DH007', time: '07:39 PM, 26/07/2025', weight: '2kg', img: 'https://hoseiki.vn/wp-content/uploads/2025/03/avatar-vo-tri-cute-23.jpg' },
    { id: 'DH010', time: '07:27 AM, 27/07/2025', weight: '1kg', img: 'https://hoseiki.vn/wp-content/uploads/2025/03/avatar-vo-tri-cute-22.jpg' },
  ];

  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#c0dbed' }}>
      <Sidebar />
      <div className="content p-4" style={{ marginLeft: '260px', width: 'calc(100% - 260px)' }}>
        <div className="search-bar mb-4">
          <input type="text" className="form-control" placeholder="Tìm kiếm..." />
        </div>
        <div className="card-container d-flex flex-wrap gap-3">
          {items.map((item) => (
            <div key={item.id} className="card" style={{ width: '200px' }}>
              <img src={item.img} className="card-img-top" alt={item.id} style={{ borderRadius: '10px 10px 0 0', height: '150px', objectFit: 'cover' }} />
              <div className="card-body text-center">
                <h6>{item.id}</h6>
                <p>Thời gian: {item.time}<br />Trọng lượng: {item.weight}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default LockerPage;