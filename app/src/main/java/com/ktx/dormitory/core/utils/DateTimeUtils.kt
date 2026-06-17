package com.ktx.dormitory.core.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val isoDateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val displayDateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun formatIsoDate(isoDate: String?): String {
        if (isoDate.isNullOrBlank()) return "N/A"
        return try {
            val date = isoDateFormat.parse(isoDate)
            if (date != null) displayDateFormat.format(date) else isoDate
        } catch (e: Exception) {
            isoDate
        }
    }

    fun formatIsoDateTime(isoDateTime: String?): String {
        if (isoDateTime.isNullOrBlank()) return "N/A"
        return try {
            val date = isoDateTimeFormat.parse(isoDateTime)
            if (date != null) displayDateTimeFormat.format(date) else isoDateTime
        } catch (e: Exception) {
            formatIsoDate(isoDateTime)
        }
    }
}