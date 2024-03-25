package com.example.simpleblog.exception

// errors happening inside our business logic
sealed class BusinessException : RuntimeException {
    private var errorCode: ErrorCode

    constructor(errorCode: ErrorCode) : super(errorCode.message) {
        this.errorCode = errorCode
    }

    constructor(message: String?, errorCode: ErrorCode) : super(errorCode.message) {
        this.errorCode = errorCode
    }


}