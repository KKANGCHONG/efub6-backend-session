package com.practice.efubaccount.account.controller;

import com.practice.efubaccount.account.dto.request.TokenRequestDto;
import com.practice.efubaccount.account.dto.response.TokenResponseDto;
import com.practice.efubaccount.account.service.AuthService;
import com.practice.efubaccount.global.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    //TODO 1.현재 인증된 사용자 email 조회

    //TODO 2. 토큰 재발급
}
