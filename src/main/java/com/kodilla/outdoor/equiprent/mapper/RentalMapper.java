package com.kodilla.outdoor.equiprent.mapper;

import com.kodilla.outdoor.equiprent.domain.Rental;
import com.kodilla.outdoor.equiprent.dto.Invoice;
import com.kodilla.outdoor.equiprent.dto.RentalDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RentalMapper {
   public RentalDto mapRentalToRentalDto(Rental rental) {
       return new RentalDto(
               rental.getId(),
               rental.getEquipment().getId(),
               rental.getRenter().getId(),
               rental.getRentalStart().toString(),
               rental.getRentalEnd().toString(),
               rental.getStatus().toString(),
               rental.getTotalPrice(),
               rental.getCurrencyCode().toString()
       );
   }

   public List<RentalDto> mapRentalListToRentalDtoList(List<Rental> rentals) {
       return rentals.stream()
               .map(this::mapRentalToRentalDto)
               .toList();
   }

   public Invoice mapRentalToInvoice(Rental rental) {
       return Invoice.builder()
               .rentalStartDate(rental.getRentalStart())
               .rentalEndDate(rental.getRentalEnd())
               .renter(rental.getRenter())
               .equipment(rental.getEquipment())
               .totalPrice(rental.getTotalPrice())
               .currencyCode(rental.getCurrencyCode())
               .build();
   }
}
