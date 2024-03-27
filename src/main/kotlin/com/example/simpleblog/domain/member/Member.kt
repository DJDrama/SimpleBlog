package com.example.simpleblog.domain.member

import com.example.simpleblog.domain.AuditingEntity
import com.example.simpleblog.domain.post.Post
import jakarta.persistence.*

@Entity
@Table(name = "Member")
data class Member(
    @Column(name = "email", nullable = false)
    val email: String,
    @Column(name = "password", nullable = false)
    val password: String,

    @Enumerated(EnumType.STRING)
    val role: Role,

) : AuditingEntity()

enum class Role {
    USER, ADMIN
}

fun createFakeMember(memberId: Long): Member {
    val member = Member("", "", Role.USER)
    member.id = memberId
    return member
}

fun Member.asDtoModel(): MemberRes {
    return MemberRes(
        id = this.id ?: 0L,
        email = this.email,
        password = this.password,
        role = this.role
    )
}