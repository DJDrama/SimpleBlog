package com.example.simpleblog.config.security

import com.example.simpleblog.domain.member.MemberRepository
import com.example.simpleblog.util.func.responseData
import com.example.simpleblog.util.value.CommonResultDto
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val objectMapper: ObjectMapper,
    private val memberRepository: MemberRepository
) {

    private val log = KotlinLogging.logger { }

    //@Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web: WebSecurity ->
            web.ignoring().requestMatchers("/**") // 모든 요청 다 허용하기
        }
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf {
                it.disable()
            }
            .headers {
                it.frameOptions { frameOption ->
                    frameOption.disable()
                }
            }
            .formLogin {
                it.disable()
            }
            .httpBasic {
                it.disable()
            }
            .sessionManagement {  // 세션 유지 안하고 JSON 토큰 기반으로
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .cors {
                it.configurationSource(corsConfig())
            }
            .addFilter(loginFilter())
            .addFilter(authenticationFilter())
            .exceptionHandling {
                it.accessDeniedHandler(CustomAccessDeniedHandler())
                it.authenticationEntryPoint(CustomAuthenticationEntryPoint(objectMapper = objectMapper))
            }
            .authorizeHttpRequests {
                it.requestMatchers(toH2Console()).permitAll()
                    // 모든 요청에 인증이 필요
                    .requestMatchers("/v1/posts").hasAnyRole("USER", "ADMIN")
            }

        return http.build()
    }


    class CustomAuthenticationSuccessHandler: AuthenticationSuccessHandler {
        private val log = KotlinLogging.logger { }
        override fun onAuthenticationSuccess(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            authentication: Authentication?
        ) {
            log.warn { "CustomAuthenticationSuccessHandler: Login Success!" }
        }

    }

    class CustomAuthenticationFailureHandler : AuthenticationFailureHandler {

        private val log = KotlinLogging.logger { }

        override fun onAuthenticationFailure(
            request: HttpServletRequest?,
            response: HttpServletResponse,
            exception: AuthenticationException?
        ) {

            log.warn { "CustomFailureHandler: Login Failed!" }
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authentication failed")
        }

    }

    class CustomAuthenticationEntryPoint(
        private val objectMapper: ObjectMapper
    ) : AuthenticationEntryPoint {
        private val log = KotlinLogging.logger { }

        override fun commence(
            request: HttpServletRequest?,
            response: HttpServletResponse,
            authException: AuthenticationException?
        ) {
            log.info { "CustomAuthenticationEntryPoint: Access Denied!" }
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.reasonPhrase)

            //response?.sendError(HttpServletResponse.SC_FORBIDDEN)
            /*val result = CommonResultDto(HttpStatus.UNAUTHORIZED, "Access Denied!", authException)
            responseData(response, objectMapper.writeValueAsString(result))*/
        }

    }

    class CustomAccessDeniedHandler : AccessDeniedHandler {
        private val log = KotlinLogging.logger { }

        override fun handle(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            accessDeniedException: AccessDeniedException?
        ) {
            log.info { "CustomAccessDeniedHandler: Access Denied!" }
            //accessDeniedException?.printStackTrace()
            response?.sendError(HttpServletResponse.SC_FORBIDDEN)
        }

    }

    @Bean
    fun authenticationFilter(): CustomBasicAuthenticationFilter {
        return CustomBasicAuthenticationFilter(
            authenticationManager = authenticationManager(),
            memberRepository = memberRepository,
            objectMapper = objectMapper
        )
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    /** There is no PasswordEncoder mapped for the id "null"
     *
     * password 자동 검증
     * */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun loginFilter(): UsernamePasswordAuthenticationFilter {
        val authenticationFilter = CustomUserNameAuthenticationFilter(objectMapper = objectMapper)
        authenticationFilter.setAuthenticationManager(authenticationManager())
        authenticationFilter.setFilterProcessesUrl("/login")
        authenticationFilter.setAuthenticationFailureHandler(CustomAuthenticationFailureHandler())
        authenticationFilter.setAuthenticationSuccessHandler(CustomAuthenticationSuccessHandler())
        return authenticationFilter
    }

    @Bean
    fun corsConfig(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOriginPattern("*") // 모든 아이피 허용
        config.addAllowedMethod("*") // 모든 http 메소드 허용
        config.addAllowedHeader("*") // 모든 헤더 허용
        config.addExposedHeader("authorization") // Bearer token 방식
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }

}