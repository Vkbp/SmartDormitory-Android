# ĐỀ XUẤT CHỨC NĂNG MỞ RỘNG CHO MOBILE APP (SINH VIÊN)

> **Mục tiêu:** Tài liệu này mô tả các chức năng đề xuất mở rộng dành cho ứng dụng Mobile Smart Dormitory. Đây là các tính năng hướng đến trải nghiệm sinh viên và là cơ sở để nhóm Backend phân tích, thiết kế Database, API và Business Logic trong các giai đoạn tiếp theo.

---

# PRIORITY P0 - ƯU TIÊN CAO

## 1. Gia hạn lưu trú (Stay Extension)

### Mô tả

Cho phép sinh viên gửi yêu cầu gia hạn thời gian ở KTX khi sắp hết hợp đồng mà không cần đến trực tiếp Ban quản lý.

### Chức năng Mobile

* Chọn thời gian gia hạn (1, 3, 6 tháng...)
* Nhập lý do gia hạn
* Đính kèm minh chứng (nếu cần)
* Theo dõi trạng thái xử lý
* Hủy yêu cầu khi chưa được duyệt

### Backend cần phát triển

### Database

```
stay_extensions
```

### API

```
POST /extensions/request
GET /extensions
GET /extensions/{id}
PUT /extensions/{id}/cancel
```

### Workflow

```
Sinh viên
        ↓
Gửi yêu cầu
        ↓
Ban quản lý xét duyệt
        ↓
Thông báo kết quả
```

---

## 2. Đổi khuôn mặt (Face Replacement)

### Mô tả

Cho phép sinh viên đăng ký cập nhật dữ liệu khuôn mặt khi ngoại hình thay đổi hoặc AI nhận diện không còn chính xác.

### Chức năng Mobile

* Chụp ảnh mới
* Thực hiện Liveness
* Gửi yêu cầu cập nhật
* Theo dõi trạng thái

### Backend cần phát triển

### Database

```
face_replacement_requests
```

### API

```
POST /students/me/face/replacements
GET /students/me/face/replacements
```

### Business Logic

* Lưu ảnh tạm
* Chờ duyệt
* Sinh embedding mới
* Thay thế vector cũ sau khi duyệt

---

## 3. Lịch sử xác thực khuôn mặt

### Mô tả

Sinh viên xem toàn bộ lịch sử các lần xác thực AI.

### Thông tin hiển thị

* Thời gian
* Camera
* Địa điểm
* Độ tin cậy AI
* PASS / FAIL
* Ảnh xác thực

### Backend cần phát triển

### Database

```
face_verification_logs
```

### API

```
GET /students/me/face/verifications
```

---

# PRIORITY P1 - ƯU TIÊN CAO

## 4. Báo hỏng cơ sở vật chất

### Mô tả

Sinh viên gửi yêu cầu sửa chữa trực tiếp trên ứng dụng.

### Chức năng Mobile

* Chụp nhiều ảnh
* Chọn loại hỏng
* Nhập mô tả
* Theo dõi tiến độ

### Backend cần phát triển

### Database

```
maintenance_requests
```

### API

```
POST /maintenance/requests
GET /maintenance/requests
GET /maintenance/requests/{id}
```

---

## 5. Thông báo đẩy (Push Notification)

### Mô tả

Thông báo theo thời gian thực tới sinh viên.

### Nội dung thông báo

* Đơn được duyệt
* Hóa đơn mới
* Thông báo Ban quản lý
* Cảnh báo khẩn cấp

### Backend cần phát triển

### Database

```
notifications
notification_receipts
```

### Công nghệ

* Firebase Cloud Messaging (FCM)

---

## 6. Chi tiết hóa đơn & PDF

### Mô tả

Hiển thị chi tiết từng khoản phí và cho phép tải hóa đơn PDF.

### Nội dung

* Tiền phòng
* Tiền điện
* Tiền nước
* Internet
* Dịch vụ khác

### Backend cần phát triển

### Database

```
invoice_details
```

### API

```
GET /bills/{id}
GET /bills/{id}/pdf
```

---

# PRIORITY P2 - ƯU TIÊN TRUNG BÌNH

## 7. Quản lý khách thăm

### Mô tả

Sinh viên đăng ký khách đến thăm và sinh mã QR để khách sử dụng tại cổng.

### Chức năng Mobile

* Nhập thông tin khách
* Chọn thời gian
* Sinh QR Code

### Backend cần phát triển

### Database

```
visitor_requests
```

### API

```
POST /visitors
GET /visitors
```

---

## 8. Lịch sử gia hạn

### Mô tả

Cho phép xem toàn bộ các đơn gia hạn trước đây.

