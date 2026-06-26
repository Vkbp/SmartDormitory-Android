# SOFTWARE ARCHITECTURE AUDIT - SMART DORMITORY (STUDENT APP)

## 1. Tổng quan Kiến trúc
Ứng dụng được xây dựng dựa trên các nguyên tắc của **Clean Architecture** kết hợp với cách tiếp cận **Feature-Based**. Điều này giúp mã nguồn cực kỳ tách bạch, dễ hiểu và dễ mở rộng.

### Các Tầng Kiến trúc (Layers)
*   **Data Layer**: Chứa các Implementation của Repository, Remote Data Source (Retrofit API), Local Data Source (Room DAO), DTOs và Mappers. Mỗi feature có gói `data` riêng.
*   **Domain Layer**: Chứa các Interface của Repository, Business Models và UseCases. Đây là tầng trung tâm, không phụ thuộc vào bất kỳ thư viện Android nào.
*   **Presentation Layer**: Sử dụng mô hình **MVVM (Model-View-ViewModel)** kết hợp với **MVI-lite (Contract)**. Tầng này bao gồm Screens (Jetpack Compose), ViewModels và UI State/Event/Effect contracts.

## 2. Công nghệ Sử dụng (Tech Stack)
*   **Ngôn ngữ**: Kotlin 100%.
*   **UI Framework**: Jetpack Compose (Modern Declarative UI).
*   **Dependency Injection**: Hilt (Dagger-based DI for Android).
*   **Networking**: Retrofit + OkHttp (với Interceptors cho Token, Retry, Idempotency).
*   **Local Database**: Room Persistence Library.
*   **Async/Concurrency**: Kotlin Coroutines + Flow.
*   **AI/ML**: ML Kit (Face Detection) + TensorFlow Lite (Face Embedding & Net).
*   **Offline Sync**: WorkManager + Room + PendingSync strategy.

## 3. Đánh giá Chi tiết

### 3.1. Clean Architecture & Feature-Based
*   **Ưu điểm**: Tính độc lập của feature cực cao. Việc thêm tính năng "Visitor" hay "Laundry" chỉ đơn giản là thêm một package mới mà không ảnh hưởng đến "Auth" hay "Payment".
*   **Thực thi**: Đã tách biệt hoàn toàn `UserRepository` thành các Repository chuyên biệt (`Profile`, `Room`, `Application`).

### 3.2. MVVM + MVI-lite
*   **Ưu điểm**: Quản lý trạng thái giao diện (UI State) nhất quán thông qua `Contract`. Giảm thiểu side-effects và lỗi UI.
*   **Thực thi**: Tất cả các ViewModels đều sử dụng `SavedStateHandle` để bảo vệ dữ liệu khi process bị kill và sử dụng `StateFlow` để phát dữ liệu.

### 3.3. AI Pipeline
*   **Ưu điểm**: Pipeline xử lý khuôn mặt được module hóa tốt trong package `ai`. Hỗ trợ kiểm tra Liveness (chớp mắt, quay đầu) trực tiếp trên thiết bị để chống giả mạo.
*   **Thực thi**: Tích hợp mượt mà với CameraX và ML Kit.

### 3.4. Offline Sync Strategy
*   **Ưu điểm**: Sử dụng bảng `PendingSync` để lưu các action khi mất mạng và `SyncWorker` để tự động thực hiện lại khi có mạng.
*   **Thực thi**: Đã áp dụng cho các hành động quan trọng như thanh toán, đăng ký khuôn mặt.

## 4. Điểm Đánh giá (Score Card)

| Hạng mục | Điểm (10) | Nhận xét |
| :--- | :---: | :--- |
| **Cấu trúc Package** | 10 | Chuẩn Feature-Based, cực kỳ ngăn nắp. |
| **Tính Cô lập (Isolation)** | 9.5 | Các feature hầu như không phụ thuộc chéo. |
| **Thiết kế Domain** | 9.0 | UseCase bao phủ toàn bộ logic, dễ unit test. |
| **Tầng Dữ liệu** | 10 | Tách biệt DTO, Entity và Mapper rõ ràng. |
| **Giao diện (Compose)** | 9.5 | Code UI sạch, tái sử dụng component tốt. |
| **Quản lý DI (Hilt)** | 10 | Scoping hợp lý, module hóa DI theo tầng. |
| **Khả năng Mở rộng** | 10 | Sẵn sàng cho quy mô lớn. |
| **Khả năng Bảo trì** | 10 | Dễ dàng định vị lỗi nhờ cấu trúc feature. |

## 5. Kết luận
Kiến trúc của ứng dụng hiện đang ở mức **Professional**. Nó không chỉ đáp ứng tốt yêu cầu của một đồ án tốt nghiệp mà còn hoàn toàn đủ tiêu chuẩn để chuyển đổi thành sản phẩm thực tế (Production-Ready) với chi phí bảo trì thấp nhất.
