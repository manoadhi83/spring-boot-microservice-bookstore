package com.manoa.notification_service.domain;

import com.manoa.notification_service.ApplicationProperties;
import com.manoa.notification_service.domain.model.OrderCancelledEvent;
import com.manoa.notification_service.domain.model.OrderCreatedEvent;
import com.manoa.notification_service.domain.model.OrderDeliveredEvent;
import com.manoa.notification_service.domain.model.OrderErrorEvent;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);

    private final ApplicationProperties properties;
    private final JavaMailSender mailSender;

    NotificationService(ApplicationProperties properties, JavaMailSender mailSender) {
        this.properties = properties;
        this.mailSender = mailSender;
    }

    public void sendOrderCreatedNotification(OrderCreatedEvent event) {
        String message =
                """
                ===================================================
                Order Created Notification
                ----------------------------------------------------
                Dear %s,
                Your order with orderNumber: %s has been created successfully.

                Thanks,
                BookStore Team
                ===================================================
                """
                        .formatted(event.customer().name(), event.orderNumber());
        LOG.info("\n{}", message);
        sendEmail(event.customer().email(), "Order Created Notification", message);
    }

    public void sendOrderDeliveredNotification(OrderDeliveredEvent event) {
        String message =
                """
                ===================================================
                Order Delivered Notification
                ----------------------------------------------------
                Dear %s,
                Your order with orderNumber: %s has been delivered successfully.

                Thanks,
                BookStore Team
                ===================================================
                """
                        .formatted(event.customer().name(), event.orderNumber());
        LOG.info("\n{}", message);
        sendEmail(event.customer().email(), "Order Delivered Notification", message);
    }

    public void sendOrderCancelledNotification(OrderCancelledEvent event) {
        String message =
                """
                ===================================================
                Order Cancelled Notification
                ----------------------------------------------------
                Dear %s,
                Your order with orderNumber: %s has been cancelled.
                Reason: %s

                Thanks,
                BookStore Team
                ===================================================
                """
                        .formatted(event.customer().name(), event.orderNumber(), event.reason());
        LOG.info("\n{}", message);
        sendEmail(event.customer().email(), "Order Cancelled Notification", message);
    }

    public void sendOrderErrorEventNotification(OrderErrorEvent event) {
        String message =
                """
                ===================================================
                Order Processing Failure Notification
                ----------------------------------------------------
                Hi %s,
                The order processing failed for orderNumber: %s.
                Reason: %s

                Thanks,
                BookStore Team
                ===================================================
                """
                        .formatted(properties.supportEmail(), event.orderNumber(), event.reason());
        LOG.info("\n{}", message);
        sendEmail(properties.supportEmail(), "Order Processing Failure Notification", message);
    }

    private void sendEmail(String recipient, String subject, String content) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(properties.supportEmail());
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(content);
            mailSender.send(mimeMessage);
            LOG.info("Email sent to: {}", recipient);
        } catch (Exception e) {
            throw new RuntimeException("Error while sending email", e);
        }
    }
}
