package com.kodilla.outdoor.equiprent.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EQUIPMENT_PRICE")
public class EquipmentPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "EQUIPMENT_ID", nullable = false)
    private Equipment equipment;
    @Enumerated(EnumType.STRING)
    @Column(name = "PRICE_TIER", nullable = false)
    private Tier tier;
    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;
    @Column(name = "CREATION_DATE", nullable = false)
    private LocalDateTime creationDate;
}