### Hiển thị

* Thời gian
* Trạng thái
* Người duyệt
* Ghi chú

### API

```
GET /extensions/history
```

---

# PRIORITY P3 - MỞ RỘNG

## 9. Thống kê điện nước

### Mô tả

Hiển thị biểu đồ sử dụng điện nước theo thời gian.

### Mobile

* Line Chart
* Bar Chart

### Backend cần phát triển

### Database

```
utility_usage
```

### API

```
GET /utilities/usage
```

---

## 10. AI Chatbot

### Mô tả

Trợ lý AI giải đáp các câu hỏi liên quan đến KTX.

### Ví dụ

* Nội quy
* Hóa đơn
* Gia hạn
* Đổi phòng
* Báo hỏng

### Backend

* LLM
* RAG
* LangChain

---

## 11. SOS Khẩn cấp

### Mô tả

Cho phép sinh viên gửi cảnh báo khẩn cấp tới bảo vệ.

### Mobile

Một nút SOS lớn.

Thông tin gửi:

* Sinh viên
* Phòng
* Tòa nhà
* GPS
* Thời gian

### Backend

* Push Notification
* Dashboard bảo vệ
* Nhật ký sự kiện

---

## 12. QR Check-in Nội bộ

### Mô tả

Sử dụng QR cho các hoạt động nội bộ.

Ví dụ:

* Hội nghị
* Điểm danh
* Sự kiện
* Workshop

### Backend

```
qr_sessions
qr_checkins
```

---

# CÁC CHỨC NĂNG NÊN BỔ SUNG

## 13. Lịch cắt điện / cắt nước

Sinh viên nhận thông báo trước khi bảo trì.

Backend

```
maintenance_schedule
```

---

## 14. Thông báo nhận bưu phẩm

Sinh viên biết khi có hàng được gửi đến KTX.

Backend

```
packages
```

---

## 15. Đặt lịch sử dụng máy giặt

Cho phép đặt trước khung giờ sử dụng phòng giặt.

Backend

```
laundry_booking
```

---

## 16. Đăng ký chuyển phòng

Sinh viên gửi yêu cầu chuyển phòng trực tuyến.

Backend

```
room_transfer_requests
```

---

## 17. Xin giấy xác nhận nội trú

Sinh viên gửi yêu cầu và tải giấy xác nhận dưới dạng PDF.

Backend

```
resident_certificates
```

---

## 18. Đăng ký trả phòng

Cho phép đăng ký trả phòng cuối học kỳ.

Backend

```
checkout_requests
```

---

## 19. Đánh giá dịch vụ

Sinh viên đánh giá sau khi yêu cầu được xử lý.

Ví dụ

* Báo hỏng
* Chuyển phòng
* Gia hạn

Backend

```
service_feedback
```

---

## 20. Góp ý & Phản ánh

Cho phép gửi góp ý trực tiếp đến Ban quản lý.

Backend

```
feedback
```

---

## 21. Khảo sát sinh viên

Ban quản lý tạo khảo sát trực tuyến.

Backend

```
surveys
survey_answers
```

---

## 22. Dashboard cá nhân

Hiển thị tổng quan:

* Hợp đồng
* Hóa đơn
* Thanh toán
* Ra vào
* Gia hạn
* Thông báo

Backend

API tổng hợp dữ liệu Dashboard.

---

# TỔNG KẾT

## Chức năng đề xuất theo mức ưu tiên

### ⭐⭐⭐⭐⭐ P0

* Gia hạn lưu trú
* Đổi khuôn mặt
* Lịch sử xác thực Face

---

### ⭐⭐⭐⭐ P1

* Báo hỏng
* Thông báo FCM
* Chi tiết hóa đơn PDF

---

### ⭐⭐⭐ P2

* Quản lý khách thăm
* Lịch sử gia hạn

---

### ⭐⭐ P3

* Thống kê điện nước
* AI Chatbot
* SOS
* QR Check-in
* Lịch bảo trì
* Bưu phẩm
* Máy giặt
* Chuyển phòng
* Giấy xác nhận nội trú
* Trả phòng
* Đánh giá dịch vụ
* Góp ý
* Khảo sát
* Dashboard cá nhân

---

## Mục tiêu

Danh sách trên là roadmap đề xuất nhằm mở rộng hệ thống Smart Dormitory theo định hướng **Student-Centric**, giúp ứng dụng Mobile trở thành cổng dịch vụ số toàn diện cho sinh viên nội trú, đồng thời cung cấp cơ sở để nhóm Backend thiết kế API, Database và Business Workflow trong các giai đoạn phát triển tiếp theo.
