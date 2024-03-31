package com.example.simpleblog.api

import com.example.simpleblog.domain.member.MemberRes
import com.example.simpleblog.domain.member.LoginDto
import com.example.simpleblog.service.MemberService
import com.example.simpleblog.util.value.CommonResultDto
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1")
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/members")
    fun findAllMembers(@PageableDefault(page = 0, size = 10) pageable: Pageable,
                       session: HttpSession): CommonResultDto<Page<MemberRes>> {
        return CommonResultDto(
            resultCode = HttpStatus.OK,
            resultMsg = "find All Members",
            data = memberService.findAll(pageable)
        )
    }

    @GetMapping("/member/{id}")
    fun findMemberById(@PathVariable id: Long): CommonResultDto<MemberRes> {
        return CommonResultDto(
            resultCode = HttpStatus.OK,
            resultMsg = "find member by id",
            data = memberService.findMemberById(id = id)
        )
    }

    @DeleteMapping("/member/{id}")
    fun deleteMemberById(@PathVariable id: Long): CommonResultDto<Unit> {
        return CommonResultDto(
            resultCode = HttpStatus.OK,
            resultMsg = "delete member by id",
            data = memberService.deleteMember(id = id)
        )
    }

    @PostMapping("/member")
    fun saveMember(@Valid @RequestBody memberSaveReq: LoginDto): CommonResultDto<MemberRes> {
        return CommonResultDto(
            resultCode = HttpStatus.CREATED,
            resultMsg = "save member",
            data = memberService.saveMember(memberSaveReq = memberSaveReq)
        )
    }

}