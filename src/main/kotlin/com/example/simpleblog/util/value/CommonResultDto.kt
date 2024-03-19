package com.example.simpleblog.util.value

import org.springframework.http.HttpStatus

data class CommonResultDto<out T>(
    val resultCode: HttpStatus,
    val resultMsg: String,
    val data: T
)