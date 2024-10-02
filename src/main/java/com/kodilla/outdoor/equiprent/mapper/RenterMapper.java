package com.kodilla.outdoor.equiprent.mapper;

import com.kodilla.outdoor.equiprent.domain.Renter;
import com.kodilla.outdoor.equiprent.dto.CreateUpdateRenterDto;
import com.kodilla.outdoor.equiprent.dto.RenterDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RenterMapper {
    public RenterDto mapRenterToRenterDto(Renter renter) {
        return RenterDto.builder()
                .id(renter.getId())
                .firstName(renter.getFirstName())
                .lastName(renter.getLastName())
                .email(renter.getEmail())
                .phoneNumber(renter.getPhoneNumber())
                .address(renter.getAddress())
                .build();
    }

    public Renter mapCreateUpdateRenterDtoToRenter(CreateUpdateRenterDto createUpdateRenterDto) {
        return Renter.builder()
                .firstName(createUpdateRenterDto.getFirstName())
                .lastName(createUpdateRenterDto.getLastName())
                .email(createUpdateRenterDto.getEmail())
                .phoneNumber(createUpdateRenterDto.getPhoneNumber())
                .address(createUpdateRenterDto.getAddress())
                .build();
    }

    public List<RenterDto> mapRenterListToRenterDtoList(List<Renter> renters) {
        return renters.stream()
                .map(this::mapRenterToRenterDto)
                .toList();
    }
}
