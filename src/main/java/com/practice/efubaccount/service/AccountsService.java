package com.practice.efubaccount.service;

import com.practice.efubaccount.dto.response.AccountResponseDto;
import com.practice.efubaccount.dto.response.CreateAccountResponseDto;
import com.practice.efubaccount.dto.request.BioUpdateRequestDto;
import com.practice.efubaccount.dto.request.CreateAccountRequestDto;
import com.practice.efubaccount.domain.Account;
import com.practice.efubaccount.domain.AccountStatus;
import com.practice.efubaccount.repository.AccountRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountsService {

    private final AccountRepository accountRepository;

    // 회원 단건 조회
    public AccountResponseDto getAccount(Long accountId) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        return AccountResponseDto.from(account);
    }

    // 회원 생성
    @Transactional
    public CreateAccountResponseDto createAccount(CreateAccountRequestDto requestDto) {
        // 이메일 중복 검사
        if (accountRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 email입니다. " + requestDto.getEmail());
        }
        Account account = requestDto.toEntity();
        Account savedAccount = accountRepository.save(account);
        return CreateAccountResponseDto.from(savedAccount);
    }

    // 프로필(자기소개) 수정
    @Transactional
    public AccountResponseDto updateAccount(Long accountId, BioUpdateRequestDto requestDto) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        account.updateBio(requestDto.getBio());
        return AccountResponseDto.from(account);
    }

    // 회원 논리적 삭제 (status 변경)
    @Transactional
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        account.changeStatus(AccountStatus.DEACTIVATED);
    }

    // 회원 물리적 삭제
    @Transactional
    public void physicalDeleteAccount(Long accountId) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        accountRepository.delete(account);
    }

}