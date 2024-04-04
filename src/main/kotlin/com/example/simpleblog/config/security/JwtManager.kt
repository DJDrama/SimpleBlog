package com.example.simpleblog.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt
import java.util.*
import java.util.concurrent.TimeUnit

class JwtManager(
    private val accessTokenExpireSecond: Long = 300
) {
    private val log = KotlinLogging.logger { }
    private val secretKey = "mySecreteKey"
    private val claimEmail = "email"
    private val claimPassword = "password"
    private val claimPrincipal = "principal"
    private val jwtSubject = "my-token"

    //private val accessTokenExpireSecond: Long = 60

    fun generateAccessToken(principal: String): String {
        val expireDate = Date(
            System.nanoTime()
                    + TimeUnit.SECONDS.toMillis(accessTokenExpireSecond)
        )

        log.info { "AccessToken expireDate = $expireDate"}

        return JWT.create()
            .withSubject(jwtSubject)
            .withExpiresAt(expireDate)
            .withClaim(claimPrincipal, principal)
            .sign(Algorithm.HMAC512(secretKey))
    }

    fun getMemberEmail(token: String): String? {
        return JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token)
            .getClaim(claimEmail).asString()
    }

    fun getPrincipalDetailsByAccessToken(accessToken: String): String {
        val decodedJWT = validatedJwt(accessToken = accessToken)
        val principalString = decodedJWT.getClaim(claimPrincipal).asString()
        return principalString
    }
    fun validatedJwt(accessToken: String): DecodedJWT {
        try{
            val algorithm = Algorithm.HMAC512(secretKey)
            val verifier = JWT.require(algorithm).build()
            val decodedJwt = verifier.verify(accessToken)
            return decodedJwt
        }catch(e: JWTVerificationException) {
            //e.printStackTrace()
            log.error { "Error->$e" }
            throw RuntimeException("Invalid jwt")
        }
    }

}