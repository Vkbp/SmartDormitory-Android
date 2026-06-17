# 🏢 Smart Dormitory Management System (Android Client)

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.10.01-green.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-orange.svg)](https://developer.android.com/topic/architecture)

**SmartDormitory** là ứng dụng di động hiện đại dành cho sinh viên và quản lý kí túc xá. Dự án tập trung vào việc tự động hóa quy trình quản lý, tăng cường bảo mật thông qua AI và tối ưu hóa trải nghiệm người dùng bằng công nghệ Android mới nhất.

---

## 🚀 Tính năng nổi bật

### 🔐 Bảo mật & Xác thực
*   **Xác thực đa lớp:** Đăng nhập qua JWT (AccessToken & RefreshToken) kết hợp vân tay/khuôn mặt (**Biometric Authentication**).
*   **Bảo mật dữ liệu:** Mã hóa thông tin nhạy cảm và Face Embeddings bằng **Android Keystore** và **AES/GCM Encryption**.

### 🤖 Smart Access (Điểm nhấn kỹ thuật)
*   **Offline Face ID:** Nhận diện khuôn mặt ngay cả khi không có mạng sử dụng model AI **MobileFaceNet** tích hợp qua **TensorFlow Lite**.
*   **Liveness Detection:** Chống giả mạo bằng ảnh/video thông qua kiểm tra nháy mắt, quay đầu và mỉm cười thời gian thực.
*   **QR Access:** Quét mã QR động để ra vào cổng nhanh chóng.

### 💰 Tài chính & Hóa đơn
*   **Quản lý hóa đơn:** Theo dõi tiền phòng, điện, nước trực quan.
*   **Thanh toán VietQR:** Tích hợp thanh toán nhanh qua mã QR ngân hàng.

### 📋 Quản lý & Vận hành
*   **Staff Approval:** Phân hệ dành cho nhân viên duyệt yêu cầu sửa chữa, hỗ trợ của sinh viên ngay trên App.
*   **Profile Management:** Quản lý hồ sơ sinh viên chi tiết, đồng bộ hóa dữ liệu với Backend theo chuẩn RESTful.

---

## 🛠 Công nghệ sử dụng

*   **Language:** 100% Kotlin.
*   **UI Framework:** Jetpack Compose (Declarative UI).
*   **Asynchronous:** Coroutines & Flow (State Management).
*   **Dependency Injection:** Hilt (Dagger).
*   **Networking:** Retrofit 2 & OkHttp 4 (Interceptor, Authenticator).
*   **Local Database:** Room Persistence, DataStore (Preferences).
*   **AI/Vision:** ML Kit (Face Detection), CameraX, TensorFlow Lite Support.
*   **Image Loading:** Coil.

---

## 🏗 Kiến trúc dự án

Dự án tuân thủ nghiêm ngặt **Clean Architecture** chia làm 3 tầng:

1.  **Domain Layer:** Chứa Business Logic, Entities và Repository Interfaces. Hoàn toàn độc lập với Framework.
2.  **Data Layer:** Triển khai Repository, gọi API, xử lý Database Room và mã hóa dữ liệu.
3.  **Presentation Layer:** Sử dụng mô hình **MVVM**. UI được xây dựng bằng Jetpack Compose, lắng nghe trạng thái từ `StateFlow`.

---

## 📸 Demo & Screenshots

Dự án hỗ trợ 3 vai trò người dùng chính:
1. **Sinh viên:** Đăng ký nội trú, quét mặt ra vào, gửi yêu cầu sửa chữa, thanh toán hóa đơn.
2. **Nhân viên (Staff):** Phê duyệt yêu cầu sinh viên, mở cửa cổng từ xa.
3. **Quản trị viên (Admin):** Quản lý hệ thống tổng thể.

---

## ⚙️ Cài đặt & Chạy thử

### 1. Yêu cầu hệ thống
*   Android Studio Ladybug (hoặc mới hơn).
*   JDK 17.
*   Thiết bị Android 7.0 (API 24) trở lên.

### 2. Cấu hình
Tạo file `local.properties` ở thư mục gốc và thêm địa chỉ IP Backend của bạn:
`BASE_URL=http://<your-ip-address>:8080/api/`

### 3. Build & Run
1.  Clone repository: `git clone https://github.com/Vkbp/SmartDormitory-Android.git`
2.  Mở dự án trong Android Studio.
3.  Nhấn **Sync Project with Gradle Files**.
4.  Nhấn **Run** để cài đặt lên thiết bị.

---

## 📄 Tài liệu API
Chi tiết về các Endpoint, Request và Response được mô tả tại: [API_DOCUMENTATION.md](API_DOCUMENTATION.md)

---

## 📜 Giấy phép
Dự án được phát hành cho mục đích đồ án tốt nghiệp.

---
**Developed with ❤️ by Android Team**
