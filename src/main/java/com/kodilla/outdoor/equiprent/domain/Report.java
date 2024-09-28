package com.kodilla.outdoor.equiprent.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "REPORTS")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TOTAL_RENTALS", nullable = false)
    private long totalRentals;
    @Column(name = "TOTAL_RETURNS", nullable = false)
    private long totalReturns;
    @Column(name = "OVERDUE_RENTALS", nullable = false)
    private long overdueRentals;
    @Column(name = "REPORT_START_DATE", nullable = false)
    private LocalDateTime reportStartDate;
    @Column(name = "REPORT_END_DATE", nullable = false)
    private LocalDateTime reportEndDate;
    @Column(name = "CREATION_DATE", nullable = false)
    private LocalDateTime creationDate;
}