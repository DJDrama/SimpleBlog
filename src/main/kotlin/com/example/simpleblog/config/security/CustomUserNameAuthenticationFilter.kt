package com.example.simpleblog.config.security

import com.example.simpleblog.domain.member.LoginDto
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class CustomUserNameAuthenticationFilter(
    private val objectMapper: ObjectMapper
) : UsernamePasswordAuthenticationFilter() {
    private val log = KotlinLogging.logger { }

    private val jwtManager: JwtManager = JwtManager()

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        log.info { "login request came" }
        lateinit var loginDto: LoginDto
        try {
            request?.inputStream.use {
                loginDto = objectMapper.readValue(it, LoginDto::class.java)
            }
        } catch (e: Exception) {
            log.error { "loginFilter Error : " + e.message }
        }

        log.info { "loginDto : $loginDto" }

        val authenticationToken = UsernamePasswordAuthenticationToken(
            loginDto.email, loginDto.password
        )

        return this.authenticationManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {

        val principalDetails = authResult?.principal as PrincipalDetails
        log.info { "Login Complete, make session principalDetails: $principalDetails" }
        val jwtToken = jwtManager.generateAccessToken(principalDetails)
        log.info { "JwtToken: $jwtToken" }

        response?.addHeader("Authorization", "Bearer $jwtToken")
    }
}