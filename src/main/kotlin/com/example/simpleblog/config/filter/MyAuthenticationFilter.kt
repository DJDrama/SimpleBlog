package com.example.simpleblog.config.filter

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import java.lang.IllegalStateException

class MyAuthenticationFilter: Filter {
    private val log = KotlinLogging.logger {  }

    /**
     * Validate Authentication
     */
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val servletRequest = request as HttpServletRequest

        servletRequest.session.getAttribute("principal") ?: throw IllegalStateException("Session not found!")
        chain?.doFilter(request, response)
    }

}