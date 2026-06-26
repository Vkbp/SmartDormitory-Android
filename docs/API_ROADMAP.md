# API ROADMAP - LỘ TRÌNH PHÁT TRIỂN API

Tài liệu này tổng hợp các API hiện có, các API còn thiếu (Mobile Ready) và các API đề xuất phát triển.

## 1. Các API Hiện có (Đã triển khai & Đang dùng)

| Method | Endpoint | Mô tả | Auth |
| :--- | :--- | :--- | :---: |
| POST | `/v1/auth/login` | Đăng nhập lấy JWT | No |
| POST | `/v1/auth/refresh` | Làm mới Access Token | Yes |
| GET | `/v1/students/me` | Lấy thông tin cá nhân | Yes |
| PATCH | `/v1/students/me` | Cập nhật hồ sơ | Yes |
| GET | `/v1/student/room/current` | Lấy thông tin phòng ở | Yes |
| GET | `/v1/bills` | Xem danh sách hóa đơn | Yes |
| POST | `/v1/face/register` | Đăng ký vector khuôn mặt | Yes |
| GET | `/v1/access/history` | Xem lịch sử ra vào | Yes |

## 2. Các API Còn thiếu (Mobile đã có code, Backend cần bổ sung)

| Method | Endpoint | Mô tả | Ưu tiên |
| :--- | :--- | :--- | :---: |
| POST | `/v1/auth/forgot-password` | Gửi email đặt lại mật khẩu | **P0** |
| POST | `/v1/auth/change-password` | Đổi mật khẩu trực tiếp | **P0** |
| POST | `/v1/extensions/request` | Gửi yêu cầu gia hạn lưu trú | **P1** |
| POST | `/v1/bills/{id}/verify` | Xác thực thanh toán thủ công | **P1** |
| POST | `/v1/face/verify` | Kiểm tra vector trên server | **P1** |

## 3. Các API Đề xuất (Phát triển tính năng mới)

| Method | Endpoint | Mô tả | Ưu tiên |
| :--- | :--- | :--- | :---: |
| POST | `/v1/maintenance/requests` | [ĐỀ XUẤT] Báo hỏng thiết bị | **P0** |
| GET | `/v1/notifications` | [ĐỀ XUẤT] Lấy danh sách thông báo | **P0** |
| POST | `/v1/visitors` | [ĐỀ XUẤT] Đăng ký khách | **P1** |
| GET | `/v1/utilities/usage` | [ĐỀ XUẤT] Xem chỉ số điện nước | **P1** |
| POST | `/v1/sos/alert` | [ĐỀ XUẤT] Phát tín hiệu khẩn cấp | **P0** |

## 4. Đặc tả Dữ liệu Chung (Standard DTO)

### Phản hồi Thành công (Success)
```json
{
  "success": true,
  "data": { ... },
  "message": "Thành công"
}
```

### Phản hồi Lỗi (Error)
```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "Mô tả chi tiết lỗi",
    "details": []
  }
}
```

---
**Ghi chú**: Tất cả các API đều yêu cầu Header `Authorization: Bearer <token>` ngoại trừ API đăng nhập và quên mật khẩu.
