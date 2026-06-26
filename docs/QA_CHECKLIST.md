# QA CHECKLIST - DANH MỤC KIỂM TRA CHẤT LƯỢNG

Tài liệu này dùng để kiểm tra độ hoàn thiện của các tính năng trước khi thực hiện Demo hoặc bàn giao.

## 1. Kiểm tra Tính năng (Functional Testing)

### 🔑 Authentication
- [ ] Đăng nhập thành công với MSSV hợp lệ.
- [ ] Báo lỗi khi sai mật khẩu hoặc tài khoản không tồn tại.
- [ ] Token được tự động làm mới (Refresh Token) khi hết hạn.
- [ ] Đăng nhập bằng Biometric (Vân tay/Khuôn mặt hệ thống) hoạt động.

### 👤 Profile & Room
- [ ] Hiển thị đúng thông tin cá nhân từ server.
- [ ] Cập nhật email/số điện thoại thành công.
- [ ] Tải ảnh đại diện lên Cloudinary thành công.
- [ ] Xem được thông tin phòng và danh sách bạn cùng phòng.

### 🤖 AI Face Management
- [ ] Face Detection bắt được khuôn mặt trong các điều kiện ánh sáng khác nhau.
- [ ] Quy trình Liveness yêu cầu chớp mắt và quay đầu hoạt động chính xác.
- [ ] Không cho phép đăng ký nếu chất lượng ảnh thấp hoặc bị che khuất.
- [ ] Đăng ký vector khuôn mặt lên server thành công.

### 💳 Payment & Invoices
- [ ] Hiển thị danh sách hóa đơn chưa thanh toán.
- [ ] Mã QR sinh ra đúng thông tin số tiền và nội dung.
- [ ] Lịch sử giao dịch được cập nhật sau khi thanh toán.

### 📡 Offline & Sync
- [ ] Vẫn vào được app và xem dữ liệu đã cache khi tắt mạng.
- [ ] Hành động đăng ký/thanh toán được lưu vào `PendingSync` khi offline.
- [ ] Dữ liệu tự động đẩy lên server ngay khi có mạng trở lại.

## 2. Kiểm tra Hiệu năng & UI (Performance & UI)
- [ ] Không có hiện tượng giật lag (jank) khi cuộn danh sách hóa đơn hoặc lịch sử.
- [ ] App khởi động nhanh (Splash screen không hiển thị quá 2 giây).
- [ ] Màu sắc và Font chữ đồng nhất theo Material 3.
- [ ] Kiểm tra hiển thị đúng trên các kích thước màn hình khác nhau.

## 3. Kiểm tra Bảo mật (Security)
- [ ] Token được lưu trữ an toàn (EncryptedSharedPreferences).
- [ ] Thông tin nhạy cảm không bị log ra console ở bản Release.
- [ ] SSL Pinning được cấu hình (nếu cần thiết cho môi trường thực tế).

---
**Ghi chú**: Mọi lỗi (bug) phát hiện trong quá trình test cần được ghi nhật ký và phân loại mức độ (Critical, Major, Minor).
