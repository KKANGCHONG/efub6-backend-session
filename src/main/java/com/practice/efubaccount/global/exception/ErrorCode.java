package com.practice.efubaccount.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Default
    INTERNAL_SERVER_ERROR(500, "예상치 못한 서버에러가 발생했습니다."),
    ERROR(400, "요청 처리에 실패했습니다."),

    // account
    ACCOUNT_NOT_FOUND(404, "존재하는 계정이 없습니다."),

    // post
    POST_NOT_FOUND(404, "해당 id의 게시물이 존재하지 않습니다."),
    POST_ACCOUNT_MISMATCH(401, "게시글 생성자가 아닙니다."),

    // comment
    COMMENT_NOT_FOUND(404, "댓글이 존재하지 않습니다."),
    COMMENT_ACCOUNT_MISMATCH(400, "댓글 작성자와 현재 로그인된 사용자가 일치하지 않습니다."),
    LIKE_NOT_FOUND(404, "좋아요가 존재하지 않습니다."),
    LIKE_ALREADY_EXISTS(400, "좋아요가 이미 존재합니다."),

    // follow
    FOLLOW_ALREADY_EXISTS(400, "이미 팔로우한 사용자입니다."),
    FOLLOW_NOT_FOUND(404, "팔로우 관계가 존재하지 않습니다."),
    CANNOT_FOLLOW_SELF(400, "자기 자신을 팔로우할 수 없습니다.");

    private final int status;
    private final String message;
}