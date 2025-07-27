import React from 'react';
import Sidebar from '../components/Sidebar';
import { Container, Form } from 'react-bootstrap';

const SurveysPage = () => {
  const complaintsItems = [
    {
      title: 'Phần mềm dịch vụ tiền',
      status: 'Ngày 03/07/2025',
      action: 'Chưa hoàn thành',
      buttonColor: 'btn-warning',
    },
    {
      title: 'Đăng ký tính năng vệ sinh',
      status: 'Ngày 01/07/2025',
      action: 'Đã hoàn thành',
      buttonColor: 'btn-success',
    },
  ];

  return (
    <div className="d-flex flex-column" style={{ minHeight: '100vh', backgroundColor: '#c0dbed' }}>
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
          {complaintsItems.map((item, index) => (
            <div key={index} className="card" style={{ width: '300px', border: 'none', backgroundColor: '#fff', borderRadius: '10px', boxShadow: '0 2px 5px rgba(0,0,0,0.1)' }}>
              <div className="card-body text-center p-3">
                <h6 className="mb-2">{item.title}</h6>
                <p className="text-muted mb-2" style={{ fontSize: '0.9rem' }}>{item.status}</p>
                <button className={`btn ${item.buttonColor} w-100`} style={{ padding: '5px', fontSize: '0.9rem' }}>{item.action}</button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default SurveysPage;