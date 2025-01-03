package com.assignment.walnut.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cards")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 88)
    private String ci;

    @Column(name = "ref_id", nullable = false, unique = true, length = 64)
    private String refId;

    @Column(name = "encrypted_card_info", nullable = false, columnDefinition = "TEXT")
    private String encryptedCardInfo;

    @Column(name = "store_id", nullable = false)
    private String storeId;
}
