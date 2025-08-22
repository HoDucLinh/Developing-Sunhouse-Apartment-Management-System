import { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import { Container, Table, Badge, Button, Spinner, Alert, Form, Modal } from 'react-bootstrap';
import { authApis, endpoints } from '../configs/Apis';
import cookie from 'react-cookies';
import { useUser } from '../contexts/UserContext';
import { pdf } from "@react-pdf/renderer";
import InvoicePDF from '../components/InvoicePDF';


const Invoice = () => {
    const { user } = useUser(); 
    const [invoices, setInvoices] = useState([]);
    const [loadingInvoices, setLoadingInvoices] = useState(true);
    const [errInvoices, setErrInvoices] = useState(null);

    const [showPDF, setShowPDF] = useState(false);
    const [selectedInvoice, setSelectedInvoice] = useState(null);
    const [pdfUrl, setPdfUrl] = useState(null);

    const [uploading, setUploading] = useState(false);

    const statusBadge = (status) => {
        switch (status) {
        case 'Đã thanh toán':
            return <Badge bg="success">✔ {status}</Badge>;
        case 'Chưa thanh toán':
            return <Badge bg="danger">✘ {status}</Badge>;
        case 'Chờ duyệt':
            return <Badge bg="warning">⌛ {status}</Badge>;
        default:
            return <Badge bg="primary">{status}</Badge>;
        }
    };

    const handleShowPDF = async (invoice) => {
        setSelectedInvoice(invoice);
        setShowPDF(true);

        const blob = await pdf(<InvoicePDF invoice={invoice} user={user} />).toBlob();
        const url = URL.createObjectURL(blob);
        setPdfUrl(url);
    };

    const handleUploadProof = async (invoiceId, file) => {
        if (!file) return;

        try {
        setUploading(true);
        let token = cookie.load("token");

        let formData = new FormData();
        formData.append("invoiceId", invoiceId);
        formData.append("file", file);

        await authApis(token).put(endpoints.uploadProof, formData, {
            headers: { "Content-Type": "multipart/form-data" },
        });

        alert("Tải minh chứng thành công!");
        loadInvoices();
        } catch (err) {
        console.error("Lỗi upload proof:", err);
        alert("Upload proof thất bại!");
        } finally {
        setUploading(false);
        }
    };

    const loadInvoices = async () => {
        if (!user) return;
        try {
        setLoadingInvoices(true);
        let token = cookie.load("token");
        let res = await authApis(token).get(endpoints.getInvoices(user.id));
        setInvoices(res.data);
        } catch (ex) {
        console.error("Lỗi load invoices:", ex);
        setErrInvoices("Không thể tải danh sách hoá đơn.");
        } finally {
        setLoadingInvoices(false);
        }
    };

    useEffect(() => {
        loadInvoices();
    }, [user]);

    return (
        <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#c0dbed' }}>
        <Sidebar />
        <Container fluid className="main-content bg-light-purple px-5 py-4" style={{ marginLeft: '220px'}}>
            
            <h5 className="mb-3">Hoá đơn - Dịch vụ</h5>
            {loadingInvoices ? (
            <Spinner animation="border" />
            ) : invoices.length === 0 ? (
            <Alert variant="info">Bạn chưa có hoá đơn nào.</Alert>
            ) : (
            <Table striped bordered hover responsive className="shadow-sm">
                <thead>
                <tr>
                    <th>Mã hoá đơn</th>
                    <th>Thành tiền</th>
                    <th>Phương thức thanh toán</th>
                    <th>Trạng thái</th>
                    <th>Minh chứng</th>
                    <th>Hành động</th>
                </tr>
                </thead>
                <tbody>
                {invoices.map((inv) => (
                    <tr key={inv.id}>
                    <td>{inv.id}</td>
                    <td>{inv.totalAmount} VND</td>
                    <td>{inv.paymentMethod}</td>
                    <td>{statusBadge(inv.status)}</td>
                    <td>
                        {inv.paymentProof ? (
                        <a href={inv.paymentProof} target="_blank" rel="noreferrer">
                            Xem minh chứng
                        </a>
                        ) : (
                        <Form.Control
                            type="file"
                            size="sm"
                            onChange={(e) => handleUploadProof(inv.id, e.target.files[0])}
                            disabled={uploading}
                        />
                        )}
                    </td>
                    <td>
                        <Button
                        variant="outline-success"
                        size="sm"
                        onClick={() => handleShowPDF(inv)}
                        >
                        Xem / In
                        </Button>
                        <Button
                        variant="outline-danger"
                        size="sm"
                        onClick={() => alert("Vui lòng liên hệ ban quản lý để được hỗ trợ xoá hoá đơn.")}
                        >
                        Xóa
                        </Button>
                    </td>
                    </tr>
                ))}
                </tbody>
            </Table>
            )}

            {/* Modal PDF */}
            <Modal show={showPDF} onHide={() => setShowPDF(false)} fullscreen>
            <Modal.Header closeButton>
                <Modal.Title>Xem và In hóa đơn</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {pdfUrl && (
                <iframe
                    src={pdfUrl}
                    title="Invoice Preview"
                    style={{ width: "100%", height: "90vh", border: "none" }}
                />
                )}
            </Modal.Body>
            </Modal>
        </Container>
        </div>
    );
};

export default Invoice;