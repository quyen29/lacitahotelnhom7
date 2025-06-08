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

<<<<<<< HEAD
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

=======
>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43
@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
<<<<<<< HEAD
    @Autowired
    private QRCodeService qrCodeService;
=======
>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43

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

<<<<<<< HEAD
    public void sendInvoiceEmailWithQR(
            String toEmail,
            Bill bill,
            Booking booking,
            Customer customer,
            RoomType roomType,
            int roomQuantity,
            String platformRaw,
            String paymentMethodRaw,
            List<Integer> roomIds
    ) {
=======
    public void sendInvoiceEmail(String toEmail, Bill bill, Booking booking, Customer customer, RoomType roomType, int roomQuantity) {
>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("lacitahotelnhom7@gmail.com", "La CiTa Hotel");
            helper.setTo(toEmail);
<<<<<<< HEAD
            helper.setSubject("Hóa đơn thanh toán - La CiTa Hotel");
            String roomIdText = roomIds.stream().map(Object::toString).collect(Collectors.joining(", "));
            String invoiceTime = bill.getInvoiceTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            String checkInTime = booking.getCheckInDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            String checkOutTime = booking.getCheckOutDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            String paymentDisplay = "Tiền mặt".equalsIgnoreCase(paymentMethodRaw) ? "Tiền mặt" : "Chuyển khoản";
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            String formattedTotal = currencyFormat.format(bill.getTotal());
            Map<String, String> platformMap = Map.of(
                    "momo", "Momo",
                    "vnpay", "VNPAY",
                    "vietcombank", "Vietcombank",
                    "mbbank", "MB Bank",
                    "techcombank", "Techcombank",
                    "bidv", "BIDV"
            );
            String platform = "Tiền mặt".equalsIgnoreCase(paymentMethodRaw) ? "" :
                    platformMap.getOrDefault(platformRaw != null ? platformRaw.toLowerCase() : "", "Chuyển khoản");
            String platformHtml = platform.isEmpty() ? "" :
                    String.format("<p><strong>Nền tảng thanh toán:</strong> %s</p>", platform);
            String content = String.format("""
                            <h2>Hóa Đơn Thanh Toán</h2>
                            <p><strong>ID hóa đơn:</strong> %d</p>
                            <p><strong>Thời gian xuất hóa đơn:</strong> %s</p>
                            <p><strong>Họ tên người đặt:</strong> %s</p>
                            <p><strong>Số điện thoại:</strong> %s</p>
                            <p><strong>Loại phòng:</strong> %s</p>
                            <p><strong>Số lượng phòng:</strong> %d</p>
                            <p><strong>ID phòng:</strong> %s</p>
                            <p><strong>Thời gian nhận phòng:</strong> %s</p>
                            <p><strong>Thời gian trả phòng:</strong> %s</p>
                            <p><strong>Số lượng người lớn:</strong> %d</p>
                            <p><strong>Số lượng trẻ em:</strong> %d</p>
                            <p><strong>Phương thức thanh toán:</strong> %s</p>
                            %s
                            <p><strong>Tổng tiền:</strong> %s VND</p>
                            """,
                    bill.getBillID(), invoiceTime,
                    customer.getFullName(), customer.getPhoneNumber(),
                    roomType.getRoomTypeName(), roomQuantity,
                    roomIdText, checkInTime, checkOutTime,
                    booking.getNumberOfAdult(), booking.getNumberOfChild(),
                    paymentDisplay, platformHtml, formattedTotal
            );
            StringBuilder qrData = new StringBuilder();
            qrData.append(String.format("""
                            ID hóa đơn: %d
                            Thời gian xuất hóa đơn: %s
                            Họ tên: %s
                            Email: %s
                            Số điện thoại: %s
                            Loại phòng: %s
                            Số lượng phòng: %d
                            ID phòng: %s
                            Số người lớn: %d
                            Số trẻ em: %d
                            Phương thức thanh toán: %s
                            """,
                    bill.getBillID(), invoiceTime,
                    customer.getFullName(), customer.getEmail(),
                    customer.getPhoneNumber(), roomType.getRoomTypeName(),
                    roomQuantity, roomIdText,
                    booking.getNumberOfAdult(), booking.getNumberOfChild(),
                    paymentDisplay
            ));
            if (!platform.isEmpty()) {
                qrData.append("Nền tảng: ").append(platform).append("\n");
            }
            qrData.append("Tổng tiền: ").append(formattedTotal).append(" VND");
            byte[] qrBytes = qrCodeService.generateQRCodeImage(qrData.toString(), 250, 250);
            helper.setText(content, true);
            helper.addAttachment("qr.png", () -> new java.io.ByteArrayInputStream(qrBytes), "image/png");
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Gửi email hóa đơn QR thất bại: " + e.getMessage());
        }
    }
}
=======
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

>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43
