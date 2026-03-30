package com.practice.efubaccount.dto.response;

import com.practice.efubaccount.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// Account 조회 후 응답 DTO

@Builder @Getter
@AllArgsConstructor
public class AccountResponseDto {

//    private String nickname;
//    private String email;
//    private String bio;
//
//    public static AccountResponseDto from(Account account) {
//        return AccountResponseDto.builder()
//                .nickname(account.getNickname())
//                .email(account.getEmail())
//                .bio(account.getBio())
//                .build();
//    }
}