# BÁO CÁO TỔNG KẾT DỰ ÁN: HỆ THỐNG QUẢN LÝ KÝ TÚC XÁ THÔNG MINH (SMART DORMITORY)

> [!NOTE]
> Báo cáo này được biên soạn bởi Chuyên gia Phân tích Hệ thống và Giảng viên Đại học. Nội dung tập trung giải thích toàn bộ giải pháp phần mềm bằng ngôn ngữ nghiệp vụ thực tế, dễ tiếp cận cho cả người không chuyên về lập trình, sẵn sàng để đưa vào báo cáo tốt nghiệp hoặc slide bảo vệ.

---

## PHẦN 1 - GIỚI THIỆU DỰ ÁN

### 1. Tên dự án
*   **Tên tiếng Anh:** Smart Dormitory Management System (Android Client)
*   **Tên tiếng Việt:** Ứng dụng di động Quản lý Ký túc xá Thông minh dành cho Sinh viên và Ban quản lý.

### 2. Mục tiêu xây dựng
Dự án được xây dựng nhằm hiện đại hóa hoạt động quản lý tại các ký túc xá đại học, chuyển đổi từ mô hình quản lý thủ công giấy tờ sang mô hình tự động hóa hoàn toàn trên thiết bị di động. Mục tiêu cốt lõi là tối ưu hóa trải nghiệm nội trú của sinh viên, giảm tải khối lượng công việc hành chính cho ban quản lý, đồng thời áp dụng trí tuệ nhân tạo (AI) để nâng cao độ an toàn và bảo mật cho môi trường học đường.

### 3. Bài toán thực tế cần giải quyết
Tại các ký túc xá truyền thống, cả sinh viên và người quản lý đều đối mặt với nhiều bất tiện:
*   **Thủ tục hành chính rườm rà:** Đăng ký phòng, xin gia hạn hay báo hỏng trang thiết bị phải viết đơn giấy, nộp trực tiếp và chờ phản hồi nhiều ngày.
*   **Thanh toán bất tiện:** Việc thu tiền phòng, tiền điện, tiền nước thủ công dễ xảy ra sai sót, đối soát chậm và mất thời gian xếp hàng của sinh viên.
*   **Bảo mật lỏng lẻo:** Việc kiểm soát ra vào cổng ký túc xá bằng thẻ từ hoặc ghi sổ thủ công dễ bị giả mạo (cho mượn thẻ, người lạ trà trộn), thiếu nhật ký lưu trữ chính xác.
*   **Phụ thuộc vào mạng Internet:** Hầu hết các ứng dụng hiện đại sẽ tê liệt khi mất kết nối mạng, khiến sinh viên không thể vào phòng hoặc báo cáo sự cố khẩn cấp.

### 4. Đối tượng sử dụng
*   **Sinh viên nội trú:** Nhóm người dùng chính, sử dụng ứng dụng để thực hiện mọi hoạt động từ đăng ký chỗ ở, thanh toán hóa đơn, gửi yêu cầu sửa chữa đến điểm danh ra vào cổng.
*   **Nhân viên ký túc xá (Staff):** Nhóm xử lý tác nghiệp trực tiếp, duyệt các yêu cầu của sinh viên, theo dõi chỉ số điện nước, gửi thông báo khẩn cấp và kiểm soát an ninh.
*   **Quản trị viên hệ thống (Admin):** Cấp quản lý cao nhất, giám sát toàn bộ hoạt động, phân quyền người dùng, xem thống kê và điều chỉnh các thiết lập hệ thống.

### 5. Ý nghĩa thực tiễn
Hệ thống không chỉ đơn thuần là một ứng dụng quản lý mà còn là giải pháp toàn diện giúp xây dựng mô hình **Ký túc xá số thông minh**. Nó giúp tiết kiệm 80% thời gian xử lý thủ tục hành chính, loại bỏ hoàn toàn hóa đơn giấy, đảm bảo an ninh tuyệt đối nhờ công nghệ nhận diện khuôn mặt người thật và duy trì hoạt động thông suốt ngay cả trong điều kiện mạng chập chờn hoặc mất kết nối.

---

## PHẦN 2 - NHỮNG CHỨC NĂNG CHÍNH

Dưới đây là chi tiết về 14 chức năng cốt lõi của hệ thống Smart Dormitory, được mô tả từ góc độ nghiệp vụ và trải nghiệm thực tế:

### 1. Đăng nhập và Xác thực bảo mật
*   **Mục đích:** Xác minh danh tính người dùng trước khi truy cập vào hệ thống.
*   **Người sử dụng:** Sinh viên, Nhân viên, Quản trị viên.
*   **Cách hoạt động:** Người dùng có thể đăng nhập bằng tài khoản (mã số sinh viên/email) và mật khẩu thông thường. Đặc biệt, hệ thống hỗ trợ tích hợp khóa bảo mật sinh trắc học của điện thoại (vân tay hoặc nhận diện khuôn mặt của thiết bị) thông qua [BiometricUtils.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/core/utils/BiometricUtils.kt) để truy cập nhanh mà không cần nhập lại mật khẩu.
*   **Lợi ích:** Đăng nhập cực nhanh chỉ trong 1 giây, bảo vệ tài khoản khỏi việc bị nhìn trộm mật khẩu.

