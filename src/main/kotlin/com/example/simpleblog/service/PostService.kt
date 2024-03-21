package com.example.simpleblog.service

import com.example.simpleblog.domain.member.MemberRes
import com.example.simpleblog.domain.member.MemberSaveReq
import com.example.simpleblog.domain.member.asDtoModel
import com.example.simpleblog.domain.member.asEntityModel
import com.example.simpleblog.domain.post.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository
) {
    @Transactional(readOnly = true)
    fun findPosts(pageable: Pageable): Page<PostRes> {
        return postRepository.findAll(pageable).map { it.asDtoModel() }
    }

    @Transactional
    fun savePost(postSaveReq: PostSaveReq): PostRes {
        return postRepository.save(postSaveReq.asEntityModel()).asDtoModel()
    }

    @Transactional
    fun deletePost(id: Long) {
        return postRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun findPostById(id: Long): PostRes {
        return postRepository.findById(id).orElseThrow().asDtoModel()
    }
}