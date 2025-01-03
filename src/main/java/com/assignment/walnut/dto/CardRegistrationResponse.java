package com.assignment.walnut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardRegistrationResponse {
    private String refId;
    private String message;
}
