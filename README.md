# 🏨 PHẦN MỀM QUẢN LÝ KHÁCH SẠN

Ứng dụng quản lý khách sạn sử dụng **Java Spring Boot**, **Thymeleaf**, **HTML/CSS/JS**, **Python** và **MySQL**, hỗ trợ các chức năng từ quản lý người dùng, đặt phòng đến phản hồi và báo cáo doanh thu.

![Hotel Management Screenshot](/src/main/resources/static/images/banner.png)

---

## 📌 Nội dung

- [🎯 Tính năng](#-tính-năng)
- [⚙️ Cài đặt](#️-cài-đặt)
- [💻 Môi trường](#-môi-trường)
- [📚 Tài liệu tham khảo](#-tài-liệu-tham-khảo)
- [🐞 Bugs và vấn đề](#-bugs-và-các-vấn-đề)
- [🚧 Tính năng đang phát triển](#-tính-năng-đang-phát-triển)
- [👨‍💻 Tác giả](#-tác-giả)

---

## 🎯 Tính năng

### 🛠️ Quản trị viên:
- Xem tổng quan hệ thống (doanh thu tháng, biểu đồ, số khách đang lưu trú,…)
- Quản lý:
  - Khách hàng
  - Phòng
  - Đặt phòng
  - Voucher / mã giảm giá
  - Đồ thất lạc
  - Phản hồi và đánh giá

### 👤 Khách hàng:
- Đăng ký và đăng nhập tài khoản
- Xem ưu đãi và liên hệ
- Đặt phòng, thanh toán, và in hóa đơn
- Gửi phản hồi và đánh giá chất lượng dịch vụ
- Chỉnh sửa thông tin cá nhân

---

## ⚙️ Cài đặt

### a. Clone dự án

```bash
git clone https://github.com/quyen29/lacitahotelnhom7.git
```

### b. Cấu hình cơ sở dữ liệu

Mở file `src/main/resources/application.properties` và cập nhật:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookingsystemdb
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

Mở file `src/main/resources/static/pythonServer/faceRecognition.py` và cập nhật:

```properties
connection = pymysql.connect(
        host="localhost",
        user=YOUR_USERNAME,
        password=YOUR_PASSWORD,
        database="bookingsystemdb",
        cursorclass=pymysql.cursors.DictCursor
)
```

### c. Chạy ứng dụng

- Mở bằng IntelliJ IDEA
- Cài thư viện Python cho AI khuôn mặt:

```bash
cd src/main/resources/static/pythonServer
py -m pip install -r requirements.txt
```

- Chạy AI server nhận diện khuôn mặt:

```bash
py faceRecognition.py
```

### d. Tài khoản hệ thống mẫu

Xem tài khoản quản trị trong file `dataSample.sql`.

---

## 💻 Môi trường

- Java 17+
- Spring Boot
- MySQL 8+
- IntelliJ IDEA
- Docker Desktop *(tuỳ chọn)*

---

## 📚 Tài liệu tham khảo

- Nhận diện khuôn mặt: [DeepFace](https://pypi.org/project/deepface/)
- Quét mã QR: [ZXing](https://github.com/zxing/zxing/)
- Dịch vụ Email: [Brevo (ex-Sendinblue)](https://www.brevo.com/)

---

## 🐞 Bugs và các vấn đề

Gặp lỗi hoặc có thắc mắc? Hãy tạo [Issue](https://github.com/quyen29/lacitahotelnhom7/issues) trên GitHub để được hỗ trợ.

---

## 🚧 Tính năng đang phát triển

- Triển khai server Ubuntu
- Đóng gói Docker
- Phát triển phiên bản mobile (Android/iOS)

---

## 👨‍💻 Tác giả

- Trần Phúc Tiến  
- Đỗ Xuân Dũng  
- Trần Công Hậu  
- Đinh Hoàng Quân  
- Mai Mỹ Quyên

---