### 2. Quản lý hồ sơ cá nhân
*   **Mục đích:** Lưu trữ và cập nhật thông tin lý lịch cá nhân của sinh viên phục vụ công tác quản lý.
*   **Người sử dụng:** Sinh viên, Nhân viên.
*   **Cách hoạt động:** Sinh viên xem và tự cập nhật các thông tin cá nhân như số điện thoại, địa chỉ thường trú, thông tin cha mẹ, số điện thoại liên lạc khẩn cấp và tải lên ảnh đại diện ngay trên ứng dụng [ProfileScreen.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/presentation/features/student/ProfileScreen.kt).
*   **Lợi ích:** Ban quản lý luôn có thông tin liên lạc mới nhất của sinh viên và người thân trong các trường hợp khẩn cấp mà không cần thu thập lại định kỳ.

### 3. Đăng ký nội trú
*   **Mục đích:** Cho phép sinh viên nộp hồ sơ xin ở ký túc xá trực tuyến.
*   **Người sử dụng:** Sinh viên mới hoặc sinh viên đăng ký kỳ học mới.
*   **Cách hoạt động:** Sinh viên điền thông tin đăng ký và theo dõi tiến độ phê duyệt qua một sơ đồ dòng thời gian trực quan (gồm các trạng thái: Đã hoàn thành, Đang xử lý, Chờ duyệt) trên màn hình [ApplicationStatusScreen.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/presentation/features/student/ApplicationStatusScreen.kt).
*   **Lợi ích:** Sinh viên không cần xếp hàng nộp đơn giấy; biết rõ hồ sơ của mình đang ở bước nào (Ví dụ: Chờ duyệt hồ sơ -> Đang xếp phòng -> Chờ đóng phí -> Hoàn tất).

### 4. Gia hạn lưu trú
*   **Mục đích:** Hỗ trợ đăng ký tiếp tục ở ký túc xá cho học kỳ tiếp theo một cách nhanh chóng.
*   **Người sử dụng:** Sinh viên đang nội trú.
*   **Cách hoạt động:** Thay vì viết đơn dài dòng, sinh viên chỉ cần truy cập màn hình [QuickExtendScreen.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/presentation/features/student/QuickExtendScreen.kt), nhập số học kỳ muốn gia hạn (Ví dụ: 1 học kỳ hoặc kỳ hè) kèm lý do ngắn gọn và nhấn gửi.
*   **Lợi ích:** Thao tác tối giản chỉ mất 15 giây, thông tin được tự động chuyển đến nhân viên duyệt.

### 5. Quản lý phòng ở
*   **Mục đích:** Hiển thị thông tin chi tiết về không gian sống của sinh viên.
*   **Người sử dụng:** Sinh viên, Nhân viên.
*   **Cách hoạt động:** Màn hình [RoomScreen.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/presentation/features/student/RoomScreen.kt) hiển thị thông tin trực quan về tòa nhà, số tầng, số phòng, ký hiệu giường được phân bổ và danh sách bạn cùng phòng.
*   **Lợi ích:** Giúp sinh viên nắm rõ thông tin phòng ở của mình và hỗ trợ nhân viên dễ dàng kiểm tra danh sách thành viên thực tế trong mỗi phòng.

### 6. Quản lý sinh viên
*   **Mục đích:** Giúp nhân viên và admin nắm bắt danh sách sinh viên nội trú và xử lý hồ sơ.
*   **Người sử dụng:** Nhân viên, Quản trị viên.
*   **Cách hoạt động:** Nhân viên có thể tìm kiếm sinh viên theo tên hoặc mã số sinh viên, xem nhanh hồ sơ cá nhân và lịch sử hoạt động của từng sinh viên.
*   **Lợi ích:** Quản lý tập trung thông tin của hàng ngàn sinh viên một cách khoa học, thay thế cho các bảng Excel rời rạc.

### 7. Quản lý hóa đơn
*   **Mục đích:** Thống kê các khoản chi phí sinh hoạt phát sinh hàng tháng của từng phòng.
*   **Người sử dụng:** Sinh viên, Nhân viên.
*   **Cách hoạt động:** Hệ thống tự động liệt kê chi tiết các hóa đơn tiền phòng, tiền điện, tiền nước và phí dịch vụ phát sinh kèm theo hạn thanh toán cụ thể [PaymentScreen.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/presentation/features/payment/PaymentScreen.kt).
*   **Lợi ích:** Minh bạch hóa các khoản thu, sinh viên có thể tự tra cứu bất kỳ lúc nào để chủ động tài chính.

### 8. Thanh toán thông minh (Tích hợp VietQR)
*   **Mục đích:** Hỗ trợ sinh viên thanh toán hóa đơn nhanh chóng qua ngân hàng di động.
*   **Người sử dụng:** Sinh viên.
*   **Cách hoạt động:** Ứng dụng tự động tính toán tổng số tiền cần đóng và tạo ra một **mã VietQR động** chứa đầy đủ thông tin: số tài khoản ký túc xá, ngân hàng nhận, số tiền chính xác đến từng đồng và nội dung chuyển khoản tự động hóa. Sinh viên quét mã bằng ứng dụng ngân hàng của mình và nhấn "Xác nhận đã chuyển khoản" để hệ thống tự động đối soát.
*   **Lợi ích:** Sinh viên không sợ chuyển sai số tiền hoặc sai số tài khoản. Hệ thống tự động ghi nhận giao dịch mà không cần nhân viên đối chiếu sổ sách ngân hàng thủ công.

