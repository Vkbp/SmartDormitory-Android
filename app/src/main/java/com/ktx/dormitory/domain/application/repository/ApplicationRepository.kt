package com.ktx.dormitory.domain.application.repository

import com.ktx.dormitory.domain.application.model.DormApplication

interface ApplicationRepository {
    suspend fun getApplicationTimeline(): Result<DormApplication>
}
