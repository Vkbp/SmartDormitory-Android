# SMART DORMITORY MOBILE-BACKEND COMPATIBILITY REPORT

*Lưu ý: Báo cáo này sử dụng Backend làm Nguồn Chân Lý (Source of Truth) và chỉ đánh giá/sửa đổi Mobile Android trong phạm vi chức năng của Sinh viên.*

---

# PHẦN 1 - PHÂN TÍCH PHẠM VI NGHIỆP VỤ

## Chức năng Sinh viên (Phạm vi Audit)
- Đăng nhập (Auth) / Refresh Token
- Cập nhật và xem hồ sơ cá nhân (Profile / Avatar)
- Xem thông tin phòng ở (Room Info)
- Quản lý hóa đơn và Thanh toán (Invoice / Payment)
- Đăng ký dữ liệu khuôn mặt (Face Registration)
- Xem lịch sử ra vào (Access History)

## Chức năng Staff/Admin (Loại trừ)
- Các tính năng phê duyệt gia hạn nội trú, Admin Dashboards, Quản trị hệ thống, Kiểm soát truy cập (TimeWindow, Curfew).

*⚠️ Ghi chú*: Chức năng **"Thông báo" (Notification)** và **"Gửi yêu cầu sửa chữa" (Request)** hiện có mã nguồn giao diện trên Mobile nhưng Backend hoàn toàn **không có Controller** xử lý. Do Backend là Source of Truth, các tính năng này trên Mobile được xem là "Orphan Features" (Mã nguồn dư thừa) và đánh dấu là FAIL.

---

# PHẦN 2 - API CONTRACT AUDIT

| API (Nghiệp vụ Sinh viên) | Mobile Endpoint | Backend Endpoint (Source of Truth) | Trạng thái |
| --- | --- | --- | --- |
| Login | `POST /v1/auth/login` | `POST /api/v1/auth/login` | PASS |
| Refresh Token | `POST /v1/auth/refresh-token` | `POST /api/v1/auth/refresh-token` | PASS |
| Get Profile | `GET /v1/students/me` | `GET /api/v1/students/me` | PASS |
| Update Profile | `PATCH /v1/students/me` | `PATCH /api/v1/students/me` | PASS |
| Upload Avatar | `POST /v1/uploads/avatar` | `POST /api/v1/uploads/avatar` | PASS |
| Get Room Info | `GET /v1/users/room` | `GET /api/v1/student/room/current` | FAIL (Sai URL) |
| Get Invoices | `GET /v1/payments/invoices` | `GET /api/v1/bills` | FAIL (Sai URL) |
| Verify Payment | `POST /v1/payments/verify/{id}` | `POST /api/payments/online` | FAIL (Sai URL & Body) |
| Register Face | `POST /v1/ai/register-face` | `POST /api/v1/students/me/face` | FAIL (Sai URL) |
| Access History | `GET /v1/access/history` | `GET /api/v1/access/history/student/{id}` | FAIL (Thiếu Tham số) |
| Verify QR Access | `POST /v1/access/verify-qr` | (Không hỗ trợ cho Student) | FAIL (Không tồn tại) |
| Get Notifications | `GET /v1/notifications` | (Không tồn tại) | FAIL (Orphan Feature) |
| Submit Request | `POST /v1/requests` | (Không tồn tại) | FAIL (Orphan Feature) |

---

# PHẦN 3 - DTO MAPPING AUDIT

| Field | Mobile DTO | Backend Response | Mức độ |
| --- | --- | --- | --- |
| `InvoiceDto.id` | `id` kiểu **Long** | `billId` kiểu **UUID String** | **Critical** |
| `InvoiceDto.paidAmount` | `@SerializedName("paid_amount")` (Snake Case) | `paidAmount` (Camel Case) | High |
| `RoomInfoDto.building` | `@SerializedName("building")` | `buildingName` | High |
| `RoomInfoDto.status` | `@SerializedName("status")` | `roomStatus` | High |
| `FaceRegisterRequest` | `embedding` (List<Float>) | `faceImageUrl` (String URL) | **Critical** |
| `UserResponse.fullName` | Có field `fullName` | Không có (chỉ có `username`, `email`) | Medium |
| `AccessLogDto` Response | Chờ đợi `BaseResponse<List<T>>` | Backend trả trực tiếp `Page<AccessHistory>` | **Critical** |

---

# PHẦN 4 - REPOSITORY AUDIT

