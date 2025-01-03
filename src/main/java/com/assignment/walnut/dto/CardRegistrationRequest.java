package com.assignment.walnut.dto;

import lombok.Data;

@Data
public class CardRegistrationRequest {
    private String ci;
    private String encryptedCardInfo;
    private String storeId;
}
