# 🏫 Smart Dormitory (SDMS) - Student Mobile App

![Platform](https://img.shields.io/badge/Platform-Android-brightgreen.svg)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)
![Architecture](https://img.shields.io/badge/Architecture-Feature--Based%20Clean%20Architecture-orange.svg)
![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-purple.svg)

**Smart Dormitory (SDMS)** là ứng dụng di động dành riêng cho sinh viên nội trú, đóng vai trò là cổng dịch vụ số toàn diện giúp quản lý đời sống ký túc xá một cách thông minh, minh bạch và an toàn.

---

## 🌟 Tính năng nổi bật

### 1. 🤖 Định danh & Ra vào bằng AI (Biometric Access)
*   **Face Registration:** Quy trình đăng ký khuôn mặt chuẩn hóa với công nghệ **Liveness Detection** (chống giả mạo bằng ảnh chụp/video) và **Quality Check** (đảm bảo độ sáng và góc chụp).
*   **Offline Access:** Trích xuất Face Embedding và lưu trữ mã hóa cục bộ, cho phép xác thực ra vào ngay cả khi mất kết nối máy chủ.

### 2. 🏠 Quản lý lưu trú & Chỗ ở
*   **Room Dashboard:** Xem chi tiết thông tin Tòa, Tầng, Số phòng và Vị trí giường theo thời gian thực.
*   **Application Tracking:** Theo dõi tiến độ duyệt đơn đăng ký nội trú thông qua sơ đồ Timeline trực quan.
*   **Stay Extension:** Gửi yêu cầu gia hạn hợp đồng lưu trú trực tiếp trên ứng dụng.

### 3. 💳 Tài chính & Thanh toán tiện lợi
*   **Digital Invoices:** Quản lý danh sách hóa đơn điện, nước, phòng và phí dịch vụ.
*   **VietQR Integration:** Tự động tạo mã QR thanh toán động với đầy đủ số tiền và nội dung chuyển khoản, giúp giảm thiểu sai sót thủ công.

### 4. 🔄 Đồng bộ hóa ngoại tuyến (Offline Sync)
*   Sử dụng **WorkManager** để tự động đẩy các yêu cầu cập nhật (Profile, Thanh toán, Face) lên Server ngay khi thiết bị có kết nối internet trở lại.

---

## 🏗️ Kiến trúc hệ thống (Architecture)

Dự án áp dụng mô hình **Feature-Based Clean Architecture** (Kiến trúc sạch theo tính năng) - tiêu chuẩn cao nhất trong phát triển ứng dụng Android hiện đại.

### Các tầng logic:
*   **Presentation Layer:** Sử dụng **MVVM + MVI-lite** (Contract-driven). Mỗi màn hình quản lý trạng thái thông qua `UiState` và tương tác qua `UiEvent`.
*   **Domain Layer:** Chứa các **Pure Kotlin UseCases** độc lập, đóng gói hoàn toàn quy tắc nghiệp vụ (Business Logic).
*   **Data Layer:** Triển khai **Repository Pattern**. Quản lý dữ liệu từ Remote (Retrofit) và Local (Room DB) một cách minh bạch.

### Cấu trúc thư mục chuẩn:
```text
com.ktx.dormitory/
├── ai/                 # Liveness, Quality Check, FaceNet Core
├── core/               # Mạng, Tiện ích, Interceptors, Sync
├── data/               # Tầng dữ liệu (Chia theo Feature)
│   ├── auth/           # DTO, Mapper, Repo implementation
│   ├── profile/
│   └── ...
├── domain/             # Tầng nghiệp vụ (Chia theo Feature)
│   ├── auth/           # UseCases, Model, Repository Interface
│   └── ...
├── presentation/       # Tầng giao diện (Chia theo Feature)
│   ├── features/
│   │   ├── face/       # Toàn bộ UI logic của tính năng Face
│   │   ├── home/
│   │   └── ...
│   └── theme/          # Cấu hình Material Design 3
└── navigation/         # Quản lý luồng chuyển màn hình
```

---

## 🛠️ Tech Stack

*   **UI:** Jetpack Compose, Material Design 3.
*   **Async/Multithreading:** Kotlin Coroutines & Flow.
*   **DI:** Hilt (Dagger).
*   **Network:** Retrofit 2, OkHttp 4, Gson.
*   **Local DB:** Room Database, EncryptedSharedPreferences (Bảo mật token).
*   **AI/Vision:** ML Kit Face Detection, CameraX, FaceNet (TFLite).
*   **Image Loading:** Coil.

---

## 📂 Tài liệu kỹ thuật chi tiết

Các báo cáo kiểm toán và đặc tả hệ thống được lưu tại thư mục `docs/`:

1.  [**Kiểm toán kiến trúc cuối cùng**](./docs/FINAL_ARCHITECTURE_AUDIT.md)
2.  [**Báo cáo tính năng chi tiết**](./docs/DETAILED_REPORT.md)
3.  [**Đặc tả API Backend (OpenAPI)**](./docs/API_SPEC.yaml)
4.  [**Lộ trình tích hợp & Mở rộng**](./docs/MIGRATION_PLAN.md)
5.  [**Danh mục kiểm thử & Demo**](./docs/QA_CHECKLIST.md)

---

## 🚀 Hướng dẫn cài đặt

1.  **Clone dự án:** `git clone https://github.com/Vkbp/SmartDormitory-Android.git`
2.  **Mở bằng Android Studio:** (Khuyến nghị bản Ladybug trở lên).
3.  **Cấu hình API:** Thay đổi `BASE_URL` trong file `core/Constants.kt` trỏ về Backend IP của bạn.
4.  **Build & Run:** Chọn thiết bị Android (API 26+) và nhấn Run.

---

## 👨‍💻 Tác giả

Dự án được thực hiện phục vụ cho Luận văn tốt nghiệp tại trường Đại học.

*   **Role:** Mobile Lead / Software Architect.
*   **Status:** Hoàn thành giai đoạn tái cấu trúc, sẵn sàng triển khai thực tế.

---
*© 2026 Smart Dormitory Project - Built with ❤️ and Clean Architecture.*
