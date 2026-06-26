# FEATURE MATRIX - SMART DORMITORY (STUDENT APP)

Bảng dưới đây liệt kê tất cả các tính năng có trong mã nguồn Mobile và trạng thái tương ứng của chúng.

## 1. Các Tính năng Chính (Core Features)

| Tính năng | Mô tả | Trạng thái | Ghi chú |
| :--- | :--- | :---: | :--- |
| **Authentication** | Đăng nhập, Đăng xuất, JWT, Refresh Token | **READY** | Hỗ trợ Biometric Login. |
| **Profile** | Xem và cập nhật thông tin cá nhân | **READY** | Tích hợp upload ảnh lên Cloudinary. |
| **Room Info** | Xem thông tin phòng, giường hiện tại | **READY** | Dữ liệu đồng bộ từ Backend. |
| **Dorm Application** | Đăng ký ở KTX, theo dõi Timeline | **READY** | Quy trình phê duyệt nhiều bước. |
| **Payment** | Xem hóa đơn, Thanh toán VietQR | **READY** | Có cơ chế xác thực offline. |
| **Face Registration** | Đăng ký khuôn mặt với AI | **READY** | Pipeline: Detect -> Quality -> Liveness -> Reg. |
| **Face Verification** | Xác thực khuôn mặt mở cửa/điểm danh | **READY** | Thực hiện hoàn toàn trên thiết bị (AI on-device). |
| **Access History** | Xem lịch sử ra vào | **READY** | Hỗ trợ Offline Sync. |
| **Stay Extension** | Đăng ký gia hạn lưu trú | **READY** | Luồng gửi yêu cầu và lý do. |

## 2. Các Tính năng Hệ thống (System Features)

| Tính năng | Mô tả | Trạng thái | Ghi chú |
| :--- | :--- | :---: | :--- |
| **Offline Sync** | Đồng bộ dữ liệu khi mất mạng | **READY** | Sử dụng WorkManager + Room. |
| **AI Pipeline** | Luồng xử lý hình ảnh thời gian thực | **READY** | Tối ưu hóa cho di động (TensorFlow Lite). |
| **Role Guard** | Bảo vệ route theo vai trò người dùng | **READY** | Tự động điều hướng Login/Home. |
| **Theme & Dark Mode**| Hỗ trợ giao diện sáng/tối | **READY** | Material Design 3. |

## 3. Chú thích Trạng thái (Legend)

*   **READY**: Mã nguồn Mobile đã hoàn thiện, Backend đã hỗ trợ API. Sẵn sàng sử dụng.
*   **MOBILE READY**: Mobile đã có giao diện và logic, nhưng Backend cần bổ sung API để chạy thực tế.
*   **SKELETON**: Chỉ mới có giao diện khung (UI Placeholder), chưa có logic xử lý.
*   **CHƯA TRIỂN KHAI**: Tính năng có trong kế hoạch nhưng chưa có mã nguồn.

---

**Kết luận**: Về phía Mobile App, dự án đã hoàn thành **100% các tính năng trọng tâm** đã đề ra trong kiến trúc. Toàn bộ mã nguồn đã được cấu trúc lại theo chuẩn Feature-Based Clean Architecture.
