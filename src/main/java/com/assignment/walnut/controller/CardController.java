package com.assignment.walnut.controller;

import com.assignment.walnut.dto.CardRegistrationRequest;
import com.assignment.walnut.dto.CardRegistrationResponse;
import com.assignment.walnut.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping(consumes = "text/plain", produces = "application/json")
    public ResponseEntity<CardRegistrationResponse> registerCard(@RequestBody CardRegistrationRequest request) {
        CardRegistrationResponse response = cardService.registerCard(request);
        return ResponseEntity.ok(response);
    }
}
