package com.example.simpleblog.domain.comment

import com.example.simpleblog.domain.member.createFakeMember
import com.example.simpleblog.domain.post.Post

data class CommentSaveReq(
    val memberId: Long,
    val content: String,
    val postId: Long
)

fun CommentSaveReq.asEntityModel(post: Post) = Comment(
    content = this.content,
    post = post,
    member = createFakeMember(this.memberId)
)