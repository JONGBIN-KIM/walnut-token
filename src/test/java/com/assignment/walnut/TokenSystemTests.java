package com.assignment.walnut;

import com.assignment.walnut.dto.CardRegistrationRequest;
import com.assignment.walnut.dto.CardRegistrationResponse;
import com.assignment.walnut.dto.TokenRequest;
import com.assignment.walnut.dto.TokenValidationRequest;
import com.assignment.walnut.service.CardService;
import com.assignment.walnut.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TokenSystemTests {

    @Autowired
    private CardService cardService;

    @Autowired
    private TokenService tokenService;

    @Test
    void testTokenIssuance() {
        // Given
        String refId = "REF123456";
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setRefId(refId);

        // When
        String token = tokenService.issueToken(tokenRequest);

        // Then
        assertThat(token).isNotBlank();
        System.out.println("Generated Token: " + token);
    }

    @Test
    void testTokenIssuanceAndValidation() {
        // Step 1: 카드 등록
        String uniqueCi = "CI123456" + System.currentTimeMillis();
        CardRegistrationRequest cardRequest = new CardRegistrationRequest();
        cardRequest.setCi(uniqueCi);
        cardRequest.setEncryptedCardInfo("ENC" + System.currentTimeMillis());
        cardRequest.setStoreId("STORE" + System.currentTimeMillis());

        CardRegistrationResponse cardResponse = cardService.registerCard(cardRequest);

        assertThat(cardResponse).isNotNull();
        String refId = cardResponse.getRefId();

        // Step 2: 토큰 발급
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setRefId(refId);

        String token = tokenService.issueToken(tokenRequest);
        assertThat(token).isNotBlank();

        // Step 3: 토큰 유효성 확인
        TokenValidationRequest tokenValidationRequest = new TokenValidationRequest();
        tokenValidationRequest.setToken(token);

        boolean isValid = tokenService.validateToken(tokenValidationRequest);
        assertThat(isValid).isTrue();
    }

    @Test
    void testEndToEndIntegration() {
        // Step 1: 카드 등록
        String uniqueCi = "CI123456" + System.currentTimeMillis();
        CardRegistrationRequest cardRequest = new CardRegistrationRequest();
        cardRequest.setCi(uniqueCi);
        cardRequest.setEncryptedCardInfo("ENC" + System.currentTimeMillis());
        cardRequest.setStoreId("STORE" + System.currentTimeMillis());

        CardRegistrationResponse cardResponse = cardService.registerCard(cardRequest);
        assertThat(cardResponse).isNotNull();
        String refId = cardResponse.getRefId();

        // Step 2: 토큰 발급
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setRefId(refId);

        String token = tokenService.issueToken(tokenRequest);
        assertThat(token).isNotBlank();

        // Step 3: 토큰 유효성 확인
        TokenValidationRequest tokenValidationRequest = new TokenValidationRequest();
        tokenValidationRequest.setToken(token);

        boolean isValid = tokenService.validateToken(tokenValidationRequest);
        assertThat(isValid).isTrue();
    }
}
