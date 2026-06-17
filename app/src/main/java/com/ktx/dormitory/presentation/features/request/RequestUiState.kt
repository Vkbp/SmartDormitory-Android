package com.ktx.dormitory.presentation.features.request

import com.ktx.dormitory.domain.model.RequestType

data class RequestFormState(
    val type: RequestType = RequestType.REPAIR,
    val content: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)