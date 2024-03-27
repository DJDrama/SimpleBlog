package com.example.simpleblog.util

import com.example.simpleblog.config.security.JwtManager
import com.example.simpleblog.config.security.PrincipalDetails
import com.example.simpleblog.domain.member.createFakeMember
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test

class UtilTest {
    private val log = KotlinLogging.logger {  }
    @Test
    fun generateJwtToken(){
        val jwtManager = JwtManager()
        val details = PrincipalDetails(createFakeMember(1))
        val accessToken = jwtManager.generateAccessToken(details)
        val email = jwtManager.getMemberEmail(accessToken)

        log.info {
            "accessToken $accessToken"
        }

        log.info {
            "email $email"
        }
    }
}