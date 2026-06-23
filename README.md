# 🏢 Smart Dormitory Management System (Android Client)

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.10.01-green.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-orange.svg)](https://developer.android.com/topic/architecture)

**SmartDormitory** là ứng dụng di động Android hiện đại dành cho sinh viên nội trú. Dự án tập trung vào việc số hóa các dịch vụ ký túc xá, nâng cao an ninh thông qua công nghệ nhận diện khuôn mặt AI và tối ưu hóa trải nghiệm sinh viên với khả năng hoạt động ngoại tuyến linh hoạt.

---

## 🚀 Các tính năng cốt lõi

### 🤖 1. AI Face ID & Security
*   **Đăng ký khuôn mặt Hybrid:** Thu thập ảnh khuôn mặt trên thiết bị, kiểm tra chất lượng và **Liveness Detection** (nháy mắt, quay đầu) để chống giả mạo.
*   **Offline Verification:** Sử dụng model **MobileFaceNet** (TensorFlow Lite) để trích xuất đặc trưng khuôn mặt (192-d embedding), cho phép xác thực ra vào ngay cả khi không có kết nối mạng.
*   **Bảo mật sinh trắc học:** Tích hợp Fingerprint/Face Unlock để bảo vệ quyền truy cập ứng dụng.

### 💰 2. Quản lý Tài chính & Thanh toán
*   **Thanh toán VietQR:** Tự động tạo mã QR động chứa thông tin hóa đơn điện, nước, tiền phòng.
*   **Lịch sử giao dịch:** Theo dõi chi tiết các hóa đơn đã thanh toán và trạng thái nợ phí thời gian thực.

### 🏠 3. Dịch vụ Sinh viên
*   **Hồ sơ & Phòng:** Quản lý thông tin cá nhân, xem thông tin phòng hiện tại và danh sách bạn cùng phòng.
*   **Tra cứu đơn đăng ký:** Kiểm tra tiến độ duyệt đơn ở ký túc xá thông qua mã CCCD.
*   **Lịch sử ra vào:** Theo dõi nhật ký quét mặt tại các cổng ký túc xá.

### 📶 4. Kiến trúc Offline-First
*   **Local Caching:** Sử dụng **Room Database** để lưu trữ toàn bộ dữ liệu nghiệp vụ, đảm bảo ứng dụng luôn sẵn sàng.
*   **Smart Sync:** Tự động đồng bộ các hành động (cập nhật hồ sơ, thanh toán, đăng ký mặt) lên máy chủ qua **WorkManager** khi có kết nối mạng trở lại.

---

## 🏗 Kiến trúc hệ thống (Clean Architecture)

Dự án tuân thủ mô hình 3 lớp chuẩn hóa:
1.  **Presentation Layer:** Sử dụng **Jetpack Compose** cho UI khai báo và **StateFlow** để quản lý trạng thái trong ViewModel.
2.  **Domain Layer:** Chứa Logic nghiệp vụ thuần túy, Use Cases và Interfaces của Repository.
3.  **Data Layer:** Triển khai Repository, xử lý API (Retrofit) và Local Database (Room).

---

## 🛠 Công nghệ ứng dụng

*   **UI:** Jetpack Compose (Material Design 3)
*   **DI:** Hilt (Dagger)
*   **Async:** Coroutines & Flow
*   **Network:** Retrofit 2, OkHttp 4 (Interceptor, Authenticator)
*   **Database:** Room, DataStore
*   **AI/ML:** Google ML Kit, TensorFlow Lite, CameraX
*   **Image Loading:** Coil

---

## 📂 Tài liệu chi tiết

Mọi thông tin chi tiết về kỹ thuật và nghiệp vụ được tổ chức trong thư mục `docs/`:

*   📄 **Tài liệu kỹ thuật tổng thể (Khuyên đọc):** [PROJECT_TECHNICAL_DOCUMENTATION.md](docs/PROJECT_TECHNICAL_DOCUMENTATION.md)
*   📝 **Báo cáo tổng kết đồ án:** [bao_cao_tong_ket_smart_dormitory.md](docs/bao_cao_tong_ket_smart_dormitory.md)
*   🔗 **Báo cáo tích hợp Backend:** [SMART_DORMITORY_MOBILE_BACKEND_INTEGRATION_REPORT.md](docs/SMART_DORMITORY_MOBILE_BACKEND_INTEGRATION_REPORT.md)

---

## ⚙️ Hướng dẫn cài đặt

1.  **Yêu cầu:** Android Studio Ladybug+, JDK 17+.
2.  **Cấu hình API:** Tạo file `local.properties` tại thư mục gốc:
    ```properties
    BASE_URL=http://<server-ip>:8080/api/
    ```
3.  **Build:** Sync Gradle và nhấn **Run** trên thiết bị hỗ trợ Camera & Biometrics.

---
**Dự án sẵn sàng cho DEMO - Phát triển bởi SmartDorm Team 🎓**
