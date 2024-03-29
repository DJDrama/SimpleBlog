package com.example.simpleblog.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = KotlinLogging.logger { }

    /**
     * @sample
     * {
     *   "errors": [
     *     {
     *       "field": "title",
     *       "value": "",
     *       "reason": "require title"
     *     }
     *   ],
     *   "code": "C001",
     *   "message": "Invalid input value"
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        log.error {
            "handleMethodArgumentNotValidException $e"
        }

        val of = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.bindingResult)
        return ResponseEntity(of, HttpStatus.BAD_REQUEST)
    }

    /**
     * @sample
     * {
     *   "errors": [],
     *   "code": "C002",
     *   "message": "Entity not found."
     * }
     */
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(e: EntityNotFoundException): ResponseEntity<ErrorResponse> {
        log.error {
            "handleEntityNotFoundException $e"
        }

        val of = ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND)
        return ResponseEntity(of, HttpStatus.INTERNAL_SERVER_ERROR)
    }

}