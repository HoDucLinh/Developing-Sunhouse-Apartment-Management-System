import { useEffect, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { authApis, endpoints } from "../configs/Apis";
import cookie from "react-cookies";
import { Container, Spinner, Alert, Table, Button } from "react-bootstrap";

const PaymentResult = () => {
  const [params] = useSearchParams();
  const navigate = useNavigate();

  // Params VNPay trả về
  const invoiceId = params.get("invoiceId");
  const responseCode = params.get("vnp_ResponseCode"); // 00 = success
  const transactionNo = params.get("vnp_TransactionNo");
  const bankCode = params.get("vnp_BankCode");
  const amount = params.get("vnp_Amount");
  const payDate = params.get("vnp_PayDate");

  const [invoice, setInvoice] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadInvoice = async () => {
      try {
        let token = cookie.load("token");
        let res = await authApis(token).get(endpoints.getInvoice(invoiceId));
        setInvoice(res.data);
      } catch (err) {
        console.error("Lỗi load invoice:", err);
      } finally {
        setLoading(false);
      }
    };

    if (invoiceId) loadInvoice();
  }, [invoiceId]);

  if (loading) return <Spinner animation="border" />;

  if (!invoice) return <Alert variant="danger">Không tìm thấy hóa đơn!</Alert>;

  return (
    <Container className="mt-4">
      <div className="d-flex align-items-center justify-content-between mb-3">
        <h4 className="mb-0">Kết quả thanh toán VNPay</h4>
        <Button variant="primary" onClick={() => navigate("/home")}>
          Quay về
        </Button>
      </div>

      <Alert variant={responseCode === "00" ? "success" : "danger"}>
        {responseCode === "00"
          ? "Thanh toán thành công!"
          : `Thanh toán thất bại (Mã lỗi: ${responseCode || "N/A"})`}
      </Alert>

      {/* Thông tin hóa đơn */}
      <h5>Thông tin hóa đơn</h5>
      <Table bordered>
        <tbody>
          <tr>
            <td>Mã hóa đơn</td>
            <td>{invoice.id}</td>
          </tr>
          <tr>
            <td>Chủ hóa đơn</td>
            <td>{invoice.fullName}</td>
          </tr>
          <tr>
            <td>Tổng tiền</td>
            <td>{invoice.totalAmount} VND</td>
          </tr>
          <tr>
            <td>Ngày phát hành</td>
            <td>{invoice.issuedDate}</td>
          </tr>
          <tr>
            <td>Hạn thanh toán</td>
            <td>{invoice.dueDate}</td>
          </tr>
        </tbody>
      </Table>

      {/* Chi tiết giao dịch VNPay */}
      <h5>Chi tiết giao dịch</h5>
      <Table bordered>
        <tbody>
          <tr>
            <td>Mã giao dịch VNPay</td>
            <td>{transactionNo || "N/A"}</td>
          </tr>
          <tr>
            <td>Ngân hàng</td>
            <td>{bankCode || "N/A"}</td>
          </tr>
          <tr>
            <td>Số tiền thanh toán</td>
            <td>{amount ? `${amount} VND` : "N/A"}</td>
          </tr>
          <tr>
            <td>Thời gian thanh toán</td>
            <td>{payDate || "N/A"}</td>
          </tr>
        </tbody>
      </Table>
    </Container>
  );
};

export default PaymentResult;
