package com.ktx.dormitory.core.network

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import retrofit2.HttpException

fun Throwable.toUserFriendlyMessage(): String {
    return when (this) {
        is SocketTimeoutException -> "Kết nối quá hạn. Vui lòng kiểm tra lại đường truyền."
        is UnknownHostException -> "Không thể kết nối tới máy chủ. Vui lòng kiểm tra Internet."
        is ConnectException -> "Máy chủ đang bảo trì hoặc không phản hồi."
        is HttpException -> {
            when (this.code()) {
                401 -> "Phiên đăng nhập hết hạn hoặc thông tin không chính xác."
                403 -> "Bạn không có quyền truy cập vào chức năng này."
                404 -> "Không tìm thấy yêu cầu trên máy chủ."
                500 -> "Lỗi hệ thống phía máy chủ. Vui lòng thử lại sau."
                else -> "Lỗi kết nối: ${this.code()}"
            }
        }
        else -> this.message ?: "Đã có lỗi không xác định xảy ra."
    }
}
