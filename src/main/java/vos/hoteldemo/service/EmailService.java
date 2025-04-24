package vos.hoteldemo.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import vos.hoteldemo.entity.Bill;
import vos.hoteldemo.entity.Booking;
import vos.hoteldemo.entity.Customer;
import vos.hoteldemo.entity.RoomType;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String verificationCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("lacitahotelnhom7@gmail.com", "La CiTa Hotel");
            helper.setTo(toEmail);
            helper.setSubject("Xác thực Email - La CiTa Hotel");
            helper.setText(
                    "<p>Xin chào,</p>" +
                            "<p>Mã xác thực của bạn là: <b>" + verificationCode + "</b></p>" +
                            "<p>Vui lòng nhập mã này trên trang xác thực để tiếp tục.</p>" +
                            "<p>Trân trọng,<br>La CiTa Hotel</p>", true);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Gửi email thất bại: " + e.getMessage());
        }
    }

    public void sendInvoiceEmail(String toEmail, Bill bill, Booking booking, Customer customer, RoomType roomType, int roomQuantity) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("lacitahotelnhom7@gmail.com", "La CiTa Hotel");
            helper.setTo(toEmail);
            helper.setSubject("Hóa đơn đặt phòng - La CiTa Hotel");
            String content = """
                        <h2>Hóa Đơn Thanh Toán</h2>
                        <p><strong>ID hóa đơn:</strong> %d</p>
                        <p><strong>Thời gian xuất hóa đơn:</strong> %s</p>
                        <p><strong>Họ tên người đặt:</strong> %s</p>
                        <p><strong>Số điện thoại:</strong> %s</p>
                        <p><strong>Loại phòng:</strong> %s</p>
                        <p><strong>Số lượng phòng:</strong> %d</p>
                        <p><strong>Thời gian nhận phòng:</strong> %s</p>
                        <p><strong>Thời gian trả phòng:</strong> %s</p>
                        <p><strong>Số lượng người:</strong> %d</p>
                        <p><strong>Phương thức thanh toán:</strong> %s</p>
                        <p><strong>Tổng tiền:</strong> %.0f VND</p>
                    """.formatted(
                    bill.getBillID(),
                    bill.getInvoiceTime().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    customer.getFullName(),
                    customer.getPhoneNumber(),
                    roomType.getRoomTypeName(),
                    roomQuantity,
                    booking.getCheckInDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    booking.getCheckOutDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    booking.getNumberOfPeople(),
                    bill.getPaymentMethod(),
                    bill.getTotal()
            );
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Gửi email hóa đơn thất bại: " + e.getMessage());
        }
    }
}

