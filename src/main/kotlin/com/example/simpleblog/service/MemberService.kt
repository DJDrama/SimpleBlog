package com.example.simpleblog.service

import com.example.simpleblog.domain.member.*
import com.example.simpleblog.exception.MemberNotFoundException
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

    @Transactional
    fun saveMember(memberSaveReq: LoginDto): MemberRes {
        return memberRepository.save(memberSaveReq.asEntityModel()).asDtoModel()
    }

    @Transactional
    fun deleteMember(id: Long) {
        return memberRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun findMemberById(id: Long): MemberRes {
        return memberRepository.findById(id).orElseThrow {
            throw MemberNotFoundException(id.toString()) // custom exception
        }.asDtoModel()
    }
}