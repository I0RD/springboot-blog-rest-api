package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository,PostRepository postRepository,ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository=postRepository;
        this.mapper=mapper;
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long id_post) {
        List<Comment> comments = commentRepository.findByPostId(id_post);
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(long id_post, long id_comment) {
        Comment comment=checkCommentBelong(id_post,id_comment);
        return mapToDTO(comment);
    }

    @Override
    public CommentDto createComment(long id_post, CommentDto commentDto) {

        Comment comment= mapToEntity(commentDto);
        //retrieve post entity by id
        Post post=postRepository.findById(id_post).orElseThrow(()->new ResourceNotFoundException("Post","id",id_post));
        comment.setPost(post);
        // set post to comment entity

        //save comment entity to DB

        Comment newComment = commentRepository.save(comment);
        return mapToDTO(newComment);
    }

    @Override
    public CommentDto updateCommentById(long id_post, long id_comment,CommentDto commentDto) {
        Comment comment=checkCommentBelong(id_post,id_comment);
        comment.setBody(commentDto.getBody());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        Comment updateComment = commentRepository.save(comment);
        return mapToDTO(updateComment);
    }

    @Override
    public void deleteCommentById(long id_post, long id_comment) {
        Comment comment=checkCommentBelong(id_post,id_comment);
        commentRepository.delete(comment);
    }

    private CommentDto mapToDTO(Comment comment){

        CommentDto commentDto=mapper.map(comment,CommentDto.class);

//        CommentDto commentDto = new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setBody(comment.getBody());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setName(comment.getName());

        return commentDto;
    }
    private Comment mapToEntity(CommentDto commentDto){

        Comment comment = mapper.map(commentDto,Comment.class);

//        Comment comment=new Comment();
//        comment.setId(commentDto.getId());
//        comment.setEmail(commentDto.getEmail());
//        comment.setName(commentDto.getName());
//        comment.setBody(commentDto.getBody());

        return comment;
    }
    private Comment checkCommentBelong(long id_post,long id_comment){
        Post post=postRepository.findById(id_post).orElseThrow(()->new ResourceNotFoundException("Post","id",id_post));
        Comment comment = commentRepository.findById(id_comment).orElseThrow(()->new ResourceNotFoundException("Comment","id",id_comment));
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belog to post");
        }
        return comment;
    }
}
