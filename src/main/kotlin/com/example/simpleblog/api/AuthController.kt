package com.example.simpleblog.api

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpSession
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/auth")
@RestController
class AuthController(

) {

    private val log = KotlinLogging.logger {  }

    @GetMapping("/login")
    fun login(session: HttpSession){
        session.setAttribute("principal", "pass")
    }
}