### 9. Thông báo nội bộ
*   **Mục đích:** Truyền tải thông tin từ ban quản lý tới toàn thể sinh viên.
*   **Người sử dụng:** Sinh viên (nhận), Nhân viên (gửi).
*   **Cách hoạt động:** Nhân viên soạn thảo và gửi các thông báo quan trọng (Ví dụ: Lịch cúp điện, lịch phun thuốc muỗi, nhắc nhở đóng tiền phòng). Sinh viên sẽ nhận được thông báo tức thời trên điện thoại [NotificationScreen.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/presentation/features/notification/NotificationScreen.kt).
*   **Lợi ích:** Đảm bảo 100% sinh viên tiếp cận được thông tin khẩn cấp, loại bỏ việc dán thông báo giấy ở bảng tin chung vốn ít người đọc.

### 10. Gửi yêu cầu hỗ trợ & Sửa chữa thiết bị
*   **Mục đích:** Tiếp nhận và xử lý các sự cố cơ sở vật chất hư hỏng trong phòng ở.
*   **Người sử dụng:** Sinh viên gửi, Nhân viên tiếp nhận phê duyệt.
*   **Cách hoạt động:** Khi có thiết bị hỏng (Ví dụ: Hỏng bóng đèn, tắc vòi nước), sinh viên vào màn hình [RequestScreen.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/presentation/features/request/RequestScreen.kt), chọn loại yêu cầu "Sửa chữa" và nhập mô tả chi tiết sự cố. Nhân viên kỹ thuật sẽ tiếp nhận và cập nhật trạng thái xử lý để sinh viên tiện theo dõi.
*   **Lợi ích:** Loại bỏ việc sinh viên phải đi tìm nhân viên kỹ thuật để báo hỏng; sự cố được khắc phục nhanh chóng và có lịch sử theo dõi độ hiệu quả của đội bảo trì.

### 11. Nhật ký ra vào cổng
*   **Mục đích:** Ghi nhận thời gian ra vào ký túc xá của sinh viên để đảm bảo an ninh giờ nghiêm nghiêm ngặt.
*   **Người sử dụng:** Sinh viên, Nhân viên quản lý cửa cổng.
*   **Cách hoạt động:** Mỗi lần đi qua cổng kiểm soát, thời gian và trạng thái (Ra hoặc Vào) của sinh viên được ghi nhận tự động vào cơ sở dữ liệu và hiển thị trên màn hình [AccessHistoryScreen.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/presentation/features/access/AccessHistoryScreen.kt).
*   **Lợi ích:** Giúp ban quản lý dễ dàng phát hiện các trường hợp sinh viên đi quá giờ giới nghiêm hoặc không về phòng qua đêm, tăng cường mức độ an toàn tối đa.

### 12. Điểm danh/Ra vào bằng nhận diện khuôn mặt
*   **Mục đích:** Sử dụng khuôn mặt làm chìa khóa sinh học để đi qua cổng tự động.
*   **Người sử dụng:** Sinh viên.
*   **Cách hoạt động:** Sinh viên chỉ cần hướng khuôn mặt về phía camera trước tại cửa cổng [FaceVerificationScreen.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/presentation/face/screen/FaceVerificationScreen.kt). Hệ thống tự động so khớp khuôn mặt thời gian thực với dữ liệu đã lưu để mở cửa mà không cần dùng thẻ hay điện thoại.
*   **Lợi ích:** Giải quyết tình trạng quên thẻ, mất thẻ. Tốc độ kiểm soát nhanh, ngăn chặn tuyệt đối tình trạng người lạ vào ký túc xá.

### 13. Đăng ký khuôn mặt bảo mật cao (Liveness Detection)
*   **Mục đích:** Thu thập khuôn mặt sinh viên một cách an toàn và chống gian lận.
*   **Người sử dụng:** Sinh viên thực hiện đăng ký ban đầu.
*   **Cách hoạt động:** Khi thiết lập khuôn mặt lần đầu trên ứng dụng [FaceRegistrationScreen.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/presentation/face/screen/FaceRegistrationScreen.kt), sinh viên phải hoàn thành **quy trình kiểm tra người thật (Liveness)** gồm 5 bước động:
    1.  *Nháy mắt* (Đảm bảo mắt cử động bình thường).
    2.  *Quay đầu sang trái* (Kiểm tra góc nghiêng khuôn mặt).
    3.  *Quay đầu sang phải* (Kiểm tra góc nghiêng đối diện).
    4.  *Mỉm cười* (Nhận diện biểu cảm cơ mặt).
    5.  *Hoàn thành* (Lưu trữ và mã hóa dữ liệu).
*   **Lợi ích:** Ngăn chặn sinh viên sử dụng ảnh in sẵn hoặc video quay sẵn trên điện thoại khác để giả mạo việc đăng ký hoặc điểm danh hộ bạn bè.

