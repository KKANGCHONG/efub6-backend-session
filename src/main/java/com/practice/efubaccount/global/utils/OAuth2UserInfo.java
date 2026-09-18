package com.practice.efubaccount.global.utils;

import java.util.Map;

public class OAuth2UserInfo {
    private final Map<String, Object> attributes;
    private final boolean kakao;

    public OAuth2UserInfo(Map<String, Object> attributes, boolean kakao) {
        this.attributes = attributes;
        this.kakao = kakao;
    }

    //TODO: 이름 반환
    public String getNickname() {
        if (kakao) {
            Object id = attributes.get("id");
            return "kakao_" + id;
        }
        return (String) attributes.get("name");
    }
    //TODO: 이메일 반환
    public String getEmail() {
        if (kakao) {
            Object account = attributes.get("kakao_account");
            if (account instanceof Map<?, ?> kakaoAccount) {
                Object email = kakaoAccount.get("email");
                return email instanceof String ? (String) email : null;
            }
            return null;
        }
        return (String) attributes.get("email");
    }


}

