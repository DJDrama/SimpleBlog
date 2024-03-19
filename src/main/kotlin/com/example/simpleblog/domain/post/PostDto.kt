package com.example.simpleblog.domain.post

import com.example.simpleblog.domain.member.Member
import com.example.simpleblog.domain.member.MemberRes
import com.example.simpleblog.domain.member.createFakeMember

data class PostSaveReq(
    val title: String,
    val content: String,
    val memberId: Long
)

data class PostRes(
    val title: String,
    val content: String,
    val member: MemberRes
)
fun PostSaveReq.asEntityModel() = Post(
    title = this.title,
    content = this.content,
    member = createFakeMember(this.memberId)
)