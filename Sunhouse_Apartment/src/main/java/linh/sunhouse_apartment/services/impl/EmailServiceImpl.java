package linh.sunhouse_apartment.services.impl;

import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import linh.sunhouse_apartment.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.Transport;

import java.util.Date;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private Environment env;

    @Override
    public void sendNewPackageNotification(String recipientEmail, String packageName, Date createdAt) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", env.getProperty("mail.smtp.host"));
        props.put("mail.smtp.port", env.getProperty("mail.smtp.port"));
        props.put("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable"));

        String username = env.getProperty("mail.smtp.username");
        String password = env.getProperty("mail.smtp.password");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(username, password);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject("Thông báo: Gói hàng mới trong locker", "UTF-8");

        String htmlContent = "<h3>Xin chào,</h3>"
                + "<p>Một gói hàng mới đã được thêm vào locker của bạn:</p>"
                + "<ul>"
                + "<li><strong>Tên gói hàng:</strong> " + packageName + "</li>"
                + "<li><strong>Thời gian:</strong> " + createdAt + "</li>"
                + "</ul>"
                + "<p>Vui lòng đến nhận gói hàng tại locker sớm nhất có thể.</p>"
                + "<p>Trân trọng,<br/>Ban quản lý chung cư</p>";

        message.setContent(htmlContent, "text/html; charset=UTF-8");

        Transport.send(message);
    }

    @Override
    public void sendNewPasswordNotification(String recipientEmail, String name) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", env.getProperty("mail.smtp.host"));
        props.put("mail.smtp.port", env.getProperty("mail.smtp.port"));
        props.put("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable"));

        String username = env.getProperty("mail.smtp.username");
        String password = env.getProperty("mail.smtp.password");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(username, password);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject("THÔNG BÁO : MẬT KHẨU TÀI KHOẢN CHUNG CƯ SUNHOUSE APARTMENT CỦA BẠN ĐÃ ĐƯỢC THAY ĐỔI", "UTF-8");

        String htmlContent = "<h3>Xin chào," + name + "</h3>"
                + "<p>Mật khẩu của bạn đã bị thay đổi gần đây, bạn vui lòng đăng nhập lại với thông tin sau : </p>"
                + "<ul>"
                + "<li><strong>USERNAME : </strong> " + name + "</li>"
                + "<li><strong>PASSWORD : </strong> " + 123456789 + "</li>"
                + "</ul>"
                + "<p>Bạn vui lòng thông báo lại với quản trị viên nếu như bạn không có hành động thay đổi mật khẩu này.</p>"
                + "<p>Trân trọng,<br/>Ban quản lý chung cư</p>";

        message.setContent(htmlContent, "text/html; charset=UTF-8");

        Transport.send(message);
    }
}
