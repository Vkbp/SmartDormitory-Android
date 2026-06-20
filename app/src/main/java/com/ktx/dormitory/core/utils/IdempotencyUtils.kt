package com.ktx.dormitory.core.utils

import java.util.UUID

object IdempotencyUtils {
    fun generateKey(): String = UUID.randomUUID().toString()
}
