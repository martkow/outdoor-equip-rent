package com.kodilla.outdoor.equiprent.mapper;

import com.kodilla.outdoor.equiprent.domain.Rental;
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
               rental.getTotalPrice()
       );
   }

   public List<RentalDto> mapRentalListToRentalDtoList(List<Rental> rentals) {
       return rentals.stream()
               .map(this::mapRentalToRentalDto)
               .toList();
   }
}
