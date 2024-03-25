package com.example.simpleblog.exception

import org.springframework.validation.BindingResult

class ErrorResponse(
    errorCode: ErrorCode,
    val errors: List<FieldError> = emptyList()
) {

    var code: String = errorCode.code
        private set

    var message: String = errorCode.message
        private set

    class FieldError private constructor(
        val field: String,
        val value: String,
        val reason: String?
    ) {
        companion object {
            fun of(bindingResult: BindingResult): List<FieldError> {
                val fieldError = bindingResult.fieldErrors
                return fieldError.map {
                    FieldError(
                        field = it.field,
                        value = it.rejectedValue?.toString() ?: "",
                        reason = it.defaultMessage
                    )
                }
            }
        }
    }
    companion object{
        fun of(code: ErrorCode, bindingResult: BindingResult): ErrorResponse{
            return ErrorResponse(
                errorCode = code,
                errors = FieldError.of(bindingResult = bindingResult)
            )
        }

        fun of(code: ErrorCode): ErrorResponse {
            return ErrorResponse(
                errorCode = code,
            )
        }
    }
}