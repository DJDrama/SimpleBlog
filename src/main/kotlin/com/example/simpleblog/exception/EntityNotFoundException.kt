package com.example.simpleblog.exception

sealed class EntityNotFoundException(message: String?): BusinessException(message, ErrorCode.ENTITY_NOT_FOUND)

class MemberNotFoundException(key: String): EntityNotFoundException("$key not found!")