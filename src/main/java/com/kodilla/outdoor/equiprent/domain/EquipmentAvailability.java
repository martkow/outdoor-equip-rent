package com.kodilla.outdoor.equiprent.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EQUIPMENT_AVAILABILITY")
public class EquipmentAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @OneToOne
    @JoinColumn(name = "EQUIPMENT_ID", nullable = false, unique = true)
    private Equipment equipment;
    @Column(name = "INITIAL_QUANTITY", nullable = false)
    private Long initialQuantity;
    @Column(name = "CURRENT_QUANTITY", nullable = false)
    private Long currentQuantity;
    @Column(name = "AVAILABLE", nullable = false)
    private boolean available;
    @Column(name = "CREATION_DATE", nullable = false)
    private LocalDateTime creationDate;
}