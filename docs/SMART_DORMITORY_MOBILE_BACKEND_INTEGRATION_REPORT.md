# SMART DORMITORY MOBILE-BACKEND INTEGRATION REPORT

# PHẦN 1 - SYSTEM OVERVIEW
## Mobile
* Công nghệ: Kotlin, Jetpack Compose, MVVM + Clean Architecture, Room, Retrofit, Coroutines.
* Architecture: 3-layer Clean Architecture (Domain, Data, Presentation).
* Repository: Sử dụng Room DB cho offline và Retrofit cho online.
* Retrofit APIs: Khai báo 6 interfaces gồm AccessApiService, AuthApiService, NotificationApiService, PaymentApiService, RequestApiService, UserApiService.

## Backend
* Framework: Java Spring Boot 3.x.
* Database: PostgreSQL/MySQL qua Spring Data JPA.
* Controllers: 28 REST controllers quản lý hệ thống phân tán (Room, Face, Payment, Auth, Registration).
* API Design: Tiêu chuẩn RESTful trả về Global ApiResponse wrapper và HTTP Status.

# PHẦN 2 - ENDPOINT COMPARISON
Kiểm tra chéo giữa `Controllers` (Backend) và `ApiServices` (Mobile):

| Endpoint | Backend | Mobile | Status |
|---|---|---|---|
| POST `/api/v1/auth/login` | Có (`AuthController`) | Có | PASS |
| POST `/api/v1/auth/refresh-token` | Có (`AuthController`) | Có | PASS |
| GET `/api/v1/users/me` | Có (`UserController`) | Có | PASS |
| POST `/api/v1/ai/register-face` | KHÔNG (Backend dùng `/api/v1/students/me/face`) | Có | WRONG IMPLEMENTATION |
| POST `/api/v1/access/verify-qr` | KHÔNG | Có | MISSING |
| GET `/api/v1/notifications` | KHÔNG | Có | MISSING |
| GET `/api/v1/payments/invoices` | KHÔNG (Backend dùng `/api/v1/bills`) | Có | WRONG IMPLEMENTATION |
| GET `/api/v1/requests` | KHÔNG | Có | MISSING |
| GET `/api/v1/users/room` | KHÔNG (Backend dùng `/api/v1/student/room/current`) | Có | WRONG IMPLEMENTATION |

# PHẦN 3 - DTO COMPARISON
| DTO | Field | Backend | Mobile | Impact |
|---|---|---|---|---|
| `LoginResponse` | `role` | Không trả về role trong Login | Có `role: String?` | Mobile điều hướng sai quyền (crash / fallback) |
| `InvoiceDto` / `BillResponse` | `id` vs `billId` | `billId: UUID` | `id: Long` | Parsing JSON thất bại, màn hình trống |
| `InvoiceDto` / `BillResponse` | snake_case vs camelCase | `paidAmount` | `paid_amount` | Mobile không parse được dữ liệu hóa đơn |
| `FaceRegisterRequestDto` | `embedding` | Dùng `faceImageUrl` (String) | Dùng `embedding` (List<Float>) | Đăng ký khuôn mặt thất bại hoàn toàn |
| `UserResponse` / `MeResponse` | `fullName` | Không có (chỉ có username, email) | Có `fullName` | Hiển thị tên rỗng trên UI |

# PHẦN 4 - BUSINESS LOGIC COMPARISON
## Authentication
* Mobile: Flow Login & Refresh token đúng cấu trúc, nhưng phụ thuộc field `role` không tồn tại trên response từ backend. 
## Face Verification
* Mobile tính toán Vector Embedding (Mảng số thực) thông qua MobileFaceNet ở client và đẩy lên `/api/v1/ai/register-face`.
* Backend yêu cầu gửi URL Ảnh chân dung (sau khi upload) qua endpoint `/api/v1/students/me/face`. **Sai lệch luồng nghiệp vụ hoàn toàn.**
## QR Access
* Mobile gọi `/api/v1/access/verify-qr`. Backend không hỗ trợ endpoint này mà chỉ hỗ trợ quét khuôn mặt.
## Payment
* Mobile lấy Invoices từ `v1/payments/invoices` nhưng Backend thực chất dùng `BillController` (`/api/v1/bills`). Mobile không thể thanh toán trực tuyến do sai hợp đồng API.
## Request Management
* Backend không hề có module "Yêu cầu sửa chữa".
## Notifications
* Backend không có module Notification.
## Student Management
* PASS cho API cập nhật thông tin cá nhân.
## Staff Approval
* Mobile không có màn hình Approval, Backend có Controller cho Review & Approval (DormApplication Admin).

