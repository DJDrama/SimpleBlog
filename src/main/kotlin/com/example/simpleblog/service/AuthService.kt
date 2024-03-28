package com.example.simpleblog.service

import com.example.simpleblog.config.security.PrincipalDetails
import com.example.simpleblog.domain.member.MemberRepository
import com.example.simpleblog.exception.MemberNotFoundException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val memberRepository: MemberRepository
) : UserDetailsService{
    private val log = KotlinLogging.logger {  }
    override fun loadUserByUsername(email: String): UserDetails {
        log.info { "Before finding a member: $email" }

        val foundMember = memberRepository.findByEmail(email)
        log.info { "Found Member: $foundMember" }
        foundMember?.let{
            return PrincipalDetails(it)
        } ?: throw MemberNotFoundException(email)
    }
}