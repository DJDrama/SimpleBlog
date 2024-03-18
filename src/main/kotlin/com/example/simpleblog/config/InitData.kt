package com.example.simpleblog.config

import com.example.simpleblog.domain.member.*
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.serpro69.kfaker.faker
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@Configuration
class InitData(
    private val memberRepository: MemberRepository
) {
    private val faker = faker { }
    private val log = KotlinLogging.logger { }

    @EventListener(ApplicationReadyEvent::class)
    fun init() {
        val members = buildList {
            repeat(100) {
                val member = generateMember()
                add(member)
                log.info { "member: $member" }
            }
        }
        memberRepository.saveAll(members)

    }

    private fun generateMember(): Member = MemberSaveReq(
        email = faker.internet.safeEmail(),
        password = faker.crypto.md5(),
        role = Role.USER
    ).asEntityModel()
}