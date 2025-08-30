import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { authApis, endpoints } from "../configs/Apis";
import cookie from "react-cookies";
import { Container, Spinner, Alert, Table } from "react-bootstrap";

const PaymentResult = () => {
  const [params] = useSearchParams();
  const invoiceId = params.get("invoiceId");
  const returnCode = params.get("return_code");
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadResult = async () => {
      try {
        let token = cookie.load("token");
        let res = await authApis(token).get(endpoints.getInvoice(invoiceId));
        setResult(res.data);
      } catch (err) {
        console.error("Lỗi load result:", err);
      } finally {
        setLoading(false);
      }
    };

    if (invoiceId) loadResult();
  }, [invoiceId]);

  if (loading) return <Spinner animation="border" />;

  if (!result) return <Alert variant="danger">Không tìm thấy kết quả!</Alert>;

  return (
    <Container className="mt-4">
      <h4>Kết quả thanh toán</h4>
      <Alert variant={returnCode === "1" ? "success" : "danger"}>
        {returnCode === "1"
          ? "Thanh toán thành công!"
          : "Thanh toán thất bại!"}
      </Alert>
      <Table bordered>
        <tbody>
          <tr>
            <td>Mã hóa đơn</td>
            <td>{result.id}</td>
          </tr>
          <tr>
            <td>Chủ hóa đơn</td>
            <td>{result.fullName}</td>
          </tr>
          <tr>
            <td>Tổng tiền</td>
            <td>{result.totalAmount} VND</td>
          </tr>
          <tr>
            <td>Ngày phát hành</td>
            <td>{result.issuedDate}</td>
          </tr>
          <tr>
            <td>Hạn thanh toán</td>
            <td>{result.dueDate}</td>
          </tr>
        </tbody>
      </Table>
    </Container>
  );
};

export default PaymentResult;
