package com.example.simpleblog.domain.post

import com.example.simpleblog.domain.AuditingEntity
import com.example.simpleblog.domain.member.Member
import jakarta.persistence.*

@Entity
@Table(name = "Post")
data class Post(
    @Column(name = "title", nullable = false)
    val title: String,
    @Column(name = "content"/*, nullable = true*/) // default true
    val content: String,

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Member::class)
    val member: Member

) : AuditingEntity()
