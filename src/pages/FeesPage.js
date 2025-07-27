import React from 'react';
import Sidebar from '../components/Sidebar';


const FeesPage = () => {
  const feesItems = [
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
    <div className="d-flex">
      <Sidebar />
      <div className="content p-4" style={{ marginLeft: '260px', width: 'calc(100% - 260px)' }}>
        <div className="search-bar mb-4">
          <input type="text" className="form-control" placeholder="Tìm kiếm..." />
        </div>
        <div className="card-container d-flex flex-wrap gap-3">
          {feesItems.map((item, index) => (
            <div key={index} className="card" style={{ width: '300px' }}>
              <div className="card-body text-center">
                <h6>{item.title}</h6>
                <p className="text-muted">{item.status}</p>
                <button className={`btn ${item.buttonColor} w-100`}>{item.action}</button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default FeesPage;