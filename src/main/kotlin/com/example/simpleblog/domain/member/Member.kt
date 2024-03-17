package com.example.simpleblog.domain.member

import com.example.simpleblog.domain.AuditingEntity
import jakarta.persistence.*

@Entity
@Table(name = "Member")
data class Member(
    @Column(name="email", nullable = false)
    val email: String,
    @Column(name="password", nullable = false)
    val password: String,

    @Enumerated(EnumType.STRING)
    val role: Role
) : AuditingEntity()

enum class Role {
    USER, ADMIN
}
