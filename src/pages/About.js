import React from "react";
import { Container } from "react-bootstrap";
import HeaderHome from "../components/HeaderHome";

const About = () => (
  <div style={{ marginTop: "80px" }}>
    <HeaderHome></HeaderHome>
    <Container>
        <h2 className="text-center mb-4 text-primary">Về Sunhouse Apartment</h2>
        <p className="fs-6 text-muted">
        <strong>Sunhouse Apartment</strong> không chỉ là một khu chung cư – mà là một phong cách sống hiện đại, năng động và tiện nghi dành riêng cho những cư dân yêu thích sự tinh tế và đẳng cấp.
        Tọa lạc tại vị trí đắc địa, Sunhouse Apartment được quy hoạch theo mô hình khu căn hộ thông minh với hệ sinh thái dịch vụ hoàn chỉnh. Từ các căn hộ được thiết kế theo phong cách châu Âu hiện đại, đến không gian xanh bao phủ quanh dự án, mọi yếu tố đều hướng đến việc kiến tạo một cuộc sống tiện nghi, an toàn và thân thiện với môi trường.
        </p>

        <p className="fs-6 text-muted">
        Chúng tôi trang bị hệ thống <strong>quản lý thông minh</strong> giúp cư dân có thể:
        <ul className="mt-2 mb-3">
            <li>📱 Đặt lịch sử dụng các tiện ích như hồ bơi, phòng gym, sân thể thao chỉ bằng vài thao tác.</li>
            <li>🔔 Nhận thông báo tức thì về các sự kiện, lịch bảo trì, hoặc cảnh báo an ninh.</li>
            <li>🛠️ Gửi phản ánh, yêu cầu sửa chữa, hỗ trợ kỹ thuật dễ dàng và minh bạch.</li>
            <li>📊 Theo dõi chi phí điện nước, phí quản lý ngay trong ứng dụng quản lý cư dân.</li>
        </ul>
        </p>

        <p className="fs-6 text-muted">
        Không chỉ dừng lại ở công nghệ, Sunhouse còn sở hữu chuỗi tiện ích phong phú như:
        <ul className="mt-2 mb-3">
            <li>🏊 Hồ bơi tràn bờ chuẩn resort</li>
            <li>💪 Phòng gym đầy đủ thiết bị hiện đại</li>
            <li>🔐 An ninh đa lớp 24/7 với hệ thống camera và thẻ từ</li>
            <li>☕ Khu thương mại – café – siêu thị mini ngay trong khuôn viên</li>
        </ul>
        </p>

        <p className="fs-6 text-muted">
        Tại Sunhouse, chúng tôi tin rằng <strong>“chung cư không chỉ là nơi ở, mà là nơi để sống hạnh phúc”</strong>. Với đội ngũ quản lý chuyên nghiệp, thái độ phục vụ tận tâm và sự đầu tư bài bản, Sunhouse Apartment đang dần trở thành biểu tượng của cuộc sống đô thị văn minh – nơi mọi cư dân đều cảm thấy tự hào khi gọi là “nhà”.
        </p>
    </Container>
  </div>
);

export default About;
