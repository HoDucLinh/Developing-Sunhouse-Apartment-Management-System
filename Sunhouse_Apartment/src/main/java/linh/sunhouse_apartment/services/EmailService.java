package linh.sunhouse_apartment.services;

import java.util.Date;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendNewPackageNotification(String recipientEmail, String packageName, Date createdAt) throws MessagingException;
    void sendNewPasswordNotification(String recipientEmail, String name) throws MessagingException;

}
