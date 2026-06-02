package com.practice.efubaccount.follow.dto;

import com.practice.efubaccount.account.domain.Account;
import com.practice.efubaccount.follow.enums.FollowStatus;

public record FollowStatusResponse(
        Long accountId,
        String nickname,
        String email,
        FollowStatus status
) {
    public static FollowStatusResponse of(Account account, FollowStatus status) {
        return new FollowStatusResponse(
                account.getAccountId(),
                account.getNickname(),
                account.getEmail(),
                status
        );
    }
}