- **AuthRepository**: Gọi API `/login` và xử lý lưu Token đúng đắn. Việc dư thừa trường `role` không gây lỗi Gson Parsing. 
- **UserRepository**: Map sai cấu trúc của `RoomInfoDto` và gọi sai API URL (`/users/room` thay vì `/student/room/current`). Sẽ không lấy được dữ liệu phòng.
- **PaymentRepository**: Gọi API `invoices`. Sẽ bị **Crash ứng dụng** ngay lập tức do Gson cố gắng convert một chuỗi UUID string từ Backend thành kiểu số `Long`.
- **AccessRepository**: Gọi `/ai/register-face` truyền một mảng số thực Float. Backend trả về 404 Not Found hoặc 400 Bad Request do sai URL và sai DTO (Backend chờ URL ảnh upload).

---

# PHẦN 5 - STUDENT FEATURE VERIFICATION

| Chức năng | Trạng thái | Ghi chú |
| --- | --- | --- |
| Login / Logout | **PASS** | Hoạt động tốt với Flow chuẩn JWT. |
| Profile Info | **PASS** | Giao tiếp đúng với Backend API. |
| Room Information | **FAIL** | Gọi sai URL không tồn tại, sai tên thuộc tính DTO. |
| Invoice Info | **FAIL** | Lỗi parse JSON UUID -> Long. App bị văng (Crash). |
| Online Payment | **FAIL** | Gọi API `/verify/{id}` không tồn tại. |
| Request (Sửa chữa) | **FAIL** | Backend không có tính năng này. |
| Notification | **FAIL** | Backend không có tính năng này. |
| Face Registration | **FAIL** | Sai phương thức tính toán (Local Embedding vs Server URL). |
| Face Verification | **FAIL** | Backend không có tính năng kiểm tra dành cho Sinh viên. |
| QR Access | **FAIL** | Backend không có API QR. |
| Access History | **FAIL** | Thiếu truyền ID và sai cấu trúc bọc response. |

---

# PHẦN 6 - INTEGRATION BUG DETECTION

1. **Bug Parse JSON Hóa Đơn (CRITICAL CRASH)**
   - **File**: `InvoiceDto.kt`
   - **Nguyên nhân**: Backend trả về `billId` là một chuỗi UUID hợp lệ, nhưng Mobile lại ép Gson parse vào thuộc tính `@SerializedName("id") val id: Long`. Gây ra lỗi `NumberFormatException` và sập UI.
2. **Bug Mismatch Convention Serialization**
   - **File**: `InvoiceDto.kt`, `RoomInfoDto.kt`
   - **Nguyên nhân**: Mobile kỳ vọng Backend trả về dữ liệu Snake Case (vd: `paid_amount`, `room_code`), nhưng mặc định Backend Spring Boot dùng Jackson trả về Camel Case (vd: `paidAmount`, `roomCode`). Kết quả: Dữ liệu bị `null` trên giao diện.
3. **Bug Face Registration Logic**
   - **File**: `AccessApiService.kt`, `FaceRegisterRequestDto.kt`
   - **Nguyên nhân**: Mobile tự xử lý AI bằng TensorFlow Lite rồi gửi mảng số liệu `embedding` lên Server. Trong khi Backend chờ nhận vào link URL `faceImageUrl` của tấm ảnh chân dung sinh viên. Giao tiếp hoàn toàn vô nghĩa.
4. **Bug Missing BaseResponse Wrapper**
   - **File**: `AccessApiService.kt`
   - **Nguyên nhân**: API History gọi của Admin trả thẳng về đối tượng `Page` của Spring Data, không có thuộc tính `success` hay `data`. Mobile sẽ đọc nhầm luồng `BaseResponse<T>` và hiển thị rỗng.

---

# PHẦN 7 - AUTO FIX PLAN (MOBILE ONLY)

*Do nguyên tắc Backend là Nguồn Chân Lý, mọi sự sửa đổi đều được chỉ định áp dụng trên Mobile.*

### 1. Sửa Lỗi Crash Hóa đơn (Invoices)
* **File cần sửa**: `PaymentApiService.kt`
* **Đoạn code hiện tại**: `@GET("v1/payments/invoices")`
* **Đoạn code đề xuất**: `@GET("v1/bills")`
* **Lý do**: Khớp với đường dẫn của `BillController` tại Backend.

