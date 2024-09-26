package com.kodilla.outdoor.equiprent.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "RENTERS")
public class Renter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;
    @Column(name = "PHONE_NUMBER", nullable = false)
    private String phoneNumber;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "CREATION_DATE", nullable = false)
    private LocalDateTime creationDate;
    @Column(name = "MODIFICATION_DATE")
    private LocalDateTime modificationDate;
}