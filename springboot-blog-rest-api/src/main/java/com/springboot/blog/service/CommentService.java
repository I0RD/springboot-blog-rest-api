package com.springboot.blog.service;

import com.springboot.blog.payload.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> getCommentsByPostId(long id_post);
    CommentDto getCommentById(long id_post, long id_comment);
    CommentDto createComment(long id_post, CommentDto commentDto);
    CommentDto updateCommentById(long id_post, long id_comment, CommentDto commentDto);
    void deleteCommentById(long id_post, long id_comment);
}
