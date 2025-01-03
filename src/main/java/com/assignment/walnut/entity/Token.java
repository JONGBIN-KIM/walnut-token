package com.assignment.walnut.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tokens")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ref_id", nullable = false, length = 64)
    private String refId;

    @Column(nullable = false, unique = true, length = 64)
    private String token;

    @Column(name = "created_at", nullable = false)
    private java.time.LocalDateTime createdAt;
}
