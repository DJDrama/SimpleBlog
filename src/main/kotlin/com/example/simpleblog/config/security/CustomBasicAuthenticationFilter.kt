package com.example.simpleblog.config.security

import com.example.simpleblog.domain.member.Member
import com.example.simpleblog.domain.member.MemberRepository
import com.fasterxml.jackson.databind.ObjectMapper
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
    private val objectMapper: ObjectMapper,
    authenticationManager: AuthenticationManager
) : BasicAuthenticationFilter(authenticationManager) {

    private val log = KotlinLogging.logger { }
    private val jwtManager = JwtManager()
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        log.info { "Requests that need authorization come here!" }
        val token = request.getHeader(AUTHORIZATION_HEADER)?.substringAfter(JWT_HEADER)
        if (token == null) {
            log.info { "No token!" }
            chain.doFilter(request, response)
            return
        }

        log.info { "token $token" }

        val principalJsonData = jwtManager.getPrincipalDetailsByAccessToken(accessToken = token)
        val principalDetails = objectMapper.readValue(principalJsonData, PrincipalDetails::class.java)

        // DB로 호출
        //val member = memberRepository.findByEmail(details.member.email)
        //val principalDetails = PrincipalDetails(member ?: throw NullPointerException("Cannot find member"))
        val authentication: Authentication = UsernamePasswordAuthenticationToken(
            principalDetails,
            principalDetails.password,
            principalDetails.authorities
        )

        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }
}