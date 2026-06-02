package com.practice.efubaccount.follow.dto;

import com.practice.efubaccount.account.domain.Account;

import java.util.List;

public record FollowListResponse(
        List<FollowUserDto> followerList,
        List<FollowUserDto> followingList,
        long followerCount,
        long followingCount
) {

    public record FollowUserDto(
            Long accountId,
            String nickname,
            String email
    ) {
        public static FollowUserDto from(Account account) {
            return new FollowUserDto(
                    account.getAccountId(),
                    account.getNickname(),
                    account.getEmail()
            );
        }
    }

    public static FollowListResponse of(List<Account> followers, List<Account> followings) {
        return new FollowListResponse(
                followers.stream().map(a -> FollowUserDto.from(a)).toList(),
                followings.stream().map(a -> FollowUserDto.from(a)).toList(),
                followers.size(),
                followings.size()
        );
    }


}
