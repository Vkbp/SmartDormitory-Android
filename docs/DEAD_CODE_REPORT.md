# DEAD CODE REPORT - SMART DORMITORY

## 🛑 SAFE TO DELETE
- `presentation/components/PlaceholderScreen.kt`: Không được sử dụng trong `AppNavigation` hay bất kỳ Screen nào.
- `data/repository/MockDataProvider.kt`: (Đã xóa) Không còn class nào tham chiếu đến dữ liệu giả.
- Các folder kỹ thuật cũ: `data/mapper/`, `domain/model/`, `domain/repository/`, v.v. (Đã dọn dẹp).

## ⚠️ KEEP FOR FUTURE
- `presentation/features/face/FaceDetectionScreen.kt`: Chưa có route nhưng cần thiết cho tính năng AI.
- `presentation/features/face/FaceVerificationScreen.kt`: Chưa có route nhưng là core logic của dự án.
- `presentation/features/extension/QuickExtendScreen.kt`: Đã có skeleton, chờ backend API gia hạn.

## ✅ REPOSITORY STATUS
Tất cả các repository hiện tại đều được Inject và sử dụng thông qua các UseCase tương ứng.
- `ProfileRepository` -> `GetProfileUseCase`, `UpdateProfileUseCase`, `UploadAvatarUseCase`
- `RoomRepository` -> `GetRoomInfoUseCase`
- `ApplicationRepository` -> `GetApplicationTimelineUseCase`
- `PaymentRepository` -> `GetInvoicesUseCase`, `VerifyPaymentUseCase`, `GetPaymentHistoryUseCase`
- `AccessRepository` -> `AccessViewModel`
- `AuthRepository` -> `LoginUseCase`, `LogoutUseCase`, v.v.
- `SettingsRepository` -> `FaceViewModel`
