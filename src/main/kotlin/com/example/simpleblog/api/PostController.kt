package com.example.simpleblog.api

import com.example.simpleblog.domain.post.PostRes
import com.example.simpleblog.service.PostService
import com.example.simpleblog.util.value.CommonResultDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PostController(
    private val postService: PostService
) {

    @GetMapping("/posts")
    fun findPosts() : CommonResultDto<List<PostRes>> {
        return CommonResultDto(resultCode = HttpStatus.OK, resultMsg = "find posts", data = postService.findPosts())
    }



}