# BACKEND REQUIREMENTS - YÊU CẦU ĐỊNH HƯỚNG PHÁT TRIỂN BACKEND

Dựa trên cấu trúc mã nguồn di động hiện tại, tài liệu này mô tả các yêu cầu kỹ thuật mà hệ thống Backend cần đáp ứng để hỗ trợ toàn diện cho ứng dụng di động.

## 1. Yêu cầu về API & Giao thức
*   **RESTful API**: Sử dụng JSON làm định dạng trao đổi dữ liệu chính.
*   **Authentication**: Hỗ trợ OAuth2/JWT. Cần cơ chế **Refresh Token** để duy trì phiên đăng nhập cho sinh viên.
*   **Versioning**: Các API nên được bắt đầu bằng `/api/v1/...` để dễ dàng nâng cấp sau này.
*   **Standard Response**: Thống nhất cấu trúc phản hồi lỗi (Error Code, Message, Details) để Mobile xử lý đồng bộ.

## 2. Các Module Backend Cần Thiết

### Module AI & Image Processing
*   **API Nhận diện**: Tiếp nhận vector Embedding từ Mobile để đối sánh với cơ sở dữ liệu.
*   **Storage**: Tích hợp với Cloudinary hoặc S3 để lưu trữ ảnh thẻ và ảnh đăng ký khuôn mặt của sinh viên.
*   **Face Matching**: Hệ thống lưu trữ Vector Database (như Milvus hoặc pgvector) để tìm kiếm khuôn mặt nhanh chóng trong hàng ngàn sinh viên.

### Module Tài chính & Thanh toán
*   **Webhooks**: Nhận thông báo từ các cổng thanh toán (SePay, Momo, VNPay) để tự động gạch nợ hóa đơn.
*   **Job Scheduler**: Tự động quét và sinh hóa đơn tiền phòng, điện nước vào ngày 1 hàng tháng.
*   **Notification**: Gửi thông báo đẩy (Firebase Cloud Messaging) khi có hóa đơn mới hoặc sắp hết hạn.

### Module Quản lý Lưu trú
*   **Business Logic**: Xử lý logic gán phòng, giường tự động hoặc thủ công từ phía Admin.
*   **Workflow Engine**: Xử lý luồng phê duyệt đơn đăng ký (Student -> Staff -> Manager).

## 3. Yêu cầu về Cơ sở dữ liệu (Database)
Hệ thống cần các bảng dữ liệu sau (tham khảo chi tiết tại DATABASE_ROADMAP.md):
*   `students`, `users`: Quản lý người dùng và vai trò.
*   `rooms`, `beds`, `buildings`: Cấu trúc phòng ở.
*   `applications`, `application_timeline`: Đơn đăng ký.
*   `bills`, `transactions`: Tài chính.
*   `face_profiles`, `access_logs`: An ninh và AI.

## 4. Tích hợp IoT (Đề xuất)
Để hỗ trợ tính năng theo dõi điện nước và mở khóa từ xa:
*   **MQTT Broker**: Sử dụng cho việc truyền tin thời gian thực giữa Backend và các thiết bị IoT (Cảm biến điện, khóa cửa vân tay).
*   **Device Shadow**: Lưu trữ trạng thái mới nhất của các thiết bị trong KTX.

## 5. Bảo mật & Hiệu năng
*   **Rate Limiting**: Giới hạn số lượng request để tránh tấn công DoS, đặc biệt là ở các API AI và Login.
*   **Audit Logging**: Lưu nhật ký các thao tác quan trọng (thanh toán, thay đổi thông tin hồ sơ).
*   **CDN**: Sử dụng CDN để phân phối ảnh đại diện của sinh viên nhằm tối ưu tốc độ tải trên Mobile.

---
**Định hướng**: Backend nên được xây dựng theo kiến trúc Microservices hoặc Modular Monolith để tương xứng với sự phân tách theo Feature của Mobile App.
