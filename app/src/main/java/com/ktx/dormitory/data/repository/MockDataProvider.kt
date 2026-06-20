package com.ktx.dormitory.data.repository

import com.ktx.dormitory.domain.model.*

object MockDataProvider {

    fun getMockProfile() = UserProfile(
        id = "1",
        studentCode = "student01",
        fullName = "Nguyễn Văn A",
        citizenId = "079204005678",
        gender = "MALE",
        birthDate = "2004-01-15", // yyyy-MM-dd
        faculty = "Information Technology",
        course = "K20",
        phone = "0912345678",
        email = "vana@example.com",
        permanentAddress = "Hồ Chí Minh, Việt Nam",
        role = "USER",
        avatarUrl = null
    )

    fun getMockTransactions() = listOf(
        Transaction("VNP123456", 1500000.0, "BANK_TRANSFER", "SUCCESS", "2024-06-02T14:30:00"),
        Transaction("MOMO78901", 250000.0, "MOMO", "SUCCESS", "2024-05-05T10:15:00")
    )
    
    fun getMockInvoices() = listOf(
        Invoice(
            id = 1,
            type = InvoiceType.ROOM,
            amount = 1500000.0,
            paidAmount = 1500000.0,
            remainingAmount = 0.0,
            status = PaymentStatus.PAID,
            dueDate = "2024-06-15",
            description = "Tiền phòng tháng 6"
        ),
        Invoice(
            id = 2,
            type = InvoiceType.ELECTRICITY,
            amount = 320000.0,
            paidAmount = 0.0,
            remainingAmount = 320000.0,
            status = PaymentStatus.UNPAID,
            dueDate = "2024-06-20",
            description = "Tiền điện tháng 5"
        )
    )

    fun getMockApplication() = DormApplication(
        applicationCode = "APP-KTX-001",
        status = "APPROVED",
        submissionDate = "2024-10-20",
        paymentDeadline = "2024-10-30",
        timeline = listOf(
            TimelineStep("Gửi đơn", "Đơn đã nhận", "2024-10-20T08:00:00", AppStepStatus.COMPLETED)
        )
    )
    
    fun getMockRoomInfo() = RoomInfo("Tòa A1", "Tầng 3", "302", "B1", "Đang lưu trú")

    fun getMockRequests() = listOf(
        DormRequest("REQ01", "Nguyễn Văn A", "SV01", RequestType.REPAIR, "Hỏng bóng đèn", RequestStatus.APPROVED, "2024-10-26")
    )
}