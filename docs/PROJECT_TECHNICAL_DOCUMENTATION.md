# PROJECT TECHNICAL DOCUMENTATION - SMART DORMITORY (MOBILE)

## 1. Tổng quan hệ thống
*   **Tên dự án:** Smart Dormitory (Mobile App)
*   **Mục tiêu dự án:** Cung cấp ứng dụng di động cho sinh viên nội trú quản lý thông tin cá nhân, thanh toán hóa đơn, đăng ký nhận diện khuôn mặt và theo dõi lịch sử ra vào.
*   **Kiến trúc tổng thể:** Clean Architecture (Data - Domain - Presentation).
*   **Công nghệ sử dụng:**
    *   **Ngôn ngữ:** Kotlin
    *   **UI Framework:** Jetpack Compose
    *   **Dependency Injection:** Hilt
    *   **Database:** Room (Local Cache), DataStore (Settings/Preferences)
    *   **Networking:** Retrofit, OkHttp
    *   **AI/ML:** Google ML Kit (Face Detection), TensorFlow Lite (MobileFaceNet cho Embedding)
    *   **Async Processing:** Coroutines, Flow, WorkManager (Offline Sync)

## 2. Cấu trúc mã nguồn
```text
app/src/main/java/com/ktx/dormitory/
 ├── ai/                # Xử lý AI: FaceNet, Liveness, Quality
 ├── core/              # Thành phần cốt lõi: Network, Sync, Utils
 ├── data/              # Lớp dữ liệu: DTOs, Local, Remote, Repositories
 ├── di/                # Dependency Injection Modules
 ├── domain/            # Lớp nghiệp vụ: Models, Repositories (Interfaces), UseCases
 ├── navigation/        # Cấu hình điều hướng (Compose Navigation)
 ├── presentation/      # Lớp hiển thị: Screens, ViewModels, Components, Theme
 ├── HomeScreen.kt      # Màn hình chính
 ├── MainActivity.kt    # Entry point duy nhất
 └── SmartDormApplication.kt
```

## 3. Danh sách chức năng
### 3.1. Xác thực (Authentication)
*   **Mục đích:** Đăng nhập, Đăng xuất, Đổi mật khẩu, Quên mật khẩu.
*   **Màn hình:** `LoginScreen`, `ChangePasswordScreen`, `SplashScreen`.
*   **ViewModel:** `LoginViewModel`.
*   **Repository:** `AuthRepositoryImpl`.
*   **API:** `AuthApiService` (`v1/auth/login`, `v1/auth/logout`, `v1/auth/change-password`).

### 3.2. Hồ sơ sinh viên (Profile)
*   **Mục đích:** Xem thông tin cá nhân chi tiết từ backend.
*   **Màn hình:** `ProfileScreen`.
*   **ViewModel:** `ProfileViewModel`.
*   **Repository:** `UserRepositoryImpl`.
*   **API:** `UserApiService` (`v1/students/me`).

### 3.3. Thông tin phòng (Room Information)
*   **Mục đích:** Xem thông tin phòng hiện tại, bạn cùng phòng.
*   **Màn hình:** `RoomScreen`.
*   **ViewModel:** `RoomViewModel`.
*   **Repository:** `UserRepositoryImpl`.
*   **API:** `UserApiService` (`v1/student/room/current`).

### 3.4. Thanh toán hóa đơn (Bills & Payment)
*   **Mục đích:** Xem danh sách hóa đơn chưa thanh toán và thực hiện thanh toán online qua VietQR.
*   **Màn hình:** `PaymentScreen`, `PaymentHistoryScreen`.
*   **ViewModel:** `PaymentViewModel`, `PaymentHistoryViewModel`.
*   **Repository:** `PaymentRepositoryImpl`.
*   **API:** `PaymentApiService` (`v1/bills`, `payments/online`).

### 3.5. Đăng ký khuôn mặt (Face Registration)
*   **Mục đích:** Thu thập dữ liệu khuôn mặt, kiểm tra chất lượng và liveness, sau đó gửi ảnh lên server.
*   **Màn hình:** `FaceRegistrationScreen`, `FaceDetectionScreen`.
*   **ViewModel:** `FaceViewModel`.
*   **Repository:** `FaceRepositoryImpl`.
*   **API:** `AccessApiService` (`v1/students/me/face`).

