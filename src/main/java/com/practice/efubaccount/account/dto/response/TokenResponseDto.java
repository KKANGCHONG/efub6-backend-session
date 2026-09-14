package com.practice.efubaccount.account.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenResponseDto {
    private String accessToken;

    @Builder
    public TokenResponseDto(final String accessToken) {
        this.accessToken = accessToken;
    }
}