# MOBILE ROADMAP - ĐỊNH HƯỚNG PHÁT TRIỂN ỨNG DỤNG DI ĐỘNG

Dựa trên mã nguồn hiện có, tài liệu này mô tả các bước tiếp theo để nâng cấp ứng dụng di động Smart Dormitory.

## 1. Tận dụng Mã nguồn Hiện tại
Kiến trúc **Feature-Based Clean Architecture** hiện tại cho phép thực hiện các nâng cấp sau với nỗ lực thấp nhất:
*   **Tái sử dụng Component**: Các thành phần như `FeatureCard`, `LoadingView`, `BaseResponse` đã có sẵn.
*   **Tái sử dụng AI Module**: Module `ai` đã hoàn thiện pipeline, có thể áp dụng cho các mục đích khác ngoài đăng ký (như điểm danh, thanh toán bằng khuôn mặt).
*   **Tái sử dụng Sync Module**: Hệ thống `PendingSync` có thể dễ dàng áp dụng cho tính năng "Báo hỏng" để sinh viên báo cáo ngay cả khi không có mạng.

## 2. Lộ trình Phát triển Tính năng (Mobile)

### Giai đoạn 1: Hoàn thiện & Tinh chỉnh (Optimization)
*   **Dark Mode**: Hoàn thiện bộ màu Material 3 cho chế độ ban đêm.
*   **Animations**: Thêm các hiệu ứng chuyển cảnh mượt mà giữa các màn hình dashboard.
*   **Biometric**: Tích hợp sâu hơn vân tay vào các hành động xác thực thanh toán.

### Giai đoạn 2: Phát triển Phân hệ mới (Extension)
*   **Module Notification**: Xây dựng màn hình danh sách thông báo và tích hợp FCM (Firebase).
*   **Module Maintenance**: Xây dựng Form báo hỏng thiết bị (chụp ảnh, chọn loại hư hỏng).
*   **Module Visitor**: Xây dựng màn hình đăng ký khách và hiển thị QR code cho khách.

### Giai đoạn 3: Tích hợp Thông minh (Smart Integration)
*   **IoT Dashboard**: Xây dựng các widget hiển thị số dư điện nước dưới dạng biểu đồ (Line Chart/Bar Chart).
*   **AI Chatbot UI**: Giao diện hội thoại để sinh viên hỏi đáp nội quy.

## 3. Cấu trúc Cần thiết cho một Feature mới
Khi thêm một tính năng mới (ví dụ: `visitor`), cần tuân thủ cấu trúc hiện tại:
1.  `presentation/features/visitor`: Screen, ViewModel, Contract.
2.  `domain/visitor`: Model, UseCases, Repository Interface.
3.  `data/visitor`: DTO, Mapper, RemoteDataSource, Repository Implementation.
4.  `di`: Đăng ký Module trong Hilt.

## 4. Danh sách các màn hình đề xuất thêm mới
*   `NotificationListScreen`: Danh sách thông báo.
*   `MaintenanceReportScreen`: Form báo hỏng thiết bị.
*   `UtilityUsageScreen`: Biểu đồ sử dụng điện nước.
*   `VisitorRegistrationScreen`: Form đăng ký khách.
*   `SosOverlay`: Nút bấm khẩn cấp nổi trên màn hình chính.

---
**Kết luận**: Với nền tảng mã nguồn hiện tại, Mobile App đang ở trạng thái **"Sẵn sàng mở rộng"**. Việc thêm một module mới có thể thực hiện trong vòng 1-2 tuần làm việc.
