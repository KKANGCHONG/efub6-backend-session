package com.practice.efubaccount.account.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@Document("efublog")
public class AccountDocument {

    @Id
    private String id;
    private String email;
    private String password;
    private String nickname;

    @Builder
    public AccountDocument(String id, String email, String password, String nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public static AccountDocument from (Account account) {
        return AccountDocument.builder()
                .id(account.getAccountId().toString())
                .email(account.getEmail())
                .password(account.getPassword())
                .nickname(account.getNickname())
                .build();
    }

    public void updateNickname(String nickname) { this.nickname = nickname; }
}