package com.assignment.walnut.controller;

import com.assignment.walnut.dto.TokenIssueResponse;
import com.assignment.walnut.dto.TokenRequest;
import com.assignment.walnut.dto.TokenValidationRequest;
import com.assignment.walnut.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tokens")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping(value = "/issue", consumes = "text/plain", produces = "application/json")
    public ResponseEntity<TokenIssueResponse> issueToken(@RequestBody TokenRequest request) {
        String token = tokenService.issueToken(request);
        return ResponseEntity.ok(new TokenIssueResponse(token, "Token issued successfully."));
    }

    @GetMapping(value = "/validate", produces = "application/json")
    public ResponseEntity<Boolean> validateToken(@RequestParam("token") String token) {
        boolean isValid = tokenService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }
}