# PHẦN 5 - ROLE & PERMISSION AUDIT
Mobile xác định phân quyền (Navigation, RoleGuard) dựa trên field `role` trong API `Login`. Tuy nhiên, `AuthResponse` của Backend chỉ trả về `accessToken` và `refreshToken`, không hề trả về role. Điều này khiến RoleGuard trên Mobile luôn nhận giá trị `null` và không thể phân quyền chính xác. Backend có `role` bên trong `MeResponse` nhưng Mobile gọi Role ở thời điểm Login.

# PHẦN 6 - FEATURE COVERAGE
## Chức năng Backend đã có (MISSING ON MOBILE)
* **API gia hạn nội trú / Đăng ký ký túc xá**: (`RegistrationController`, `ApplicationController`) - MISSING ON MOBILE
* **API quản lý phòng (Dashboards, Beds, Buildings, Floors)**: Dành cho Admin - MISSING ON MOBILE
* **API Quản lý kiểm soát truy cập (Time Window, Curfew Policies, Emergency Unlock)**: (`SmartAccess`) - MISSING ON MOBILE
* **Xử lý webhook thanh toán (Sepay)**: Backend có webhook, Mobile không hỗ trợ UI.

# PHẦN 7 - UNUSED MOBILE FEATURES
Các tính năng Mobile đã code nhưng Backend không có (ORPHAN FEATURE):
* File `NotificationApiService.kt` (Tính năng Thông báo)
* File `RequestApiService.kt` (Tính năng Gửi Yêu cầu Sửa chữa, Đơn từ)
* Phương thức `verifyQrCode` trong `AccessApiService.kt` (Xác thực cửa bằng QR)

# PHẦN 8 - API CONTRACT VIOLATIONS
1. **Sai Request Body (AI Face Registration)**: 
   - Ví dụ: Mobile gửi mảng float cho `embedding`, Backend đòi URL `faceImageUrl`. API Crash (400 Bad Request).
2. **Sai Serialization Mapping (Payment)**: 
   - Ví dụ: `BillResponse` trả `billId: "UUID"`, Mobile map vào `id: Long`. Ứng dụng ném ra ngoại lệ `IllegalStateException` từ Gson.
3. **Sai Naming Convention**:
   - Ví dụ: Backend dùng `camelCase` (`remainingAmount`), Mobile expect `snake_case` (`remaining_amount`). Dữ liệu sẽ bị null.

# PHẦN 9 - DATABASE MODEL COMPARISON
* **Face**: Backend Entity lưu trữ dạng file ảnh gốc (Image URL), Mobile Domain xử lý TFLite lưu dạng vector toán học (Embedding).
* **Bill/Invoice**: Backend dùng `BigDecimal` cho giá tiền, Mobile dùng `Double`. Backend dùng UUID cho ID, Mobile dùng Long.
* **User**: Backend tách biệt User/Student, Mobile gộp chung thông tin dẫn đến thiếu hụt mapping.

# PHẦN 10 - ERROR HANDLING AUDIT
Backend bắt ngoại lệ qua `GlobalExceptionHandler` và đóng gói lỗi dưới cấu trúc `ApiResponse<T>` với `success = false`, `message = "Lỗi"`.
Mobile sử dụng chung DTO `BaseResponse` với trường `success: Boolean` giúp nhận diện lỗi. Tuy nhiên Retrofit sẽ ném Exception cho HTTP 4xx/5xx thay vì trả về body success=false. Nếu Mobile không có CallAdapter/Interceptor xử lý HttpExceptions thì App sẽ crash. (Cần sửa file Retrofit Client trên Mobile).

