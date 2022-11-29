package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.springboot.blog.utils.AppConstants.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService){
        this.postService=postService;
    }

    //create blog post rest api
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto){
        return new ResponseEntity<PostDto>(postService.createPost(postDto),HttpStatus.CREATED);
    }

    //get all posts rest api
    @GetMapping
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo",defaultValue = DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = DEFAULT_SORT_BY,required = false)String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTORY, required = false)String sortDir
    ){
        return postService.getAllPosts(pageNo,pageSize, sortBy, sortDir);
    }

    //get post by id
    @GetMapping("/{idPost}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "idPost") long idPost){
        return ResponseEntity.ok(postService.getPostById(idPost));
    }
    @PostMapping("/{idPost}")
    public ResponseEntity<PostDto>updatePost(@RequestBody PostDto postDto,@PathVariable(name = "idPost") long idPost){
        return ResponseEntity.ok(postService.updatePost(postDto,idPost));
    }
    @DeleteMapping("/{idPost}")
    public ResponseEntity<String> deletePostById( @PathVariable(name = "idPost") long idPost){
        postService.deletePostById(idPost);
        return ResponseEntity.ok(new String("Deleted post with id: "+idPost));

    }
}
