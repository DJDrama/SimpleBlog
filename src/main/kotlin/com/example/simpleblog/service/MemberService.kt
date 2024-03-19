package com.example.simpleblog.service

import com.example.simpleblog.domain.member.MemberRepository
import com.example.simpleblog.domain.member.asDtoModel
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable) = memberRepository.findAll(pageable).map {
        it.asDtoModel()
    }
}