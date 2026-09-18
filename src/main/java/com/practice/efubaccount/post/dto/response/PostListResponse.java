package com.practice.efubaccount.post.dto.response;

import com.practice.efubaccount.post.dto.summary.PostSummary;

import java.util.List;

public record PostListResponse( // record : 데이터만 포함하는 불변한 객체를 정의하는 데이터 클래스
        List<PostSummary> posts,
        Long totalPosts
) {}