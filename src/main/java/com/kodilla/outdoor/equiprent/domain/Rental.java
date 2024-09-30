package com.kodilla.outdoor.equiprent.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "RENTALS")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "EQUIPMENT_ID", nullable = false)
    private Equipment equipment;
    @ManyToOne
    @JoinColumn(name = "RENTER_ID", nullable = false)
    private Renter renter;
    @Column(name = "RENTAL_START", nullable = false)
    private LocalDateTime rentalStart;
    @Column(name = "RENTAL_END", nullable = false)
    private LocalDateTime rentalEnd;
    @Column(name = "RETURN_DATE")
    private LocalDateTime returnDate;
    @Column(name = "STATUS", nullable = false)
    private RentalStatus status;
    @Column(name = "TOTAL_PRICE", nullable = false)
    private BigDecimal totalPrice;
    @Column(name = "CURRENCY_CODE", nullable = false)
    private CurrencyCode currencyCode;
    @Column(name = "CREATION_DATE", nullable = false)
    private LocalDateTime creationDate;
    @Column(name = "MODIFICATION_DATE")
    private LocalDateTime modificationDate;
}
