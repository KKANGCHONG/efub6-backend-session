package com.practice.efubaccount.dto.response;

import com.practice.efubaccount.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// Account 생성 후 응답 DTO

@Builder @Getter
@AllArgsConstructor
public class CreateAccountResponseDto {
//    private Long id;
//    private String nickname;
//    private String email;
//    private String bio;
//
//    public static CreateAccountResponseDto from(Account account) {
//        return CreateAccountResponseDto.builder()
//                .id(account.getAccountId())
//                .nickname(account.getNickname())
//                .email(account.getEmail())
//                .bio(account.getBio())
//                .build();
//    }
}