package com.example.simpleblog.domain.member

data class MemberSaveReq(
    val email: String,
    val password: String,
    val role: Role
)

fun MemberSaveReq.asEntityModel() = Member(
    email = this.email,
    password = this.password,
    role = this.role
)
