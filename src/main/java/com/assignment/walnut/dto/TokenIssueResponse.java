package com.assignment.walnut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenIssueResponse {
    private String token;
    private String message;
}
