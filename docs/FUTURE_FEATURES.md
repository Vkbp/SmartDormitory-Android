# FUTURE FEATURES - ĐỀ XUẤT TIỆN ÍCH CHO SINH VIÊN

Dựa trên bối cảnh hệ thống **Smart Dormitory**, tài liệu này đề xuất các tính năng cần được phát triển thêm để hoàn thiện hệ sinh thái ứng dụng dành cho sinh viên.

## 1. Tiện ích Quản lý Cơ sở vật chất

### [ĐỀ XUẤT PHÁT TRIỂN] Báo hỏng thiết bị (Maintenance Request)
*   **Mục đích**: Cho phép sinh viên chụp ảnh và báo cáo các hư hỏng trong phòng (đèn cháy, vòi nước rò rỉ).
*   **Giá trị**: Rút ngắn thời gian sửa chữa, tăng tính minh bạch.
*   **Độ ưu tiên**: **P0** (Phù hợp làm luận văn).

### [ĐỀ XUẤT PHÁT TRIỂN] Theo dõi Điện & Nước (Utility Monitoring)
*   **Mục đích**: Kết nối với các công tơ thông minh (IoT) để sinh viên theo dõi số dư và mức tiêu thụ điện nước theo thời gian thực.
*   **Giá trị**: Giúp sinh viên điều chỉnh hành vi sử dụng, tránh bị sốc hóa đơn cuối tháng.
*   **Độ ưu tiên**: **P1** (Cần IoT).

## 2. Tiện ích An ninh & An toàn

### [ĐỀ XUẤT PHÁT TRIỂN] Quản lý Khách ghé thăm (Visitor Management)
*   **Mục đích**: Sinh viên đăng ký thông tin khách, hệ thống sinh mã QR dùng một lần để khách quét tại cổng an ninh.
*   **Giá trị**: Kiểm soát an ninh chặt chẽ hơn thay vì ghi sổ tay thủ công.
*   **Độ ưu tiên**: **P1**.

### [ĐỀ XUẤT PHÁT TRIỂN] Nút bấm SOS khẩn cấp (Emergency SOS)
*   **Mục đích**: Một nút bấm nổi bật trên ứng dụng để gửi cảnh báo kèm vị trí phòng cho Ban quản lý trong trường hợp khẩn cấp (y tế, hỏa hoạn).
*   **Giá trị**: Đảm bảo an toàn tính mạng cho sinh viên.
*   **Độ ưu tiên**: **P0**.

## 3. Tiện ích Dịch vụ Đời sống

### [ĐỀ XUẤT PHÁT TRIỂN] Quản lý Bưu phẩm (Parcel Management)
*   **Mục đích**: Nhận thông báo khi có bưu phẩm gửi đến KTX, quét QR code để nhận hàng tại tủ thông minh hoặc quầy bảo vệ.
*   **Giá trị**: Tránh thất lạc hàng hóa, tiện lợi cho sinh viên đi học cả ngày.
*   **Độ ưu tiên**: **P2**.

### [ĐỀ XUẤT PHÁT TRIỂN] Đặt phòng học/Sinh hoạt chung (Facility Booking)
*   **Mục đích**: Xem lịch trống và đặt chỗ tại các phòng tự học, phòng gym hoặc phòng SHC.
*   **Giá trị**: Tối ưu hóa việc sử dụng tài nguyên chung.
*   **Độ ưu tiên**: **P2**.

## 4. Giao tiếp & Cộng đồng

### [ĐỀ XUẤT PHÁT TRIỂN] Trung tâm Thông báo (Notification Center)
*   **Mục đích**: Nơi lưu trữ tất cả các thông báo từ Ban quản lý (thông báo cúp điện, lịch đóng tiền, sự kiện).
*   **Giá trị**: Sinh viên không bị trôi mất thông tin quan trọng.
*   **Độ ưu tiên**: **P0**.

### [ĐỀ XUẤT PHÁT TRIỂN] AI Chatbot hỗ trợ (Smart Assistant)
*   **Mục đích**: Giải đáp nhanh các câu hỏi thường gặp (FAQ) về nội quy, thủ tục hành chính bằng ngôn ngữ tự nhiên.
*   **Giá trị**: Giảm tải cho bộ phận tiếp sinh viên.
*   **Độ ưu tiên**: **P2**.

## 5. Tổng hợp Ma trận Đề xuất

| Tính năng | Độ khó | Ưu tiên | Phù hợp Luận văn |
| :--- | :---: | :---: | :---: |
| Báo hỏng thiết bị | Thấp | **P0** | Có |
| SOS Khẩn cấp | Thấp | **P0** | Có |
| Trung tâm Thông báo | Thấp | **P0** | Có |
| Quản lý Khách (QR) | Trung bình | **P1** | Có |
| Theo dõi Điện Nước | Cao | **P1** | Có (Mảng IoT) |
| Quản lý Bưu phẩm | Trung bình | **P2** | Có |
| AI Chatbot | Cao | **P2** | Có (Mảng AI) |

---
**Ghi chú**: Các đề xuất trên hoàn toàn có thể triển khai dựa trên nền tảng Clean Architecture hiện tại của Mobile App bằng cách thêm các Module/Package mới.
