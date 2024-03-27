package com.example.simpleblog.config.security

import com.example.simpleblog.domain.member.Member
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class PrincipalDetails(
    private val member: Member
) : UserDetails {
    private val log = KotlinLogging.logger { }
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        log.info { "Validate Role" }
        val collection = mutableListOf<GrantedAuthority>()
        collection.add(GrantedAuthority {
            "ROLE_" + member.role
        })
        return collection
    }

    override fun getPassword(): String {
        return member.password
    }

    override fun getUsername(): String {
        return member.email
    }

    override fun isAccountNonExpired(): Boolean {
        return true // 항상 유효하도록
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}