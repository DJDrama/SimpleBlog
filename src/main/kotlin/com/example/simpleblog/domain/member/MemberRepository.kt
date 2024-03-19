package com.example.simpleblog.domain.member

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>{
    override fun findAll(pageable: Pageable): Page<Member>
}