### 3.6. Lịch sử ra vào (Access History)
*   **Mục đích:** Xem lịch sử quét mặt ra vào ký túc xá.
*   **Màn hình:** `AccessHistoryScreen`.
*   **ViewModel:** `AccessViewModel`.
*   **Repository:** `AccessRepositoryImpl`.
*   **API:** `AccessApiService` (`v1/access/history/student/{id}`).

### 3.7. Trạng thái đơn (Application Status)
*   **Mục đích:** Tra cứu tiến độ đơn đăng ký ở ký túc xá qua CCCD.
*   **Màn hình:** `ApplicationStatusScreen`.
*   **ViewModel:** `ApplicationViewModel`.
*   **Repository:** `UserRepositoryImpl`.
*   **API:** `UserApiService` (`v1/applications/status`).

## 4. Kiến trúc hệ thống
Hệ thống tuân thủ **Clean Architecture**:
1.  **UI (Compose):** Màn hình và Components.
2.  **ViewModel:** Quản lý State (UI State) và gọi UseCase.
3.  **Domain:** Chứa Logic nghiệp vụ (UseCase), Model nghiệp vụ và Interface của Repository.
4.  **Data:** Implement Repository, xử lý Remote (Retrofit) và Local (Room).

**Luồng dữ liệu:**
`UI` ↔ `ViewModel` ↔ `UseCase` ↔ `Repository` ↔ `RemoteDataSource / LocalDataSource`

## 5. API Documentation
Tất cả API sử dụng `BaseResponse<T>` làm chuẩn đóng gói.

| API | Method | Endpoint | File |
| --- | --- | --- | --- |
| Login | POST | `v1/auth/login` | `AuthApiService` |
| Get Current User | GET | `v1/users/me` | `AuthApiService` |
| Refresh Token | POST | `v1/auth/refresh-token` | `AuthApiService` |
| Logout | POST | `v1/auth/logout` | `AuthApiService` |
| Change Password | POST | `v1/auth/change-password` | `AuthApiService` |
| Get Profile | GET | `v1/students/me` | `UserApiService` |
| Update Profile | PATCH | `v1/students/me` | `UserApiService` |
| Upload Avatar | POST | `v1/uploads/avatar` | `UserApiService` |
| Get Room Info | GET | `v1/student/room/current` | `UserApiService` |
| Get App Status | GET | `v1/applications/status` | `UserApiService` |
| Get Bills | GET | `v1/bills` | `PaymentApiService` |
| Verify Payment | POST | `payments/online` | `PaymentApiService` |
| Register Face | POST | `v1/students/me/face` | `AccessApiService` |
| Access History | GET | `v1/access/history/student/{id}` | `AccessApiService` |

## 6. Database
Sử dụng Room Database (`AppDatabase`).

| Entity | Table Name | Chức năng |
| --- | --- | --- |
| `UserProfileEntity` | `user_profiles` | Lưu thông tin profile sinh viên offline |
| `InvoiceEntity` | `invoices` | Cache danh sách hóa đơn |
| `AccessLogEntity` | `access_logs` | Cache lịch sử ra vào |
| `FaceEntity` | `registered_faces` | Lưu vector khuôn mặt (đã mã hóa) để xác thực offline |
| `PendingSyncEntity` | `pending_sync` | Hàng đợi các hành động cần đồng bộ khi có mạng |

## 7. Offline First
Kiến trúc Offline First đảm bảo ứng dụng vẫn hoạt động khi không có mạng.
1.  **Dữ liệu đọc:** Luôn ưu tiên hiển thị từ Room DB, sau đó cập nhật từ Network.
2.  **Dữ liệu ghi:** Nếu mất mạng, hành động sẽ được lưu vào `PendingSyncEntity`.
3.  **Đồng bộ:** `WorkManager` (`SyncWorker`) sẽ tự động chạy lại các yêu cầu trong hàng đợi khi thiết bị có kết nối mạng trở lại.

## 8. Security
*   **JWT Token:** Lưu trữ Access Token và Refresh Token trong `TokenManager`.
*   **Encrypted Storage:** Sử dụng `EncryptedSharedPreferences` (qua `DataStoreManager` hoặc `SecurityUtils`) để lưu thông tin nhạy cảm.
*   **Interceptors:**
    *   `AuthInterceptor`: Tự động thêm `Authorization: Bearer <token>` vào mọi request.
    *   `TokenAuthenticator`: Tự động gọi Refresh Token khi Access Token hết hạn (401).
