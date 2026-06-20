package com.ktx.dormitory.presentation.features.request

import android.os.Parcelable
import com.ktx.dormitory.domain.model.DormRequest
import com.ktx.dormitory.domain.model.RequestType
import kotlinx.parcelize.Parcelize

@Parcelize
data class RequestUiState(
    val isLoading: Boolean = false,
    val requests: List<DormRequest> = emptyList(),
    val error: String? = null,
    val isSubmitting: Boolean = false,
    val submitSuccess: Boolean = false
) : Parcelable

@Parcelize
data class RequestFormState(
    val type: RequestType = RequestType.REPAIR,
    val content: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
) : Parcelable