* **File cần sửa**: `InvoiceDto.kt`
* **Đoạn code hiện tại**:
  ```kotlin
  @SerializedName("id") val id: Long
  @SerializedName("paid_amount") val paidAmount: Double
  @SerializedName("remaining_amount") val remainingAmount: Double
  @SerializedName("due_date") val dueDate: String
  @SerializedName("room_code") val roomCode: String?
  ```
* **Đoạn code đề xuất**:
  ```kotlin
  @SerializedName("billId") val id: String // Đổi Long thành String để đọc UUID
  @SerializedName("paidAmount") val paidAmount: Double
  @SerializedName("remainingAmount") val remainingAmount: Double
  @SerializedName("dueDate") val dueDate: String
  @SerializedName("roomCode") val roomCode: String?
  ```
* **Lý do**: Khắc phục ngay lập tức lỗi Crash do parse UUID. Khớp hoàn toàn với định dạng CamelCase do Jackson sinh ra từ Backend.

### 2. Sửa Lỗi Lấy Thông tin Phòng
* **File cần sửa**: `UserApiService.kt`
* **Đoạn code hiện tại**: `@GET("v1/users/room")`
* **Đoạn code đề xuất**: `@GET("v1/student/room/current")`

* **File cần sửa**: `RoomInfoDto.kt`
* **Đoạn code hiện tại**:
  ```kotlin
  @SerializedName("building") val building: String?
  @SerializedName("room_code") val roomCode: String?
  @SerializedName("status") val status: String?
  ```
* **Đoạn code đề xuất**:
  ```kotlin
  @SerializedName("buildingName") val building: String?
  @SerializedName("roomCode") val roomCode: String?
  @SerializedName("roomStatus") val status: String?
  ```
* **Lý do**: Tương thích với `CurrentRoomResponse.java` từ Backend.

### 3. Sửa Lỗi Đăng Ký Khuôn Mặt
* **File cần sửa**: `AccessApiService.kt`
* **Đoạn code hiện tại**:
  ```kotlin
  @POST("v1/ai/register-face")
  suspend fun registerFaceOnServer(@Body request: FaceRegisterRequestDto): BaseResponse<Unit>
  ```
* **Đoạn code đề xuất**:
  ```kotlin
  @POST("v1/students/me/face")
  suspend fun registerFaceOnServer(@Body request: Map<String, String>): BaseResponse<Unit>
  // Chú thích cho Android: ViewModel cần gửi request = mapOf("faceImageUrl" to url_sau_khi_upload)
  ```
* **Lý do**: Khớp với API `@PostMapping("/me/face")` yêu cầu URL String từ `FaceStudentController` thay vì gửi ma trận.

---

# PHẦN 8 - FINAL INTEGRATION REPORT

1. **Tổng số API của Sinh viên**: 13
2. **API đúng chuẩn (PASS)**: 5 (38.5%)
3. **API sai chuẩn (FAIL)**: 8 (61.5%)
4. **DTO đúng chuẩn**: 2 (`LoginRequest`, `UpdateProfileRequest`)
5. **DTO sai chuẩn nghiêm trọng**: 4 (`InvoiceDto`, `RoomInfoDto`, `UserResponse`, `FaceRegisterRequestDto`)
6. **Chức năng sinh viên HOẠT ĐỘNG**: Đăng nhập, Hiển thị Hồ Sơ, Đổi mật khẩu.
7. **Chức năng sinh viên CHƯA HOẠT ĐỘNG**: Xem thông tin Phòng, Hóa đơn (crash app), Thanh toán, Ra vào cổng bảo mật, Lịch sử ra vào.
8. **Danh sách Mobile File cần sửa**: 
   - `InvoiceDto.kt` (Rất gấp)
   - `PaymentApiService.kt`
   - `RoomInfoDto.kt`
   - `UserApiService.kt`
   - `AccessApiService.kt`

## KẾT LUẬN CUỐI CÙNG
**NOT COMPATIBLE**

Bản thân Mobile và Backend không thể thực thi các nghiệp vụ cốt lõi do lỗi giao tiếp hợp đồng. Lỗi phân giải JSON UUID -> Long và khác biệt lớn về kiến trúc nghiệp vụ ảnh hưởng tới độ khả dụng của ứng dụng. Mobile cần được tái thiết kế Data Layer dựa trên bảng Auto Fix Plan được cung cấp phía trên để có thể "nói chung một ngôn ngữ" với Backend.
