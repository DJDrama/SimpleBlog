package com.example.simpleblog.config

import com.example.simpleblog.domain.member.*
import com.example.simpleblog.domain.post.Post
import com.example.simpleblog.domain.post.PostRepository
import com.example.simpleblog.domain.post.PostSaveReq
import com.example.simpleblog.domain.post.asEntityModel
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.serpro69.kfaker.faker
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@Configuration
class InitData(
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository,
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

        val posts = generatePosts(100)
        postRepository.saveAll(posts)

    }

    private fun generatePosts(cnt: Int): List<Post> {
        val posts = mutableListOf<Post>()
        for(i in 1 .. cnt){
            val post = generatePosts()
            log.info { "insert $post" }
            posts.add(post)
        }
        return posts
    }

    private fun generateMember(): Member = MemberSaveReq(
        email = faker.internet.safeEmail(),
        password = faker.crypto.md5(),
        role = Role.USER
    ).asEntityModel()

    private fun generatePosts() = PostSaveReq(
        title = faker.address.community(),
        content = faker.internet.slug(),
        memberId = 1,
    ).asEntityModel()
}