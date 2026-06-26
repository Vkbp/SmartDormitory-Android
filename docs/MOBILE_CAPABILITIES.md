# MOBILE APP CAPABILITIES - NHỮNG GÌ MOBILE ĐÃ LÀM ĐƯỢC

Tài liệu này tổng hợp toàn bộ năng lực kỹ thuật và nghiệp vụ hiện tại của ứng dụng di động Smart Dormitory.

## 1. Năng lực AI On-Device (AI tại biên)
Ứng dụng không chỉ gửi ảnh lên server mà còn tự xử lý thông minh ngay trên điện thoại:
*   **Face Detection**: Nhận diện vị trí khuôn mặt trong khung hình với độ trễ cực thấp (< 50ms).
*   **Quality Check**: Đảm bảo ảnh đủ độ sáng, khuôn mặt không bị che khuất và độ phân giải đạt chuẩn trước khi đăng ký.
*   **Liveness Detection**: Yêu cầu người dùng thực hiện các hành động (chớp mắt, quay đầu) để chống giả mạo bằng ảnh chụp hoặc video.
*   **Feature Extraction**: Chuyển đổi khuôn mặt thành vector đặc trưng (Embedding) 128/512 chiều bằng model MobileFaceNet.

## 2. Hoạt động Ngoại tuyến & Đồng bộ (Offline First)
Ứng dụng được thiết kế để hoạt động trong môi trường mạng không ổn định:
*   **Local Caching**: Toàn bộ dữ liệu Profile, Room, Invoices được lưu tại SQLite (Room) để truy cập tức thì.
*   **Pending Actions**: Khi sinh viên thực hiện thanh toán hoặc đăng ký khuôn mặt lúc mất mạng, ứng dụng sẽ lưu vào hàng đợi `PendingSync`.
*   **Auto Sync**: WorkManager sẽ tự động chạy ngầm để đẩy dữ liệu lên server ngay khi phát hiện có kết nối Internet trở lại.

## 3. Hệ thống Thanh toán & Xác thực
*   **Biometric Authentication**: Hỗ trợ vân tay/khuôn mặt (hệ thống) để đăng nhập nhanh mà không cần nhập mật khẩu.
*   **VietQR Integration**: Tự động tạo mã QR chứa thông tin số tiền và nội dung chuyển khoản theo chuẩn Napas.
*   **Idempotency**: Cơ chế đảm bảo một hành động (như thanh toán) không bị thực hiện lặp lại nhiều lần do lỗi mạng.

## 4. Giao diện & Trải nghiệm Người dùng (UX/UI)
*   **Declarative UI**: Xây dựng hoàn toàn bằng Jetpack Compose, mang lại hiệu ứng mượt mà và code sạch.
*   **State-Driven UI**: Giao diện phản ứng chính xác theo trạng thái dữ liệu (Loading, Error, Success).
*   **Modern Navigation**: Luồng chuyển màn hình mượt mà, hỗ trợ deep linking và guard theo role.

## 5. Danh sách Năng lực Nghiệp vụ (Business Capabilities)
1.  **Quản lý Tài khoản**: Đăng nhập, đổi mật khẩu, quên mật khẩu qua email.
2.  **Hồ sơ Sinh viên**: Quản lý thông tin liên lạc, địa chỉ, ảnh đại diện.
3.  **Thông tin Lưu trú**: Xem chi tiết phòng, giường và danh sách bạn cùng phòng.
4.  **Theo dõi Đăng ký**: Xem tiến độ phê duyệt đơn đăng ký ở KTX qua timeline.
5.  **Tài chính**: Theo dõi các hóa đơn điện nước, tiền phòng và lịch sử giao dịch.
6.  **An ninh**: Đăng ký nhận diện khuôn mặt và xem nhật ký ra vào các cổng/phòng.
7.  **Yêu cầu**: Gửi yêu cầu gia hạn thời gian ở lại KTX.

---
**Đánh giá Demo**: Ứng dụng đã sẵn sàng 100% để thực hiện demo các luồng nghiệp vụ thực tế từ lúc sinh viên đăng nhập đến khi hoàn tất các thủ tục tại KTX.
