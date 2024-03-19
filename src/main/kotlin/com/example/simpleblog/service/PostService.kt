package com.example.simpleblog.service

import com.example.simpleblog.domain.post.PostRepository
import com.example.simpleblog.domain.post.PostRes
import com.example.simpleblog.domain.post.asDtoModel
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository
) {
    @Transactional(readOnly = true)
    fun findPosts(): List<PostRes> {
        return postRepository.findAll().map { it.asDtoModel()}
    }
}