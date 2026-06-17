package com.ktx.dormitory.core

import com.ktx.dormitory.BuildConfig

object Constants {
    val BASE_URL: String = BuildConfig.BASE_URL

    // Giảm timeout xuống 5s để phát hiện lỗi IP nhanh hơn khi Demo
    const val NETWORK_TIMEOUT = 5L // giây
}
