package com.example.simpleblog.util

import com.example.simpleblog.config.security.JwtManager
import com.example.simpleblog.config.security.PrincipalDetails
import com.example.simpleblog.domain.member.createFakeMember
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class UtilTest {
    private val log = KotlinLogging.logger {  }

    private val objectMapper = ObjectMapper()

    @Test
    fun generateJwtToken(){
        objectMapper.registerModule(JavaTimeModule()) // for Localdatetime parsing error
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val jwtManager = JwtManager(accessTokenExpireSecond = 60)
        val details = PrincipalDetails(createFakeMember(1))
        val accessToken=jwtManager.generateAccessToken(objectMapper.writeValueAsString(details))



        val decodedJwt = jwtManager.validatedJwt(accessToken)

        val principalString = decodedJwt.getClaim("principal").asString()

        val principalDetails = objectMapper.readValue(principalString, PrincipalDetails::class.java)

        log.info { "result = ${principalDetails.member}"}

    }

    @Test
    fun bcryptEncodeTest(){
        println(BCryptPasswordEncoder().encode("0c1df2157f07d9f85fff25f6cb120ad6"))
    }
}