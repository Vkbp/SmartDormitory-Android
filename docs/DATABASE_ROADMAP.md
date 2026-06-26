# DATABASE ROADMAP - CẤU TRÚC DỮ LIỆU HỆ THỐNG

Tài liệu này liệt kê các bảng dữ liệu cần có để vận hành hệ thống Smart Dormitory, phân chia theo thực tế hiện tại và kế hoạch tương lai.

## 1. Các Bảng Hiện có (Core Schema)

| Tên bảng | Mục đích |
| :--- | :--- |
| `users` | Thông tin tài khoản đăng nhập (mssv, password_hash, role). |
| `students` | Thông tin chi tiết sinh viên (họ tên, ngày sinh, cccd, avatar). |
| `rooms` | Quản lý thông tin phòng (tên phòng, loại phòng, tầng). |
| `beds` | Quản lý giường trong từng phòng (số giường, trạng thái trống). |
| `buildings` | Quản lý các tòa nhà trong KTX. |
| `applications` | Đơn đăng ký ở KTX của sinh viên. |
| `bills` | Hóa đơn tiền phòng và dịch vụ. |
| `face_profiles` | Lưu trữ vector khuôn mặt và trạng thái phê duyệt AI. |
| `access_logs` | Nhật ký ra vào của sinh viên tại các cổng an ninh. |

## 2. Các Bảng Cần bổ sung (Future Schema)

### [ĐỀ XUẤT PHÁT TRIỂN] Phân hệ Tiện ích & Báo hỏng
| Tên bảng | Mục đích |
| :--- | :--- |
| `maintenance_requests` | Lưu thông tin báo hỏng (mô tả, ảnh, trạng thái xử lý). |
| `notifications` | Lưu trữ các thông báo gửi tới sinh viên. |
| `notification_receipts` | Theo dõi sinh viên nào đã đọc thông báo nào. |

### [ĐỀ XUẤT PHÁT TRIỂN] Phân hệ An ninh Mở rộng
| Tên bảng | Mục đích |
| :--- | :--- |
| `visitor_requests` | Thông tin khách ghé thăm và mã QR tương ứng. |
| `stay_extensions` | Lịch sử yêu cầu gia hạn lưu trú và kết quả phê duyệt. |

### [ĐỀ XUẤT PHÁT TRIỂN] Phân hệ Dịch vụ & IoT
| Tên bảng | Mục đích |
| :--- | :--- |
| `utility_meters` | Lưu thông số công tơ điện/nước thông minh. |
| `utility_readings` | Nhật ký chỉ số tiêu thụ theo từng ngày/tháng. |
| `facility_bookings` | Quản lý việc đặt chỗ phòng chức năng (Gym, SHC). |

## 3. Sơ đồ Quan hệ (Gợi ý)
*   `Student` (1) --- (N) `Application`
*   `Room` (1) --- (N) `Bed`
*   `Bed` (1) --- (1) `Student` (Active Assignment)
*   `Student` (1) --- (N) `Bill`
*   `Student` (1) --- (1) `FaceProfile`
*   `Student` (1) --- (N) `AccessLog`

---
**Định hướng kỹ thuật**: Sử dụng PostgreSQL kết hợp với `pgvector` để lưu trữ và đối sánh khuôn mặt ngay trong database, giúp đơn giản hóa kiến trúc AI.