# PHẦN 11 - REAL USER FLOW VERIFICATION
## Đăng nhập
Flow: ViewModel -> AuthApiService (`POST /login`) -> Backend
Kết luận: **FAIL** (Không lấy được Role, ảnh hưởng đến routing giao diện).
## Đăng ký khuôn mặt
Flow: CameraX -> MobileFaceNet (Mobile) -> `POST /ai/register-face`
Kết luận: **FAIL** (Không có endpoint, cấu trúc body sai hoàn toàn).
## Quét QR
Kết luận: **FAIL** (Backend không có API QR Access).
## Thanh toán
Kết luận: **FAIL** (Parsing hóa đơn chết vì sai kiểu dữ liệu ID (UUID vs Long) và tên field camelCase/snake_case).
## Gửi yêu cầu sửa chữa
Kết luận: **FAIL** (Orphan Feature, chưa làm phía Backend).
## Gia hạn nội trú
Kết luận: **FAIL** (Missing on Mobile).
## Phê duyệt yêu cầu
Kết luận: **FAIL** (Missing on Mobile).

# PHẦN 12 - CRITICAL INTEGRATION ISSUES
1. **Face Registration Mismatch**: Khác biệt kiến trúc nghiêm trọng. Mobile xử lý AI Edge-computing (nhúng model TFLite), Backend xử lý truyền thống.
2. **Payment UUID Mapping Crash**: Việc không parse được JSON dẫn tới màn hình hóa đơn trắng hoặc văng app.
3. **Missing Role in Login**: Cản trở toàn bộ quá trình phân luồng người dùng. Backend phải bổ sung role vào `AuthResponse`.
4. **URL Mismatch**: Mobile gọi Endpoint sai URL so với cấu trúc mới của Backend (ví dụ `/api/v1/payments/invoices` vs `/api/v1/bills`).

# PHẦN 13 - FIX PLAN
| Priority | Issue | Mobile File | Backend File | Fix Required |
|---|---|---|---|---|
| Critical | Trả thiếu Role khi Đăng nhập | `AuthApiService.kt` | `AuthController.java` | Backend thêm `role` vào `AuthResponse` |
| Critical | UUID / Long mismatch trên hóa đơn | `InvoiceDto.kt` | N/A | Sửa `id: Long` thành `id: String` trên Mobile |
| Critical | CamelCase/Snake_case mismatch hóa đơn | `InvoiceDto.kt` | N/A | Thay đổi `@SerializedName` cho trùng khớp với Backend |
| High | Lệch luồng AI Nhận diện khuôn mặt | `AccessApiService.kt`, Repository | `FaceStudentController.java` | Thống nhất luồng dữ liệu Edge vs Server |
| High | Lệch Endpoint Room / Bills | `UserApiService.kt`, `PaymentApiService.kt`| N/A | Sửa URL trên Mobile thành `/api/v1/student/room/current` và `/api/v1/bills` |
| Medium | Xóa API rác (Notification, Request) | `NotificationApiService.kt` | N/A | Dọn dẹp code rác trên Mobile |

# PHẦN 14 - FINAL COMPATIBILITY SCORE
| Hạng mục | Điểm |
|---|---|
| API Compatibility | 2/10 |
| DTO Compatibility | 1/10 |
| Business Logic Match | 2/10 |
| Role Consistency | 0/10 |
| Feature Coverage | 3/10 |
| Overall Integration | 1.6/10 |

# PHẦN 15 - FINAL VERDICT
❌ NOT INTEGRATED

Hệ thống Mobile và Backend hiện tại **không thể kết nối và chạy cùng nhau**. Code Mobile dường như được phát triển độc lập (hoặc kết nối với một bản mock backend cũ), dẫn đến sự lệch pha nghiêm trọng trong thiết kế Endpoint, cấu trúc DTO, Data type (UUID vs Long), Serialization conventions (Camel vs Snake), và toàn bộ luồng nghiệp vụ AI Face Recognition. Đòi hỏi phải đại tu toàn diện phần kết nối mạng của Mobile.
