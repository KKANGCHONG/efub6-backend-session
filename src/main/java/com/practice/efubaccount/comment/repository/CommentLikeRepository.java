package com.practice.efubaccount.comment.repository;

import com.practice.efubaccount.account.domain.Account;
import com.practice.efubaccount.comment.domain.Comment;
import com.practice.efubaccount.comment.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByCommentAndAccount(Comment comment, Account account);

    Optional<CommentLike> findByCommentAndAccount(Comment comment, Account account);
}