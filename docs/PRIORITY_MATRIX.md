# PRIORITY MATRIX - MA TRẬN ƯU TIÊN PHÁT TRIỂN

Ma trận này giúp xác định những đầu việc cần tập trung xử lý ngay để đạt được hiệu quả cao nhất.

## 1. Phân loại theo Tác động và Nỗ lực

| Tác động \ Nỗ lực | Thấp (Dễ làm) | Cao (Khó làm) |
| :--- | :--- | :--- |
| **Cao** | **Quick Wins**: <br> - Sửa lỗi UI <br> - Hoàn thiện Notification <br> - Báo hỏng thiết bị | **Strategic Initiatives**: <br> - Theo dõi Điện nước (IoT) <br> - AI Chatbot <br> - Xác thực khuôn mặt Server-side |
| **Thấp** | **Fill-ins**: <br> - Dark Mode hoàn thiện <br> - Thống kê cá nhân <br> - FAQ tĩnh | **Deprioritized**: <br> - Đặt phòng học <br> - Quản lý bưu phẩm |

## 2. Ma trận Trạng thái và Ưu tiên Tính năng

| Tính năng | Mobile Code | Backend API | Database | Độ ưu tiên | Demo | Production |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| **Login/Auth** | Xong | Có | Có | **P0** | OK | OK |
| **Face Reg.** | Xong | Có | Có | **P0** | OK | Cần Test AI |
| **Profile** | Xong | Có | Có | **P0** | OK | OK |
| **Room Info** | Xong | Có | Có | **P1** | OK | OK |
| **Payment** | Xong | Có | Có | **P1** | OK | Cần Webhook |
| **Báo hỏng** | Cần làm | Cần làm | Cần làm | **P1** | No | No |
| **Thông báo** | Cần làm | Cần làm | Cần làm | **P1** | No | No |
| **Khách thăm** | Cần làm | Cần làm | Cần làm | **P2** | No | No |
| **Điện nước** | Cần làm | Cần làm | Cần làm | **P2** | No | No |

## 3. Khuyến nghị tập trung (Action Plan)

1.  **Ưu tiên 1 (Ngay lập tức)**: Ổn định các tính năng P0 (Auth, Face, Profile) để phục vụ bảo vệ luận văn.
2.  **Ưu tiên 2 (Ngắn hạn)**: Xây dựng Module Thông báo và Báo hỏng. Đây là 2 tính năng sinh viên cần nhất.
3.  **Ưu tiên 3 (Dài hạn)**: Tích hợp IoT để quản lý điện nước. Đây là "linh hồn" của KTX thông minh nhưng đòi hỏi chi phí phần cứng.

---
**Kết luận**: Dự án nên tập trung vào việc **"Ổn định hóa"** các tính năng hiện có trước khi mở rộng sang các tính năng mới cần nhiều nguồn lực.
