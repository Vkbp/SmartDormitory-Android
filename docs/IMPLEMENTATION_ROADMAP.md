# IMPLEMENTATION ROADMAP - LỘ TRÌNH TRIỂN KHAI DỰ ÁN

Lộ trình này chia việc phát triển hệ thống Smart Dormitory thành các giai đoạn dựa trên mức độ quan trọng và khả năng triển khai thực tế.

## Giai đoạn 0: Hoàn thiện Sản phẩm Demo (P0)
*   **Mục tiêu**: Đảm bảo tất cả các luồng hiện tại chạy trơn tru với Backend thực.
*   **Công việc Mobile**:
    *   Tối ưu hóa UI/UX các màn hình chính.
    *   Xử lý triệt để các edge cases khi mất mạng (Offline Sync).
    *   Hoàn thiện luồng Face Registration với Liveness ổn định.
*   **Công việc Backend**:
    *   Cấu hình môi trường Production (Staging).
    *   Tích hợp thực tế với hệ thống gửi Email (Forgot Password).

## Giai đoạn 1: Thông báo & Phản hồi (P1)
*   **Mục tiêu**: Tăng tính tương tác giữa Ban quản lý và Sinh viên.
*   **Công việc Mobile**:
    *   Triển khai Module **Notification Center**.
    *   Triển khai Module **Maintenance Request** (Báo hỏng).
*   **Công việc Backend**:
    *   Xây dựng hệ thống gửi thông báo đẩy (Push Notification).
    *   Xây dựng Module quản lý yêu cầu báo hỏng phía Admin.

## Giai đoạn 2: An ninh & Tiện ích (P2)
*   **Mục tiêu**: Tăng cường an ninh và tự động hóa các thủ tục ra vào.
*   **Công việc Mobile**:
    *   Triển khai Module **Visitor Management** (Quản lý khách).
    *   Tích hợp nút bấm **SOS Khẩn cấp**.
*   **Công việc Backend**:
    *   Xây dựng logic sinh và xác thực mã QR cho khách.
    *   Hệ thống cảnh báo SOS thời gian thực cho bảo vệ.

## Giai đoạn 3: Smart IoT & AI (P3)
*   **Mục tiêu**: Trở thành một KTX thông minh thực thụ.
*   **Công việc Mobile**:
    *   Dashboard theo dõi điện nước (IoT).
    *   Tích hợp AI Chatbot hỗ trợ sinh viên.
*   **Công việc Backend**:
    *   Kết nối với các thiết bị Gateway IoT.
    *   Triển khai LLM/Chatbot Engine (như OpenAI API hoặc RAG với LangChain).

## Bảng Tổng hợp Thời gian (Ước tính)

| Giai đoạn | Thời gian | Trọng tâm |
| :--- | :---: | :--- |
| **P0** | 2-4 tuần | Stability & Demo |
| **P1** | 4-6 tuần | Communication |
| **P2** | 6-8 tuần | Security |
| **P3** | 8-12 tuần | Automation |

---
**Ghi chú**: Lộ trình này mang tính chất tham khảo và có thể thay đổi tùy thuộc vào nguồn lực đội ngũ Backend và yêu cầu cụ thể của Ban quản lý KTX.
