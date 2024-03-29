package com.example.simpleblog.domain.comment

import com.example.simpleblog.domain.AuditingEntity
import com.example.simpleblog.domain.member.Member
import com.example.simpleblog.domain.post.Post
import jakarta.persistence.*

@Entity
@Table(name = "Comment")
data class Comment(
    @Column(name = "content", nullable = false)
    val content: String,

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Post::class)
    val post: Post,

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Member::class)
    val member: Member
) : AuditingEntity()
