package com.kodilla.outdoor.equiprent.dto;

import com.kodilla.outdoor.equiprent.domain.CurrencyCode;
import com.kodilla.outdoor.equiprent.domain.Equipment;
import com.kodilla.outdoor.equiprent.domain.Renter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Invoice {
    public static final String INVOICE_TITLE = "INVOICE FOR RENTAL";
    public static final String RENTAL_START_DATE = "Rental start date";
    public static final String RENTAL_END_DATE = "Rental end date";
    public static final String RENTER_DETAILS = "Renter details";
    public static final String RENTED_EQUIPMENT_DETAILS = "Rented equipment details";
    public static final String TOTAL = "TOTAL";
    public static final String GENERATION_DATE = "Generated";

    private LocalDateTime rentalStartDate;
    private LocalDateTime rentalEndDate;
    private Renter renter;
    private Equipment equipment;
    private BigDecimal totalPrice;
    private CurrencyCode currencyCode;
    private LocalDateTime creationDate;
}
