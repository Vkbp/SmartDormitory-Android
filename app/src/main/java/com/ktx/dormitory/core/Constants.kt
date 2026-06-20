package com.ktx.dormitory.core

import com.ktx.dormitory.BuildConfig

object Constants {
    val BASE_URL: String = BuildConfig.BASE_URL

    /**
     * Timeout 15s là điểm cân bằng giữa Reliability và UX.
     * Cho phép mạng yếu xử lý được, nhưng không bắt người dùng đợi quá lâu.
     */
    const val NETWORK_TIMEOUT = 15L // giây
}
