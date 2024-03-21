package com.example.simpleblog.domain.member

import jakarta.validation.constraints.NotNull

data class MemberSaveReq(
    @field:NotNull(message = "require email")
    val email: String?,
    val password: String?,
    val role: Role?
)

fun MemberSaveReq.asEntityModel() = Member(
    email = this.email ?: "",
    password = this.password ?: "",
    role = this.role ?: Role.USER
)

data class MemberRes(
    val id: Long,
    val email: String,
    val password: String,
    val role: Role
)