*   **Face Data:** Embedding được mã hóa trước khi lưu vào Room bằng `SecurityUtils.encryptEmbedding`.

## 9. Screen Documentation
*   **SplashScreen:** Kiểm tra trạng thái đăng nhập và điều hướng.
*   **LoginScreen:** Form đăng nhập với validation.
*   **HomeScreen:** Dashboard chính với các phím tắt tính năng.
*   **ProfileScreen:** Hiển thị thông tin chi tiết sinh viên (MSSV, CCCD, Lớp, ...).
*   **RoomScreen:** Hiển thị số phòng, tầng, danh sách bạn cùng phòng.
*   **PaymentScreen:** Danh sách hóa đơn và tích hợp VietQR để thanh toán.
*   **FaceRegistrationScreen:** Camera Preview tích hợp ML Kit để quét mặt và kiểm tra liveness.
*   **AccessHistoryScreen:** Danh sách các lần ra vào với thời gian và địa điểm (Cổng).

## 10. Data Flow
**Ví dụ luồng Thanh toán:**
1.  `PaymentScreen` gọi `viewModel.fetchInvoices()`.
2.  `PaymentViewModel` gọi `GetInvoicesUseCase`.
3.  `PaymentRepository` lấy từ `PaymentApiService` và cache vào `InvoiceDao`.
4.  Khi User nhấn "Thanh toán", App gọi `verifyPayment`.
5.  Nếu mất mạng, App lưu vào `PendingSyncDao`. `SyncWorker` sẽ gửi lại sau.

## 11. Dependency Injection
Sử dụng Hilt Modules:
*   `NetworkModule`: Cấu hình Retrofit, OkHttp, Interceptors.
*   `DatabaseModule`: Khởi tạo Room Database và các DAO.
*   `RepositoryModule`: Bind các Repository Interface với Implementation.
*   `DataSourceModule`: Cung cấp Remote và Local DataSources.

## 12. WorkManager
*   **SyncWorker:** Định kỳ hoặc khi có mạng, quét bảng `pending_sync` để thực hiện lại các hành động: `VERIFY_PAYMENT`, `UPDATE_PROFILE`, `REGISTER_FACE`.
*   **Retry Logic:** Tối đa 5 lần thử lại với khoảng thời gian giãn cách.

## 13. Dead Code Analysis
**SAFE TO DELETE:**
*   `com.ktx.dormitory.ai.presentation`: Có vẻ là code cũ của phần AI, hiện tại AppNavigation dùng `com.ktx.dormitory.presentation.face.screen`.
*   `MockDataProvider.kt`: Dùng cho testing hoặc giai đoạn đầu, hiện đã có API thật.
*   `AccessScreen.kt`: Thay thế bởi `AccessHistoryScreen.kt`.

**NEED REVIEW:**
*   `FaceVerificationScreen.kt`: Có file nhưng chưa thấy route trong `AppNavigation`. Cần kiểm tra xem có dùng cho tính năng Verify tại chỗ không.

## 14. Project Health Report
| Hạng mục | Điểm | Ghi chú |
| --- | --- | --- |
| Build | 10/10 | Build ổn định với Gradle Kotlin DSL. |
| Architecture | 9/10 | Tuân thủ Clean Architecture rất tốt. |
| Security | 9/10 | Có mã hóa embedding và Interceptor xử lý token bài bản. |
| Offline | 8/10 | Có hàng đợi đồng bộ nhưng cần test kỹ luồng xung đột dữ liệu. |
| Maintainability | 9/10 | Code tách biệt rõ ràng, dễ bảo trì. |

## 15. Final Summary
Hệ thống hiện tại đã hoàn thiện các tính năng cốt lõi cho sinh viên:
1.  Quản lý tài khoản & hồ sơ.
2.  Tra cứu phòng & đơn đăng ký.
3.  Thanh toán hóa đơn tập trung.
4.  Đăng ký khuôn mặt với AI (Liveness + Quality check).
5.  Lịch sử ra vào tích hợp.

**Trạng thái:** **READY FOR DEMO**
Các chức năng không còn API Backend (như Thông báo, Yêu cầu sửa chữa) đã được loại bỏ khỏi mã nguồn để đảm bảo tính nhất quán.
