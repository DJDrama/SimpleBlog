package com.example.simpleblog.api

import com.example.simpleblog.domain.member.MemberRes
import com.example.simpleblog.service.MemberService
import com.example.simpleblog.util.value.CommonResultDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/members")
    fun findAllMembers(@PageableDefault(page=0, size=10) pageable: Pageable): CommonResultDto<Page<MemberRes>>{
        return CommonResultDto(
            resultCode = HttpStatus.OK,
            resultMsg = "find All Members",
            data = memberService.findAll(pageable)
        )
    }


}