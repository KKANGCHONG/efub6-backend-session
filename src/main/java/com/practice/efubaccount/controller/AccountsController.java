package com.practice.efubaccount.controller;

import com.practice.efubaccount.dto.request.BioUpdateRequestDto;
import com.practice.efubaccount.dto.request.CreateAccountRequestDto;
import com.practice.efubaccount.dto.response.AccountResponseDto;
import com.practice.efubaccount.dto.response.CreateAccountResponseDto;
import com.practice.efubaccount.service.AccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountsController {

    private final AccountsService accountsService;

    // 회원 조회: GET /accounts/{accountId}
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDto> getAccount(@PathVariable("accountId") Long accountId) {
        AccountResponseDto responseDto = accountsService.getAccount(accountId);
        return ResponseEntity.ok(responseDto);
    }

    // 계정 생성 POST /accounts
    @PostMapping
    public ResponseEntity<CreateAccountResponseDto> createAccount(@RequestBody CreateAccountRequestDto requestDto) {
        CreateAccountResponseDto responseDto = accountsService.createAccount(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 계정 프로필(자기소개) 수정: PATCH /accounts/profile/{accountId}
    @PatchMapping("/profile/{accountId}")
    public ResponseEntity<AccountResponseDto> updateAccount(@PathVariable("accountId") Long accountId,
                                                            @RequestBody BioUpdateRequestDto requestDto) {
        AccountResponseDto responseDto = accountsService.updateAccount(accountId, requestDto);
        return ResponseEntity.ok(responseDto);
    }


    // 계정 논리적 삭제(탈퇴): PATCH /accounts/{accountId}
    @PatchMapping("/{accountId}")
    public ResponseEntity<Map<String, String>> deleteAccount(@PathVariable("accountId") Long accountId) {
        accountsService.deleteAccount(accountId);  // 상태 변경만 수행
        Map<String, String> response = new HashMap<>();
        response.put("message", "성공적으로 탈퇴되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 계정 물리적 삭제: DELETE /accounts/{accountId}
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Map<String, String>> physicalDeleteAccount(@PathVariable("accountId") Long accountId) {
        accountsService.physicalDeleteAccount(accountId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "성공적으로 탈퇴되었습니다.");
        return ResponseEntity.ok(response);
    }

}