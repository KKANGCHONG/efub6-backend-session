package com.practice.efubaccount.global.jwt;

import com.practice.efubaccount.account.domain.Account;
import com.practice.efubaccount.account.domain.AccountStatus;
import com.practice.efubaccount.account.repository.AccountRepository;
import com.practice.efubaccount.account.repository.AccountDocumentRepository;
import com.practice.efubaccount.account.service.AccountService;
import com.practice.efubaccount.account.service.AuthService;
import com.practice.efubaccount.global.exception.CustomException;
import com.practice.efubaccount.global.exception.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenTypeTest {
    private TokenProvider tokenProvider;
    private AccountService accountService;
    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> values;
    private Account account;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        redisTemplate = mock(RedisTemplate.class);
        values = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(values);
        tokenProvider = new TokenProvider(mock(AccountRepository.class), redisTemplate);
        String testSecret = Base64.getEncoder().encodeToString(
                "a-test-secret-that-is-long-enough-for-hs256-signing".getBytes(StandardCharsets.UTF_8));
        ReflectionTestUtils.setField(tokenProvider, "secretKey", testSecret);
        accountService = mock(AccountService.class);
        account = Account.builder().email("member@example.com").password("unused").nickname("member").build();
        ReflectionTestUtils.setField(account, "accountId", 7L);
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void accessTokenAuthenticatesApiButRefreshTokenDoesNot() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(tokenProvider);
        String accessToken = tokenProvider.createAccessToken(account);
        String refreshToken = tokenProvider.createRefreshToken(account);

        assertTrue(tokenProvider.isValidToken(accessToken, TokenProvider.TokenType.ACCESS));
        assertFalse(tokenProvider.isValidToken(refreshToken, TokenProvider.TokenType.ACCESS));
        MockHttpServletRequest accessRequest = new MockHttpServletRequest();
        accessRequest.addHeader("Authorization", "Bearer " + accessToken);
        filter.doFilter(accessRequest, new MockHttpServletResponse(), (request, response) ->
                assertEquals("member@example.com", SecurityContextHolder.getContext().getAuthentication().getName()));

        SecurityContextHolder.clearContext();
        MockHttpServletRequest refreshRequest = new MockHttpServletRequest();
        refreshRequest.addHeader("Authorization", "Bearer " + refreshToken);
        filter.doFilter(refreshRequest, new MockHttpServletResponse(), (request, response) ->
                assertNull(SecurityContextHolder.getContext().getAuthentication()));
    }

    @Test
    void onlyStoredRefreshTokenCanReissueAccessToken() {
        AuthService authService = new AuthService(accountService, tokenProvider, redisTemplate);
        String accessToken = tokenProvider.createAccessToken(account);
        String refreshToken = tokenProvider.createRefreshToken(account);
        when(accountService.findByEmail("member@example.com")).thenReturn(account);
        when(values.get("7")).thenReturn(refreshToken);

        String reissued = authService.reissueAccessToken(refreshToken).getAccessToken();
        assertTrue(tokenProvider.isValidToken(reissued, TokenProvider.TokenType.ACCESS));
        assertFalse(tokenProvider.isValidToken(reissued, TokenProvider.TokenType.REFRESH));

        CustomException error = assertThrows(CustomException.class, () -> authService.reissueAccessToken(accessToken));
        assertEquals(ErrorCode.INVALID_REFRESH_TOKEN, error.getErrorCode());
    }

    @Test
    void deactivatedAccountCannotReissue() {
        AuthService authService = new AuthService(accountService, tokenProvider, redisTemplate);
        String refreshToken = tokenProvider.createRefreshToken(account);
        account.changeStatus(AccountStatus.DEACTIVATED);
        when(accountService.findByEmail("member@example.com")).thenReturn(account);

        CustomException error = assertThrows(CustomException.class, () -> authService.reissueAccessToken(refreshToken));
        assertEquals(ErrorCode.ACCOUNT_DEACTIVATED, error.getErrorCode());
        verify(values, never()).get(anyString());
    }

    @Test
    @SuppressWarnings("unchecked")
    void deactivationRemovesStoredRefreshToken() {
        AccountRepository accountRepository = mock(AccountRepository.class);
        RedisTemplate<String, Object> accountRedisTemplate = mock(RedisTemplate.class);
        AccountService service = new AccountService(accountRepository, accountRedisTemplate,
                mock(AccountDocumentRepository.class));
        when(accountRepository.findByAccountId(7L)).thenReturn(Optional.of(account));

        service.deleteAccount(7L);

        assertEquals(AccountStatus.DEACTIVATED, account.getStatus());
        verify(accountRedisTemplate).delete("7");
    }
}
