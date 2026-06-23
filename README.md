# 🏢 Smart Dormitory Management System (Android Client)

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.10.01-green.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-orange.svg)](https://developer.android.com/topic/architecture)

**SmartDormitory** là ứng dụng di động Android hiện đại dành cho sinh viên và ban quản lý ký túc xá đại học. Dự án tập trung vào việc tự động hóa quy trình quản lý hành chính, nâng cao an ninh thông qua công nghệ trí tuệ nhân tạo (AI Face ID) và mang lại trải nghiệm tiện ích tốt nhất cho sinh viên thông qua tích hợp cổng thanh toán trực tuyến và chế độ hoạt động ngoại tuyến linh hoạt.

---

## 📂 Thư mục tài liệu đồ án (Documentation)

Mọi tài liệu chi tiết phục vụ cho viết báo cáo tốt nghiệp hoặc slide bảo vệ đồ án đã được tổng hợp đầy đủ trong thư mục [doc/](docs):

*   📄 **Báo cáo tổng kết đồ án (Khuyên đọc):** [bao_cao_tong_ket_smart_dormitory.md](docs/bao_cao_tong_ket_smart_dormitory.md) - Báo cáo hoàn chỉnh 9 phần, viết bằng ngôn ngữ nghiệp vụ thực tế dễ hiểu dành cho giảng viên đại học và người không biết code.
*   📝 **Tài liệu đặc tả API Backend:** [API_DOCUMENTATION.md](docs/API_DOCUMENTATION.md) - Đặc tả chi tiết các cổng API trao đổi dữ liệu.
*   ⚙️ **Sơ đồ cấu trúc mã nguồn:** [structure.txt](docs/structure.txt) - Tổng quan cây thư mục dự án Android.
*   🧠 **Định hướng phát triển AI:** [dinhhuongAI](docs/dinhhuongAI) - Định hướng tích hợp trí tuệ nhân tạo lâu dài cho ký túc xá thông minh.

---

## 🚀 Các tính năng nổi bật của dự án

### 🔐 1. Xác thực đa lớp & Bảo mật Sinh trắc học
*   **Đăng nhập bằng vân tay/khuôn mặt:** Tích hợp trực tiếp với bộ cảm biến bảo mật có sẵn của điện thoại thông qua thư viện Biometric giúp người dùng truy cập an toàn chỉ trong 1 chạm.
*   **Quản lý phiên thông minh:** Sử dụng cơ chế tự động gia hạn phiên đăng nhập ngầm trong nền mà không yêu cầu người dùng đăng nhập lại thường xuyên.

### 🤖 2. Smart Access & Nhận diện AI Offline
*   **Nhận diện khuôn mặt ngoại tuyến (Offline Face ID):** Tích hợp mô hình AI **MobileFaceNet** chạy trực tiếp trên thiết bị bằng **TensorFlow Lite**, giúp xác thực ra vào cửa tự động mà không cần kết nối mạng.
*   **Chống giả mạo người thật (Liveness Detection):** Quy trình đăng ký khuôn mặt 5 bước động (nháy mắt, quay đầu sang trái, quay đầu sang phải, mỉm cười) ngăn chặn việc gian lận bằng ảnh chụp hay video quay sẵn.
*   **Mã hóa sinh học cấp phần cứng:** Toàn bộ thông tin vector khuôn mặt được mã hóa bằng thuật toán AES/GCM bảo mật và quản lý thông qua phân vùng bảo mật phần cứng **Android Keystore**.

### 💰 3. Tài chính & Thanh toán VietQR
*   **Mã VietQR động tự động:** Ứng dụng tự động tính toán dư nợ tiền phòng, điện, nước để tạo mã QR ngân hàng chứa chính xác số tiền và nội dung chuyển khoản, giúp sinh viên quét thanh toán tức thì và ban quản lý đối soát tự động.

### 📶 4. Hoạt động ngoại tuyến & Đồng bộ hóa tự động
*   **Offline Mode:** Cơ sở dữ liệu cục bộ (Room DB) lưu trữ toàn bộ dữ liệu cấu hình, giúp sinh viên xem thông tin phòng, hóa đơn và viết đơn báo hỏng ngay cả khi mất kết nối mạng.
*   **Background Sync:** Mọi dữ liệu thao tác offline sẽ được đưa vào hàng đợi lưu trữ tạm và tự động đẩy lên máy chủ qua dịch vụ ngầm khi điện thoại kết nối mạng trở lại.

---

## 🏗 Kiến trúc dự án (Clean Architecture)

Dự án áp dụng cấu trúc 3 tầng chuẩn hóa độc lập giúp tăng khả năng bảo trì và nâng cấp:

1.  **Domain Layer:** Chứa Business Logic (Quy tắc nghiệp vụ), các thực thể (Entities) và giao diện điều phối (Repository Interfaces). Tầng này hoàn toàn độc lập với các thư viện bên thứ ba.
2.  **Data Layer:** Triển khai các giao tiếp dữ liệu thực tế (Repository Implementation), kết nối mạng qua Retrofit, xử lý lưu trữ Room Database và thực hiện mã hóa dữ liệu.
3.  **Presentation Layer (MVVM + Jetpack Compose):** Xây dựng giao diện hiển thị dạng khai báo (Declarative UI) dựa trên Jetpack Compose và quản lý trạng thái thông tin qua luồng dữ liệu `StateFlow`.

---

## 🛠 Công nghệ ứng dụng

*   **Ngôn ngữ:** 100% Kotlin
*   **UI Framework:** Jetpack Compose (Material Design 3)
*   **Bơm phụ thuộc (DI):** Hilt (Dagger)
*   **Xử lý bất đồng bộ:** Coroutines & Flow (State Management)
*   **Kết nối mạng:** Retrofit 2 & OkHttp 4 (Interceptor, Authenticator)
*   **Cơ sở dữ liệu cục bộ:** Room Persistence, DataStore (Preferences)
*   **Xử lý hình ảnh & AI:** ML Kit (Face Detection), CameraX, TensorFlow Lite Support
*   **Tải ảnh:** Coil

---

## ⚙️ Hướng dẫn cài đặt và chạy thử

### 1. Yêu cầu hệ thống
*   Android Studio Ladybug (hoặc mới hơn)
*   Java Development Kit (JDK) 17 trở lên
*   Thiết bị Android chạy hệ điều hành từ Android 7.0 (API 24) trở lên có hỗ trợ camera và vân tay (để kiểm thử các tính năng AI & Biometrics)

### 2. Cấu hình ban đầu
Tạo tệp `local.properties` nằm tại thư mục gốc của dự án và thêm địa chỉ IP máy chủ Backend của bạn:
```properties
BASE_URL=http://<ip-address-cua-ban>:8080/api/
```

### 3. Khởi chạy dự án
1.  Mở dự án trong phần mềm **Android Studio**.
2.  Nhấn nút **Sync Project with Gradle Files** để tải các thư viện đi kèm.
3.  Chạy ứng dụng lên thiết bị ảo hoặc thiết bị thật bằng cách nhấn nút **Run** (biểu tượng tam giác xanh).

---
**Đồ án tốt nghiệp được phát triển bởi Android Team - Kỷ nguyên Smart Campus 🎓**