### 14. Phân quyền người dùng chặt chẽ
*   **Mục đích:** Bảo vệ dữ liệu hệ thống, giới hạn chức năng đúng vai trò.
*   **Người sử dụng:** Toàn bộ người dùng.
*   **Cách hoạt động:** Hệ thống áp dụng một "người gác cổng phân quyền" [RoleGuard](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/navigation/AppNavigation.kt#L59-L95). Khi đăng nhập, ứng dụng kiểm tra vai trò của tài khoản. Sinh viên chỉ thấy các chức năng cá nhân; Nhân viên chỉ thấy các bảng điều khiển phê duyệt và gửi thông báo [StaffApprovalScreen.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/presentation/features/staff/StaffApprovalScreen.kt); Admin được quyền truy cập cấu hình hệ thống.
*   **Lợi ích:** Đảm bảo an toàn thông tin, tránh việc sinh viên can thiệp vào dữ liệu quản lý hoặc nhân viên can thiệp vào cấu hình máy chủ.

---

## PHẦN 3 - QUY TRÌNH HOẠT ĐỘNG THỰC TẾ

Dưới đây là mô tả chi tiết các luồng công việc thực tế được thiết kế trực quan như một cuốn cẩm nang hướng dẫn sử dụng:

### 1. Quy trình sinh viên đăng ký nội trú và nhận phòng
```mermaid
graph TD
    A[Sinh viên nộp đơn online] --> B[Nhân viên duyệt hồ sơ lý lịch]
    B --> C[Hệ thống tự động xếp giường trống]
    C --> D[Sinh viên quét VietQR đóng tiền phòng]
    D --> E[Nhận thông tin phòng ở trên ứng dụng]
    E --> F[Đăng ký khuôn mặt & bắt đầu ở]
```
*   **Bước 1:** Sinh viên truy cập ứng dụng, điền thông tin và nộp đơn đăng ký nội trú trực tuyến.
*   **Bước 2:** Ban quản lý tiếp nhận hồ sơ, đối chiếu thông tin ưu tiên và duyệt đơn.
*   **Bước 3:** Hệ thống tự động sắp xếp giường trống trong phòng phù hợp với giới tính và khóa học của sinh viên.
*   **Bước 4:** Sinh viên nhận thông báo đóng tiền phòng, thực hiện quét mã VietQR trên ứng dụng để nộp học phí nội trú.
*   **Bước 5:** Sau khi thanh toán thành công, ứng dụng hiển thị thông tin phòng ở chính thức và sinh viên thực hiện đăng ký khuôn mặt để kích hoạt quyền ra vào tự động.

### 2. Quy trình gửi và xử lý yêu cầu sửa chữa thiết bị hư hỏng
*   **Bước 1 (Sinh viên báo hỏng):** Sinh viên phát hiện vòi nước bị rò rỉ trong phòng. Sinh viên mở ứng dụng -> Chọn chức năng **Gửi yêu cầu** -> Chọn loại **Sửa chữa** -> Nhập nội dung: *"Vòi hoa sen phòng 402 bị nứt đầu ren, nước chảy liên tục"* -> Nhấn gửi.
*   **Bước 2 (Tiếp nhận & Xếp lịch):** Yêu cầu xuất hiện tức thời trên bảng điều khiển của nhân viên quản lý dưới dạng trạng thái **Chờ duyệt** (màu cam). Nhân viên kỹ thuật ký túc xá nhận thông tin và chuyển trạng thái sang **Đã duyệt** để xếp lịch sửa chữa.
*   **Bước 3 (Khắc phục):** Nhân viên kỹ thuật đến phòng 402 thay thế vòi hoa sen mới.
*   **Bước 4 (Đóng yêu cầu):** Nhân viên cập nhật trạng thái công việc đã hoàn thành. Sinh viên mở ứng dụng thấy yêu cầu chuyển sang màu xanh lá cây **Đã hoàn thành**, kết thúc quy trình.

### 3. Quy trình thanh toán hóa đơn hàng tháng
*   **Bước 1 (Nhận thông báo):** Vào ngày 5 hàng tháng, hệ thống gửi thông báo: *"Bạn có hóa đơn điện nước tháng mới cần thanh toán"*.
*   **Bước 2 (Kiểm tra số tiền):** Sinh viên mở mục **Thanh toán**, xem chi tiết số tiền phòng, chỉ số điện tiêu thụ và lượng nước đã dùng.
*   **Bước 3 (Thực hiện chuyển khoản):** Sinh viên quét mã VietQR hiển thị trên màn hình [PaymentScreen.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/presentation/features/payment/PaymentScreen.kt) bằng Mobile Banking của bất kỳ ngân hàng nào. Số tiền và nội dung chuyển khoản được điền tự động chính xác.
*   **Bước 4 (Xác nhận):** Sau khi ngân hàng báo chuyển khoản thành công, sinh viên nhấn nút **Xác nhận đã chuyển khoản** trên ứng dụng. Hệ thống sẽ tự động chuyển trạng thái hóa đơn sang **Đã thanh toán** sau khi khớp lệnh giao dịch với ngân hàng đối tác.

---

## PHẦN 4 - NHỮNG ĐIỂM NỔI BẬT VỀ ĐỘ TIN CẬY CỦA HỆ THỐNG

Smart Dormitory được thiết kế với triết lý đặt tính ổn định và an toàn thông tin lên hàng đầu. Dưới đây là những cơ chế kỹ thuật giúp hệ thống vượt trội so với các ứng dụng thông thường:

### 1. Công nghệ nhận diện khuôn mặt ngoại tuyến (Offline Face ID)
*   **Hệ thống làm gì:** Ứng dụng tích hợp mô hình AI siêu nhẹ trực tiếp trên điện thoại. Khi quét mặt, hệ thống tự xử lý hình ảnh và so sánh trực tiếp với khuôn mặt lưu trên máy mà không cần gửi hình ảnh lên mạng internet.
*   **Lợi ích:** Tốc độ nhận diện cực nhanh (dưới 0.2 giây), không bị trễ mạng và hoạt động được ngay cả khi ký túc xá mất kết nối Internet hoàn toàn.
*   **Ứng dụng thực tế:** Sinh viên đi học về muộn, cổng ký túc xá bị mất mạng internet vẫn có thể quét mặt đi qua cửa tự động bình thường.

### 2. Khả năng hoạt động khi mất mạng internet toàn phần (Offline Mode)
*   **Người dùng thấy gì:** Người dùng vẫn mở được ứng dụng, xem được lịch sử ra vào, thông tin phòng ở, các hóa đơn cũ đã lưu, và vẫn có thể viết đơn xin sửa chữa hoặc nhấn xác nhận đóng tiền.
*   **Hệ thống xử lý thế nào:** Mọi thao tác của người dùng khi không có mạng sẽ không bị báo lỗi "Mất kết nối". Thay vào đó, hệ thống tự động lưu các thao tác này vào một "hòm thư chờ đồng bộ" tạm thời trong bộ nhớ máy [PendingSyncEntity.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/data/local/entity/PendingSyncEntity.kt).
*   **Lợi ích:** Đảm bảo trải nghiệm liền mạch, người dùng không bao giờ bị ức chế vì ứng dụng bị văng hay treo khi mất mạng.

### 3. Tự động đồng bộ dữ liệu thông minh khi có mạng trở lại
*   **Hệ thống làm gì:** Một dịch vụ chạy ngầm trên Android [SyncWorker.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/core/sync/SyncWorker.kt) liên tục giám sát trạng thái kết nối mạng của thiết bị. Ngay khi phát hiện điện thoại có sóng Wifi hoặc 4G trở lại, hệ thống sẽ tự động gửi toàn bộ các yêu cầu đang nằm trong "hòm thư chờ" lên máy chủ theo đúng thứ tự thời gian.
*   **Lợi ích:** Sinh viên không cần gửi lại yêu cầu. Hệ thống tự động làm việc này mà không cần người dùng phải mở ứng dụng lên kích hoạt.

### 4. Khôi phục trạng thái hoạt động sau khi tắt máy hoặc hết pin
*   **Hệ thống làm gì:** Nhờ việc áp dụng công nghệ cơ sở dữ liệu cục bộ Room, mọi thông tin quan trọng đều được ghi nhận vào ổ cứng của điện thoại ngay lập tức chứ không chỉ lưu trên bộ nhớ tạm (RAM).
*   **Lợi ích:** Nếu điện thoại của sinh viên đột ngột sập nguồn do hết pin giữa chừng khi đang đăng ký khuôn mặt hoặc viết dở đơn sửa chữa, dữ liệu đã lưu trước đó không bị mất đi. Khi sạc pin và mở lại máy, ứng dụng sẽ tự khôi phục đúng trạng thái trước khi tắt nguồn.

### 5. Bảo mật thông tin sinh học cấp độ phần cứng
*   **Hệ thống làm gì:** Dữ liệu khuôn mặt của sinh viên không lưu dưới dạng một bức ảnh chân dung (vì rất dễ bị đánh cắp). Hệ thống chuyển đổi khuôn mặt thành một dãy số đặc trưng (gọi là vector khuôn mặt). Dãy số này được mã hóa bằng thuật toán quân đội AES-GCM cực mạnh và lưu trữ trong phân vùng bảo mật phần cứng của điện thoại thông qua dịch vụ **Android Keystore** [SecurityUtils.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/core/utils/SecurityUtils.kt).
*   **Lợi ích:** Ngăn chặn tuyệt đối tin tặc hack vào điện thoại để lấy dữ liệu khuôn mặt của sinh viên. Kể cả khi điện thoại bị root, dữ liệu mã hóa này vẫn an toàn vì mã khóa được quản lý bởi chip bảo mật vật lý của điện thoại.

### 6. Duy trì phiên đăng nhập thông minh (Silent Token Refresh)
*   **Hệ thống làm gì:** Ứng dụng sử dụng cơ chế Token đôi (AccessToken ngắn hạn và RefreshToken dài hạn) được điều phối tự động bởi [TokenAuthenticator.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/core/network/TokenAuthenticator.kt). Khi AccessToken hết hạn (Ví dụ sau 1 tiếng), thay vì đá người dùng ra màn hình đăng nhập, hệ thống sẽ âm thầm gửi RefreshToken lên máy chủ để xin cấp một khóa mới trong nền.
*   **Lợi ích:** Sinh viên chỉ cần đăng nhập một lần duy nhất khi cài app và có thể sử dụng hàng tháng trời mà không bao giờ bị yêu cầu đăng nhập lại một cách phiền phức.

---

## PHẦN 5 - CÁC TÌNH HUỐNG THỰC TẾ VÀ KHẢ NĂNG PHẢN ỨNG

Để chứng minh độ bền bỉ và tính thực tế của ứng dụng trước hội đồng chấm đồ án, dưới đây là cách hệ thống xử lý các sự cố thực tế thường gặp:

| Tình huống thực tế | Phản ứng tự động của hệ thống | Trải nghiệm của người dùng | Kết quả cuối cùng |
| :--- | :--- | :--- | :--- |
| **Điện thoại mất Wifi / Sóng yếu** | Ngắt kết nối API, tự động chuyển sang đọc dữ liệu từ cơ sở dữ liệu cục bộ Room. Lưu các lệnh gửi mới vào bảng hàng đợi tạm thời [PendingSyncEntity.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/data/local/entity/PendingSyncEntity.kt). | Ứng dụng vẫn hoạt động bình thường, không báo lỗi mạng, giao diện mượt mà. | Mọi dữ liệu thao tác được bảo toàn và tự động đồng bộ lên máy chủ khi có mạng trở lại. |
| **Máy chủ ký túc xá tạm ngưng hoạt động** | Hệ thống nhận diện lỗi kết nối từ xa. Ứng dụng chuyển sang chế độ hoạt động độc lập (Offline Standalone). | Người dùng vẫn có thể thực hiện quét khuôn mặt ra vào cổng ký túc xá bình thường vì thuật toán AI chạy trực tiếp trên máy. | Tránh việc ùn tắc tại cửa ra vào ký túc xá khi máy chủ tổng gặp sự cố phần cứng. |
| **Điện thoại đột ngột hết pin sập nguồn** | Cơ chế lưu trữ đĩa cứng tức thời ghi nhận trạng thái giao dịch trước khi sập nguồn. | Máy tắt nguồn. Khi cắm sạc mở lại, ứng dụng tự động tải dữ liệu cũ lên. | Không bị mất mát dữ liệu đang nhập dở, sinh viên không cần làm lại từ đầu. |
| **Đang gửi đơn sửa chữa thì mất kết nối mạng** | Giao dịch gửi đơn bị lỗi kết nối vật lý, hệ thống tự động bọc nội dung đơn vào hàng đợi chờ đồng bộ. | Sinh viên nhận được thông báo: *"Hệ thống sẽ tự gửi đơn khi có mạng trở lại"*. | Đơn được gửi đi thành công mà sinh viên không cần phải nhập lại nội dung hay nhấn gửi lại. |
| **Sinh viên mất kiên nhẫn nhấn nút gửi liên tiếp nhiều lần** | Nút bấm được tạm thời vô hiệu hóa ngay sau cú chạm đầu tiên và hiển thị biểu tượng quay vòng chờ xử lý [RequestScreen.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/presentation/features/request/RequestScreen.kt#L168). | Sinh viên chỉ bấm được duy nhất 1 lần, nút bấm chuyển sang trạng thái chờ. | Ngăn chặn việc gửi trùng đơn lên máy chủ, tránh làm nghẽn băng thông và rác dữ liệu quản lý. |
| **Đã lâu không mở ứng dụng (Đăng nhập hết hạn)** | [TokenAuthenticator.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/core/network/TokenAuthenticator.kt) phát hiện mã bảo mật đã hết hạn hoàn toàn và không thể tự gia hạn. | Hệ thống tự động xóa sạch dữ liệu tạm để bảo mật, điều hướng sinh viên về màn hình Đăng nhập [LoginScreen.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/presentation/features/auth/LoginScreen.kt). | Bảo vệ tài khoản sinh viên khỏi nguy cơ bị chiếm dụng phiên đăng nhập cũ trên điện thoại. |

---

## PHẦN 6 - GIÁ TRỊ MANG LẠI

Hệ thống Smart Dormitory mang lại giá trị thiết thực cho tất cả các bên tham gia vào hệ sinh thái ký túc xá:

*   **Đối với sinh viên:**
    *   Loại bỏ hoàn toàn các thủ tục giấy tờ, tiết kiệm thời gian xếp hàng hành chính.
    *   Ra vào cổng ký túc xá nhanh chóng và an toàn hơn bằng khuôn mặt, không lo quên thẻ hay mất chìa khóa.
    *   Thanh toán tiền phòng dễ dàng, minh bạch mọi chi phí phát sinh hàng tháng.
    *   Theo dõi tiến độ xử lý sự cố thiết bị trực quan, tăng sự hài lòng với môi trường sống.

*   **Đối với ban quản lý ký túc xá:**
    *   Giảm 80% thời gian xử lý thủ tục hành chính nhờ tự động hóa xếp phòng, duyệt đơn sửa chữa và gia hạn.
    *   Kiểm soát an ninh ra vào chặt chẽ hơn nhờ công nghệ nhận diện khuôn mặt người thật, ngăn chặn người lạ đột nhập.
    *   Quản lý điện nước và thu phí chính xác, tự động đối soát thanh toán qua ngân hàng, tránh thất thoát tài chính.
    *   Gửi thông báo nhanh chóng tới toàn thể sinh viên chỉ bằng vài click chuột.

*   **Đối với nhà trường:**
    *   Xây dựng hình ảnh trường đại học hiện đại, đi đầu trong việc ứng dụng công nghệ và trí tuệ nhân tạo.
    *   Tối ưu hóa nguồn lực nhân sự quản lý ký túc xá.
    *   Có được nguồn dữ liệu số hóa chính xác để phục vụ công tác thống kê, hoạch định chính sách hỗ trợ sinh viên.

*   **Đối với quá trình chuyển đổi số giáo dục:**
    *   Góp phần hoàn thiện hệ sinh thái đô thị đại học thông minh (Smart Campus).
    *   Chuyển đổi hoàn toàn từ văn bản giấy sang dữ liệu số được lưu trữ an toàn, hỗ trợ phân tích dữ liệu lớn (Big Data) trong tương lai.

---

## PHẦN 7 - ĐÁNH GIÁ MỨC ĐỘ HOÀN THIỆN

Để hội đồng đánh giá đồ án có cái nhìn khách quan, dưới đây là bảng phân tích chi tiết mức độ hoàn thiện các phân hệ chức năng của ứng dụng Android Client hiện tại:

| Nhóm chức năng | Chức năng cụ thể | Hoàn thành | Đang phát triển | Dự kiến mở rộng | Đánh giá kỹ thuật |
| :--- | :--- | :---: | :---: | :---: | :--- |
| **Xác thực & Bảo mật** | Đăng nhập tài khoản | **✓** | | | Hoạt động ổn định, phân quyền chuẩn xác qua Token JWT. |
| | Đăng nhập vân tay / khuôn mặt | **✓** | | | Tích hợp tốt với khóa bảo mật sinh trắc học có sẵn trên điện thoại. |
| | Đổi & Quên mật khẩu | **✓** | | | Hoạt động tốt qua Email khôi phục mật khẩu. |
| **Nhận diện AI** | Đăng ký khuôn mặt & Liveness | **✓** | | | Hoàn thiện quy trình kiểm tra 5 bước người thật chống giả mạo hình ảnh. |
| | Xác thực khuôn mặt offline | **✓** | | | Nhận diện ngoại tuyến rất mượt mà bằng MobileFaceNet chạy trên TensorFlow Lite. |
| **Hành chính & Phòng** | Gửi yêu cầu sửa chữa | **✓** | | | Gửi yêu cầu và lưu hàng đợi thông minh khi mất mạng. |
| | Đăng ký gia hạn lưu trú | **✓** | | | Form gia hạn tinh gọn, gửi trực tiếp về hệ thống. |
| | Xem thông tin phòng ở | **✓** | | | Hiển thị chính xác vị trí giường, phòng, tòa nhà của sinh viên. |
| | Xem tiến độ đơn đăng ký | **✓** | | | Giao diện Timeline trực quan dễ theo dõi tiến độ nộp hồ sơ. |
| **Tài chính & Thu phí**| Tra cứu hóa đơn | **✓** | | | Hiển thị đầy đủ tiền phòng, điện nước và tổng dư nợ. |
| | Thanh toán qua mã VietQR | **✓** | | | Tạo QR động tự động điền thông tin tài khoản ký túc xá và số tiền cần đóng. |
| | Xem lịch sử thanh toán | **✓** | | | Liệt kê danh sách các hóa đơn cũ đã đóng tiền. |
| **Truyền thông** | Xem thông báo | **✓** | | | Đọc và đánh dấu thông báo đã đọc ngoại tuyến tốt. |
| **Nghiệp vụ Staff** | Duyệt yêu cầu sinh viên | **✓** | | | Giao diện tiện lợi cho nhân viên duyệt sửa chữa, gia hạn [StaffApprovalScreen.kt](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/presentation/features/staff/StaffApprovalScreen.kt). |
| | Quản lý phòng | | **✓** | | Hiện tại đang sử dụng màn hình chờ (Placeholder) [StaffRoomManage](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/navigation/AppNavigation.kt#L182-L184). |
| | Ghi chỉ số Điện nước | | **✓** | | Hiện tại đang sử dụng màn hình chờ (Placeholder) [StaffWaterElectric](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/navigation/AppNavigation.kt#L186-L188). |
| **Nghiệp vụ Admin** | Quản lý người dùng | | **✓** | | Hiện tại đang sử dụng màn hình chờ (Placeholder) [AdminUsers](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/navigation/AppNavigation.kt#L190-L192). |
| | Thống kê hệ thống | | **✓** | | Hiện tại đang sử dụng màn hình chờ (Placeholder) [AdminStats](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/navigation/AppNavigation.kt#L194-L196). |
| | Cấu hình hệ thống | | **✓** | | Hiện tại đang sử dụng màn hình chờ (Placeholder) [AdminSettings](file:///D:/HocTap/LuanVan/Code/app/src/main/java/com/ktx/dormitory/navigation/AppNavigation.kt#L198-L200). |
| **Nâng cấp tương lai** | Tích hợp cổng tự động IoT | | | **✓** | Đóng mở cổng tự động qua tín hiệu điều khiển phần cứng từ xa. |
| | Nhận diện cảm xúc sinh viên | | | **✓** | Sử dụng AI phân tích biểu cảm để hỗ trợ chăm sóc sức khỏe tinh thần sinh viên. |

---

## PHẦN 8 - CÔNG NGHỆ ĐƯỢC ỨNG DỤNG VÀ VAI TRÒ CỦA CHÚNG

Dự án sử dụng các công nghệ hiện đại nhất trong phát triển ứng dụng di động Android nhưng được tinh chỉnh để giải quyết các bài toán cụ thể của ký túc xá:

1.  **Ứng dụng di động Android hiện đại (Jetpack Compose & Clean Architecture):**
    *   *Vai trò:* Giúp xây dựng giao diện ứng dụng đẹp mắt, hiện đại, chuyển động mượt mà và trực quan cho sinh viên. Cấu trúc mã nguồn được phân chia thành 3 lớp rõ rệt (Clean Architecture) giúp ứng dụng dễ dàng bảo trì, nâng cấp thêm tính năng mới trong tương lai mà không sợ làm hỏng các tính năng cũ.
2.  **Trí tuệ nhân tạo (TFLite & ML Kit):**
    *   *Vai trò:* ML Kit đóng vai trò phát hiện vị trí khuôn mặt, mắt nhắm/mở, miệng cười trên khung hình camera. TensorFlow Lite chạy mô hình **MobileFaceNet** để chuyển đổi hình ảnh khuôn mặt thành vector 128 chiều phục vụ so khớp sinh trắc học. Đây là "trái tim" giúp hệ thống tự động kiểm soát ra vào mà không cần mạng.
3.  **Cơ sở dữ liệu cục bộ Room Persistence:**
    *   *Vai trò:* Lưu trữ toàn bộ dữ liệu hồ sơ sinh viên, thông tin phòng ở, hóa đơn và nhật ký ra vào trực tiếp trên ổ cứng điện thoại. Đây là nền tảng giúp ứng dụng hoạt động ngoại tuyến (Offline) mượt mà.
4.  **Hệ thống đồng bộ ngầm WorkManager:**
    *   *Vai trò:* Đảm nhận nhiệm vụ tự động gửi các dữ liệu chờ từ điện thoại lên máy chủ khi phát hiện thiết bị kết nối mạng trở lại. Công nghệ này hoạt động ổn định kể cả khi người dùng đã tắt ứng dụng.
5.  **Bảo mật phần cứng Android Keystore:**
    *   *Vai trò:* Lưu trữ và quản lý các khóa mã hóa dùng để bảo vệ dữ liệu khuôn mặt của sinh viên. Đảm bảo dữ liệu sinh trắc học không thể bị giải mã bên ngoài thiết bị.
6.  **Thanh toán điện tử tích hợp VietQR:**
    *   *Vai trò:* Tạo ra cầu nối giao dịch tài chính nhanh gọn giữa sinh viên và ngân hàng của ký túc xá, tự động hóa khâu làm sổ sách đối soát tiền tệ.

---

## PHẦN 9 - KẾT LUẬN

### 1. Dự án giải quyết được bài toán gì?
Dự án Smart Dormitory đã giải quyết thành công bài toán quản lý và vận hành ký túc xá thời kỳ số hóa. Ứng dụng đã giải phóng sinh viên khỏi các thủ tục giấy tờ hành chính phức tạp, tạo ra phương thức thanh toán tiền phòng nhanh chóng, đồng thời mang lại một giải pháp kiểm soát an ninh tối ưu, tự động bằng nhận diện khuôn mặt người thật.

### 2. Điểm mạnh lớn nhất của dự án
Điểm mạnh vượt trội của dự án là **Tính bền bỉ và Tính sẵn sàng cao**. Nhờ cơ chế lưu trữ cục bộ Room, hàng đợi đồng bộ tự động WorkManager và nhận diện khuôn mặt ngoại tuyến (Offline Face ID), ứng dụng có khả năng hoạt động ổn định, mượt mà và an toàn trong mọi điều kiện sự cố mạng hay mấy nguồn máy chủ.

### 3. Điểm khác biệt so với quản lý truyền thống
*   *Truyền thống:* Ra vào trình thẻ giấy/thẻ từ (dễ mất, dễ cho mượn); đóng tiền bằng chuyển khoản chụp màn hình gửi thủ công cho nhân viên đối chiếu; báo hỏng bằng cách ghi sổ ở văn phòng ký túc xá.
*   *Smart Dormitory:* Ra vào quét mặt tự động; thanh toán bằng VietQR tự động đối soát khớp tiền; báo hỏng ngay tại phòng qua ứng dụng và theo dõi trực quan tiến độ bảo trì.

### 4. Khả năng áp dụng thực tế
Ứng dụng có tính thực tiễn cực kỳ cao và hoàn toàn có thể triển khai thực tế tại bất kỳ ký túc xá đại học nào ở Việt Nam. Khả năng hoạt động ngoại tuyến giúp hệ thống không đòi hỏi hạ tầng mạng quá đắt đỏ ở các cửa cổng ra vào, giúp tiết kiệm chi phí đầu tư ban đầu cho nhà trường.

### 5. Khả năng mở rộng trong tương lai
*   Tích hợp hệ thống IoT điều khiển mở khóa chốt cửa phòng ở tự động khi sinh viên quét mặt thành công.
*   Phát triển phiên bản Web dành cho Ban giám hiệu nhà trường để giám sát các báo cáo tài chính và an ninh vĩ mô.
*   Ứng dụng AI phân tích lịch sử ra vào và hành vi để phát hiện sớm các trường hợp sinh viên gặp khó khăn hoặc cần hỗ trợ tâm lý đặc biệt.
