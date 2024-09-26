package com.kodilla.outdoor.equiprent.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EQUIPMENT")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORY", nullable = false)
    private EquipmentCategory category;
    @OneToOne(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private EquipmentAvailability equipmentAvailability;
    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EquipmentPrice> prices;
    @Column(name = "CREATION_DATE", nullable = false)
    private LocalDateTime creationDate;
}
