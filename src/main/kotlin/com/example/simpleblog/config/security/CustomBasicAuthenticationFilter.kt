package com.example.simpleblog.config.security

import com.example.simpleblog.domain.member.Member
import com.example.simpleblog.domain.member.MemberRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class CustomBasicAuthenticationFilter(
    private val memberRepository: MemberRepository,
    authenticationManager: AuthenticationManager
) : BasicAuthenticationFilter(authenticationManager) {

    private val log = KotlinLogging.logger { }
    private val jwtManager = JwtManager()
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        log.info { "Requests that need authorization come here!" }
        val authorizationHeader = request.getHeader("Authorization")
        if (authorizationHeader.isNullOrBlank()) {
            log.info { "Header is null or blank ${request.requestURL}" }
            throw NullPointerException("Header is null")
        }

        val token = authorizationHeader.substringAfter("Bearer ")

        if (token.isNotBlank()) {
            chain.doFilter(request, response)
        }

        log.info { "token $token" }

        val email = jwtManager.getMemberEmail(token)
        val member: Member?
        if (email != null)
            member = memberRepository.findByEmail(email)
        else throw NullPointerException("Can't find member email")

        val principalDetails = PrincipalDetails(member ?: throw NullPointerException("Cannot find member"))
        val authentication: Authentication = UsernamePasswordAuthenticationToken(
            principalDetails,
            principalDetails.password
        )

        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }
}