import React from 'react';
import Sidebar from '../components/Sidebar';
import { Container, Table, Button } from 'react-bootstrap';

const residents = [
  { room: 'P101', name: 'Nguyễn Văn A', phone: '0905400321', status: 'Cư dân' },
  { room: 'P102', name: 'Lê Thị C', phone: '0911234422', status: 'Chờ duyệt' },
  { room: 'P103', name: 'Trần Văn E', phone: '0984555711', status: 'Chờ duyệt' },
];

const ResidentsPage = () => (
  <div className="d-flex">
    <Sidebar />
    <Container fluid style={{ marginLeft: '240px' }} className="py-4">
      <h2 className="mb-4">Quản lý cư dân</h2>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Phòng</th>
            <th>Họ và tên</th>
            <th>SĐT</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {residents.map((r, i) => (
            <tr key={i}>
              <td>{r.room}</td>
              <td>{r.name}</td>
              <td>{r.phone}</td>
              <td>{r.status}</td>
              <td>
                <Button variant="info" size="sm" className="me-2">Xem</Button>
                {r.status === 'Chờ duyệt' ? (
                  <Button variant="success" size="sm">Duyệt</Button>
                ) : (
                  <Button variant="danger" size="sm">Khoá</Button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </Container>
  </div>
);

export default ResidentsPage;