package com.example.simpleblog.api

import com.example.simpleblog.domain.member.Member
import com.example.simpleblog.service.MemberService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/members")
    fun findAllMembers(): List<Member>{
        return memberService.findAll()
    }


}