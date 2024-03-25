package com.example.simpleblog.domain.post

import com.example.simpleblog.domain.member.Member
import com.example.simpleblog.domain.member.MemberRes
import com.example.simpleblog.domain.member.createFakeMember
import jakarta.validation.constraints.NotNull

data class PostSaveReq(
    @field:NotNull(message = "require title")
    val title: String?,
    val content: String?,
    @field:NotNull(message = "require memberId")
    val memberId: Long?
)

data class PostRes(
    val title: String,
    val content: String,
    val member: MemberRes
)
fun PostSaveReq.asEntityModel() = Post(
    title = this.title ?: "",
    content = this.content ?: "",
    member = createFakeMember(this.memberId!!)
)