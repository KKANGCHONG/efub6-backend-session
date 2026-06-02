package com.practice.efubaccount.comment.controller;

import com.practice.efubaccount.comment.domain.Comment;
import com.practice.efubaccount.comment.dto.request.CommentRequest;
import com.practice.efubaccount.comment.dto.request.CommentUpdateRequest;
import com.practice.efubaccount.comment.dto.response.CommentResponse;
import com.practice.efubaccount.comment.service.CommentService;
import com.practice.efubaccount.post.dto.response.PostCommentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class PostCommentController {

    private final CommentService commentService;


    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Void> createComment(@PathVariable("postId") Long postId,
                                              @RequestBody @Valid CommentRequest request) {
        Long id = commentService.createComment(postId,request);
        return ResponseEntity.created(URI.create("/posts/"+postId+"/comments/"+id)).build();
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<PostCommentResponse> getComments(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(commentService.getPostCommentList(postId));
    }

    // 댓글 수정
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable("commentId") Long commentId,
                                                         @RequestBody @Valid CommentUpdateRequest request,
                                                         @RequestHeader("Auth-Id") Long accountId,
                                                         @RequestHeader("Auth-Password") String password) {
        CommentResponse response = commentService.updateComment(commentId, request, accountId, password);
        return ResponseEntity.ok(response);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId,
                                              @RequestHeader("Auth-Id") Long accountId,
                                              @RequestHeader("Auth-Password") String password) {
        commentService.deleteComment(commentId, accountId, password);
        return ResponseEntity.noContent().build();
    }

    // 댓글 좋아요
    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<String> likeComment(@PathVariable("commentId") Long commentId,
                                              @RequestHeader("Auth-Id") Long accountId) {
        commentService.likeComment(commentId, accountId);
        return ResponseEntity.status(HttpStatus.CREATED).body("좋아요를 눌렀습니다.");
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/comments/{commentId}/like")
    public ResponseEntity<String> unlikeComment(@PathVariable("commentId") Long commentId,
                                                @RequestHeader("Auth-Id") Long accountId) {
        commentService.unlikeComment(commentId, accountId);
        return ResponseEntity.ok("좋아요가 취소되었습니다.");
    }
}