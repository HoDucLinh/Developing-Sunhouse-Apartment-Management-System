import { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import { Container, Row, Col, Table, Badge, Button, Spinner, Alert, Form, Modal, Card } from 'react-bootstrap';
import { authApis, endpoints } from '../configs/Apis';
import cookie from 'react-cookies';
import { useUser } from '../contexts/UserContext';
import { pdf } from "@react-pdf/renderer";
import InvoicePDF from '../components/InvoicePDF';

const Invoice = () => {
    const { user } = useUser(); 
    const [invoices, setInvoices] = useState([]);
    const [loadingInvoices, setLoadingInvoices] = useState(true);
    const [errInvoices, setErrInvoices] = useState(null);   // sửa lỗi typo ở đây

    const [showPDF, setShowPDF] = useState(false);
    const [selectedInvoice, setSelectedInvoice] = useState(null);
    const [pdfUrl, setPdfUrl] = useState(null);

    const [uploading, setUploading] = useState(false);

    const [showDetail, setShowDetail] = useState(false);
    const [detailInvoice, setDetailInvoice] = useState(null);

    const unpaidCount = invoices.filter(inv => 
        inv.status?.trim() === 'Chưa thanh toán' || 
        inv.status?.trim() === 'UNPAID'
    ).length;

    const statusBadge = (status) => {
        switch (status?.trim()) {
            case 'Đã thanh toán':
            case 'PAID':
                return <Badge bg="success" pill className="px-3 py-2 fs-6">✔ Đã thanh toán</Badge>;
            
            case 'Chưa thanh toán':
            case 'UNPAID':
                return <Badge bg="danger" pill className="px-3 py-2 fs-6">✘ Chưa thanh toán</Badge>;
        
            default:
                return <Badge bg="secondary" pill className="px-3 py-2 fs-6">{status}</Badge>;
        }
    };

    const handlePayVNPay = async (invoice) => {
        try {
            let token = cookie.load("token");
            let res = await authApis(token).post(endpoints.paymentVNPay, {
                invoiceId: invoice.id,
                amount: invoice.totalAmount
            });

            const payUrl = res.data;
            if (payUrl) {
                window.location.href = payUrl;
            } else {
                alert("Không tạo được đơn hàng VNPay.");
            }
        } catch (err) {
            console.error("VNPay error:", err);
            alert("Thanh toán VNPay thất bại!");
        }
    };

    const handleShowDetail = (invoice) => {
        setDetailInvoice(invoice);
        setShowDetail(true);
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
            setErrInvoices(null);
            let token = cookie.load("token");
            let res = await authApis(token).get(endpoints.getInvoices(user.id));
            setInvoices(res.data);
        } catch (ex) {
            console.error("Lỗi load invoices:", ex);
            setErrInvoices("Không thể tải danh sách hoá đơn. Vui lòng thử lại sau.");
        } finally {
            setLoadingInvoices(false);
        }
    };

    useEffect(() => {
        loadInvoices();
    }, [user]);

    return (
        <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#f8f9fa' }}>
            <Sidebar />

            <Container fluid className="px-5 py-4" style={{ marginLeft: '220px' }}>
                
                {/* Header */}
                <div className="d-flex justify-content-between align-items-center mb-5">
                    <div>
                        <h1 className="display-6 fw-bold text-dark">Hoá đơn & Thanh toán</h1>
                        <p className="text-muted mb-0">Quản lý hoá đơn dịch vụ và thanh toán của bạn</p>
                    </div>
                    <div className="text-muted text-center mt-4 small">
                        Tổng số hoá đơn: <strong>{invoices.length}</strong>
                    </div>
                    {unpaidCount > 0 && (
                        <div className="bg-danger text-white px-2 py-2 rounded-3 shadow-sm">
                            <small>Chưa thanh toán</small>
                            <h3 className="mb-0 fw-bold text-center">{unpaidCount}</h3>
                        </div>
                    )}
                </div>

                {loadingInvoices ? (
                    <div className="text-center py-5">
                        <Spinner animation="border" variant="success" size="lg" />
                        <p className="mt-3 text-muted">Đang tải danh sách hoá đơn...</p>
                    </div>
                ) : errInvoices ? (
                    <Alert variant="danger">{errInvoices}</Alert>
                ) : invoices.length === 0 ? (
                    <Alert variant="info" className="text-center py-4">
                        Bạn chưa có hoá đơn nào.
                    </Alert>
                ) : (
                    <>
                        <Card className="shadow border-0 rounded-4 overflow-hidden">
                            <Card.Body className="p-0">
                                <Table responsive hover className="mb-0 align-middle">
                                    <thead className="table-light">
                                        <tr>
                                            <th>Mã hoá đơn</th>
                                            <th>Thành tiền</th>
                                            <th>Phương thức</th>
                                            <th>Trạng thái</th>
                                            <th>Minh chứng</th>
                                            <th className="text-center">Hành động</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {invoices.map((inv) => (
                                            <tr key={inv.id}>
                                                <td className="fw-medium">#{inv.id}</td>
                                                <td className="fw-bold text-success">
                                                    {inv.totalAmount.toLocaleString('vi-VN')} ₫
                                                </td>
                                                <td>{inv.paymentMethod || "—"}</td>
                                                <td>{statusBadge(inv.status)}</td>
                                                <td>
                                                    {inv.paymentProof ? (
                                                        <a 
                                                            href={inv.paymentProof} 
                                                            target="_blank" 
                                                            rel="noreferrer"
                                                            className="text-success fw-medium"
                                                        >
                                                            Xem minh chứng
                                                        </a>
                                                    ) : (
                                                        <Form.Control
                                                            type="file"
                                                            size="sm"
                                                            onChange={(e) => handleUploadProof(inv.id, e.target.files[0])}
                                                            disabled={uploading}
                                                            className="w-auto"
                                                        />
                                                    )}
                                                </td>
                                                <td className="text-center">
                                                    <div className="d-flex gap-2 justify-content-center flex-wrap">
                                                        <Button
                                                            variant="outline-success"
                                                            size="sm"
                                                            onClick={() => handleShowPDF(inv)}
                                                        >
                                                            📄 In hoá đơn
                                                        </Button>
                                                        <Button
                                                            variant="outline-info"
                                                            size="sm"
                                                            onClick={() => handleShowDetail(inv)}
                                                        >
                                                            👁 Xem chi tiết
                                                        </Button>
                                                        {inv.status !== 'Đã thanh toán' && (
                                                            <Button
                                                                variant="primary"
                                                                size="sm"
                                                                onClick={() => handlePayVNPay(inv)}
                                                            >
                                                                Thanh toán VNPay
                                                            </Button>
                                                        )}
                                                    </div>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </Table>
                            </Card.Body>
                        </Card>
                    </>
                )}

                {/* Modal Chi tiết hóa đơn */}
                <Modal show={showDetail} onHide={() => setShowDetail(false)} size="lg" centered>
                    <Modal.Header closeButton>
                        <Modal.Title>Chi tiết hóa đơn #{detailInvoice?.id}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {detailInvoice ? (
                            <>
                                <Card className="mb-4">
                                    <Card.Body>
                                        <h6 className="fw-semibold mb-3">Thông tin chung</h6>
                                        <Row>
                                            <Col md={6}>
                                                <p><strong>Khách hàng:</strong> {detailInvoice.fullName}</p>
                                                <p><strong>Ngày lập:</strong> {detailInvoice.issuedDate}</p>
                                                <p><strong>Hạn thanh toán:</strong> {detailInvoice.dueDate}</p>
                                            </Col>
                                            <Col md={6}>
                                                <p><strong>Phương thức:</strong> {detailInvoice.paymentMethod}</p>
                                                <p><strong>Thành tiền:</strong> <span className="fw-bold text-success">
                                                    {detailInvoice.totalAmount.toLocaleString('vi-VN')} ₫
                                                </span></p>
                                                <p><strong>Trạng thái:</strong> {statusBadge(detailInvoice.status)}</p>
                                            </Col>
                                        </Row>
                                    </Card.Body>
                                </Card>

                                <h6 className="fw-semibold mb-3">Chi tiết dịch vụ</h6>
                                <Table striped bordered hover responsive>
                                    <thead className="table-light">
                                        <tr>
                                            <th>Tên dịch vụ</th>
                                            <th>Số tiền</th>
                                            <th>Ghi chú</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {detailInvoice.details?.map((d, idx) => (
                                            <tr key={idx}>
                                                <td className="fw-medium">{d.feeName}</td>
                                                <td className="fw-medium">{d.amount.toLocaleString('vi-VN')} ₫</td>
                                                <td>{d.note || "—"}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </Table>
                            </>
                        ) : (
                            <Alert variant="info">Không có dữ liệu hóa đơn</Alert>
                        )}
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowDetail(false)}>
                            Đóng
                        </Button>
                    </Modal.Footer>
                </Modal>

                {/* Modal PDF */}
                <Modal show={showPDF} onHide={() => {
                    setShowPDF(false);
                    if (pdfUrl) URL.revokeObjectURL(pdfUrl);
                }} fullscreen>
                    <Modal.Header closeButton>
                        <Modal.Title>
                            Xem và In hóa đơn #{selectedInvoice?.id}
                        </Modal.Title>
                    </Modal.Header>
                    <Modal.Body className="p-0">
                        {pdfUrl && (
                            <iframe
                                src={pdfUrl}
                                title="Invoice Preview"
                                style={{ width: "100%", height: "100vh", border: "none" }}
                            />
                        )}
                    </Modal.Body>
                </Modal>
            </Container>
        </div>
    );
};

export default Invoice;