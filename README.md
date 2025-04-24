
# 🌴 Resort Booking Website

## 📌 Giới thiệu
Website đặt phòng resort giúp tối ưu hóa quy trình quản lý đặt phòng, tăng trải nghiệm người dùng và giảm tải công việc cho nhân viên. Hệ thống được xây dựng cho các resort quy mô vừa và nhỏ, với đầy đủ tính năng như quản lý phòng, đặt phòng trực tuyến, thanh toán, phản hồi và thống kê báo cáo.

## 👨‍💻 Nhóm phát triển
- **Trần Phúc Tiến** - N22DCAT057  
- **Đỗ Xuân Dũng** - N22DCAT010  
- **Trần Công Hậu** - N22DCAT020  
- **Đinh Hoàng Quân** – N22DCAT044  
- **Mai Mỹ Quyên** - N22DCCN166  
- **GVHD:** ThS. Châu Văn Vân

## 🎯 Tính năng chính
### Cho khách hàng
- Đăng ký, đăng nhập, xác thực qua email
- Xem thông tin phòng, đặt phòng theo ngày, loại phòng
- Thanh toán trực tuyến
- Gửi và xem phản hồi
- Quản lý thông tin cá nhân và lịch sử đặt phòng

### Cho quản trị viên
- Quản lý phòng (thêm/sửa/xóa, cập nhật trạng thái động)
- Quản lý đặt phòng và thanh toán
- Xem, quản lý phản hồi khách hàng
- Báo cáo thống kê doanh thu, lượt khách, đánh giá
- Tích hợp AI nhận diện khuôn mặt (DeepFace)

## 🛠 Công nghệ sử dụng
| Thành phần      | Công nghệ              |
|----------------|------------------------|
| Frontend       | HTML5, CSS3, JS, Thymeleaf, Bootstrap |
| Backend        | Java 17, Spring Boot, Spring MVC, Spring Security |
| CSDL           | MySQL 8.x              |
| AI             | DeepFace (Python 3.11)      |
| Build Tool     | Maven                  |
| IDE            | IntelliJ IDEA / VS Code |
| Quản lý phiên bản | Git + GitHub        |

## 🧱 Cấu trúc thư mục
```
/controller         --> Xử lý yêu cầu từ frontend  
/entity            --> Lớp ánh xạ dữ liệu  
/service           --> Xử lý nghiệp vụ  
/repository (dao)  --> Giao tiếp với cơ sở dữ liệu  
/templates         --> Giao diện người dùng (.html)  
/static            --> CSS/JS/Images  
```

## 🚀 Hướng dẫn cài đặt
```bash
# 1. Clone repo
git clone https://github.com/<your-username>/resort-booking-system.git

# 2. Cấu hình database trong file:
src/main/resources/application.properties

spring.datasource.url=jdbc:mysql://localhost:3306/bookingsystemdb
spring.datasource.username=vos
spring.datasource.password=vos!12345678

# 3. Build project
mvn clean install

# 4. Chạy ứng dụng
mvn spring-boot:run

# 5. Truy cập website
http://localhost:8080
```

## 🤖 Nhận diện khuôn mặt (AI - DeepFace)
```bash
# Chạy server nhận diện khuôn mặt (cần cài các thư viện trong /static/pythonServer)
py src/main/resources/static/pythonServer/faceRecognition.py
```

## ✅ Kiểm thử
Các chức năng chính đã được kiểm thử thành công bao gồm:
- Đăng ký, đăng nhập
- Đặt phòng, thanh toán
- Gửi, xem phản hồi
- Thống kê, báo cáo (quản trị viên)

## 📈 Hướng phát triển
- Tích hợp thanh toán online hoàn chỉnh
- Triển khai trên máy chủ thực tế (Ubuntu)
- Tối ưu hiển thị trên thiết bị di động
- Thêm quản lý dịch vụ kèm theo resort

