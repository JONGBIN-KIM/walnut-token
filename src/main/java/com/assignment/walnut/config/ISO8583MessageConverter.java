package com.assignment.walnut.config;

import com.assignment.walnut.dto.CardRegistrationRequest;
import com.assignment.walnut.dto.TokenRequest;
import com.assignment.walnut.dto.TokenValidationRequest;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Component
public class ISO8583MessageConverter implements HttpMessageConverter<Object> {

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return MediaType.TEXT_PLAIN.includes(mediaType);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Collections.singletonList(MediaType.TEXT_PLAIN);
    }

    @Override
    public Object read(Class<?> clazz, HttpInputMessage inputMessage) throws IOException {
        // 요청 URI를 기반으로 분기 처리
        String uri = inputMessage.getHeaders().getFirst("X-Original-URI");

        if (uri == null) {
            throw new IllegalArgumentException("X-Original-URI header is required.");
        }

        byte[] body = inputMessage.getBody().readAllBytes();
        String payload = new String(body, StandardCharsets.UTF_8).trim();

        if (uri != null && uri.contains("/api/cards")) {
            // 카드 등록 API
            return parseCardRegistrationRequest(payload);
        } else if (uri != null && uri.contains("/api/tokens/issue")) {
            // 토큰 발급 API
            return parseTokenRequest(payload);
        } else if (uri != null && uri.contains("/api/tokens/validate")) {
            // 토큰 유효성 확인 API
            return parseTokenValidationRequest(payload);
        }

        throw new IllegalArgumentException("Unsupported URI or payload format.");
    }

    @Override
    public void write(Object o, MediaType mediaType, HttpOutputMessage outputMessage) {
        throw new UnsupportedOperationException("Writing is not supported.");
    }

    // 카드 등록 요청 파싱
    private CardRegistrationRequest parseCardRegistrationRequest(String payload) {
        String ci = payload.substring(0, 20).trim();
        String encryptedCardInfo = payload.substring(20, 276).trim();
        String storeId = payload.substring(276).trim();

        CardRegistrationRequest request = new CardRegistrationRequest();
        request.setCi(ci);
        request.setEncryptedCardInfo(encryptedCardInfo);
        request.setStoreId(storeId);
        return request;
    }

    // 토큰 요청 파싱
    private TokenRequest parseTokenRequest(String payload) {
        String refId = payload.substring(0, 20).trim();

        TokenRequest request = new TokenRequest();
        request.setRefId(refId);
        return request;
    }

    // 토큰 유효성 확인 요청 파싱
    private TokenValidationRequest parseTokenValidationRequest(String payload) {
        String token = payload.substring(0, 32).trim();

        TokenValidationRequest request = new TokenValidationRequest();
        request.setToken(token);
        return request;
    }
}
