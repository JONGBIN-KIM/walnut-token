package com.assignment.walnut.service;

import com.assignment.walnut.dto.TokenRequest;
import com.assignment.walnut.dto.TokenValidationRequest;
import com.assignment.walnut.entity.Token;
import com.assignment.walnut.repository.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String issueToken(TokenRequest request) {
        try {
            // Step 1: 토큰 생성 (refId + storeId + timestamp 조합)
            String rawTokenData = request.getRefId() + "|" + LocalDateTime.now();
            String hashedToken = hashWithSHA256(rawTokenData);

            logger.info("[토큰생성] hashedToken : "+hashedToken);

            // Step 2: 토큰 엔티티 생성 및 저장
            Token tokenEntity = Token.builder()
                    .refId(request.getRefId())
                    .token(hashedToken)
                    .createdAt(LocalDateTime.now())
                    .build();

            tokenRepository.save(tokenEntity);
            logger.info("[토큰저장완료] : "+tokenRepository.findAll());

            // Step 3: 생성된 토큰 반환
            return hashedToken;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Error generating token hash", e);
        }
    }

    private String hashWithSHA256(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    public boolean validateToken(String token) {
        logger.info("[토큰 유효성 확인] token : "+token);
        return tokenRepository.findByToken(token).isPresent();
    }
}
