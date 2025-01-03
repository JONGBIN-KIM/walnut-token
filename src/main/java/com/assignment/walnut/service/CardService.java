package com.assignment.walnut.service;

import com.assignment.walnut.dto.CardRegistrationRequest;
import com.assignment.walnut.dto.CardRegistrationResponse;
import com.assignment.walnut.entity.Card;
import com.assignment.walnut.repository.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Service
public class CardService {

    private final CardRepository cardRepository;

    private static final Logger logger = LoggerFactory.getLogger(CardService.class);

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public CardRegistrationResponse registerCard(CardRegistrationRequest request) {
        // Step 1: ref_id 생성 (SHA-256 암호화)
        String refId = generateRefId(request.getStoreId(), request.getCi(), request.getEncryptedCardInfo());

        // Step 2: Card 엔티티 저장
        Card card = Card.builder()
                .ci(request.getCi())
                .encryptedCardInfo(request.getEncryptedCardInfo())
                .storeId(request.getStoreId())
                .refId(refId)
                .build();

        cardRepository.save(card);

        logger.info("[ref_id 생성] refId : "+refId);

        // Step 3: 응답 반환
        return new CardRegistrationResponse(refId, "Card registered successfully.");
    }

    private String generateRefId(String storeId, String ci, String encryptedCardInfo) {
        try {
            // 데이터 조합 (요약)
            String partialCi = ci.substring(0, Math.min(8, ci.length()));
            String partialEncryptedCardInfo = encryptedCardInfo.substring(Math.max(0, encryptedCardInfo.length() - 8));
            String combined = storeId + partialCi + partialEncryptedCardInfo;

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(combined.getBytes(StandardCharsets.UTF_8));

            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating ref_id", e);
        }
    }

}
