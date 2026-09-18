package com.practice.efubaccount.account.service;

import com.practice.efubaccount.account.domain.Account;
import com.practice.efubaccount.account.domain.AccountStatus;
import com.practice.efubaccount.global.utils.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final AccountService accountService;

    //OAuth2UserRequest를 받아 사용자를 로드하는 메서드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2 사용자 정보 로드
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        boolean kakao = "kakao".equals(userRequest.getClientRegistration().getRegistrationId());
        if (kakao && oAuth2User.getAttributes().get("id") == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error("id_missing"),
                    "카카오 사용자 ID가 없습니다.");
        }
        OAuth2UserInfo oAuth2UserInfo = new OAuth2UserInfo(oAuth2User.getAttributes(), kakao);
        String email = oAuth2UserInfo.getEmail();
        if (email == null || email.isBlank()) {
            throw new OAuth2AuthenticationException(new OAuth2Error("email_missing"),
                    "OAuth2 제공자가 이메일을 반환하지 않았습니다.");
        }

        Account account = accountService.findOrCreateOAuth2Account(email, oAuth2UserInfo.getNickname());
        if (account.getStatus() == AccountStatus.DEACTIVATED) {
            throw new OAuth2AuthenticationException(new OAuth2Error("account_deactivated"),
                    "탈퇴한 계정은 로그인할 수 없습니다.");
        }

        // 사용자 속성 생성
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("id", account.getAccountId());
        attributes.put("email", account.getEmail());

        // DefaultOAuth2User 객체 생성하여 반환
        return new DefaultOAuth2User(
                Collections.singleton(new OAuth2UserAuthority(attributes)),
                attributes,
                "email"); // 기본 식별자 지정
    }

}
