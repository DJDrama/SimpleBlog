package com.example.simpleblog.api

import com.example.simpleblog.domain.post.PostRes
import com.example.simpleblog.domain.post.PostSaveReq
import com.example.simpleblog.service.PostService
import com.example.simpleblog.util.value.CommonResultDto
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
//@RequestMapping("/post")
class PostController(
    private val postService: PostService
) {

    @GetMapping("/posts")
    fun findPosts(@PageableDefault(size = 10) pageable: Pageable): CommonResultDto<Page<PostRes>> {
        return CommonResultDto(resultCode = HttpStatus.OK, resultMsg = "find posts", data = postService.findPosts(pageable))
    }


    @GetMapping("/post/{id}")
    fun findPostById(@PathVariable id: Long): CommonResultDto<PostRes> {
        return CommonResultDto(
            resultCode = HttpStatus.OK,
            resultMsg = "find Post by id",
            data = postService.findPostById(id = id)
        )
    }

    @DeleteMapping("/post/{id}")
    fun deletePostById(@PathVariable id: Long): CommonResultDto<Unit> {
        return CommonResultDto(
            resultCode = HttpStatus.OK,
            resultMsg = "delete Post by id",
            data = postService.deletePost(id = id)
        )
    }

    @PostMapping("/post")
    fun savePost(@Valid @RequestBody postSaveReq: PostSaveReq): CommonResultDto<PostRes> {
        return CommonResultDto(
            resultCode = HttpStatus.CREATED,
            resultMsg = "save Post",
            data = postService.savePost(postSaveReq = postSaveReq)
        )
    }


}