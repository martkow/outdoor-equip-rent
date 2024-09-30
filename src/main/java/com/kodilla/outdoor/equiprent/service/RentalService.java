package com.kodilla.outdoor.equiprent.service;

import com.kodilla.outdoor.equiprent.dto.CreateRentalDto;
import com.kodilla.outdoor.equiprent.exception.*;
import com.kodilla.outdoor.equiprent.domain.*;
import com.kodilla.outdoor.equiprent.external.api.nbp.pl.client.ApiNbpPlClient;
import com.kodilla.outdoor.equiprent.repository.EquipmentPriceRepository;
import com.kodilla.outdoor.equiprent.repository.EquipmentRepository;
import com.kodilla.outdoor.equiprent.repository.RentalRepository;
import com.kodilla.outdoor.equiprent.repository.RenterRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class RentalService {
    private final RentalRepository rentalRepository;
    private final EquipmentRepository equipmentRepository;
    private final EquipmentPriceRepository equipmentPriceRepository;
    private final RenterRepository renterRepository;
    private final ApiNbpPlClient apiNbpPlClient;

    public Rental createRental(CreateRentalDto createRentalDto, CurrencyCode currencyCode) throws EquipmentNotFoundException, EquipmentNotAvailableException, TierNotAvailableException, RenterNotFoundException, ExchangeRateNotAvailableException {
        Equipment equipment = equipmentRepository.findById(createRentalDto.getEquipmentId())
                .orElseThrow(() -> new EquipmentNotFoundException(createRentalDto.getEquipmentId()));

        if (!equipment.getEquipmentAvailability().isAvailable() || equipment.getEquipmentAvailability().getCurrentQuantity() <= 0) {
            throw new EquipmentNotAvailableException(createRentalDto.getEquipmentId());
        }

        EquipmentPrice equipmentPrice = equipmentPriceRepository.findById(createRentalDto.getRentalTierId())
                .orElseThrow(() -> new TierNotAvailableException(createRentalDto.getRentalTierId()));

        LocalDateTime rentalStart = LocalDateTime.now();
        LocalDateTime rentalEnd = switch (equipmentPrice.getTier()) {
            case HOUR -> rentalStart.plusHours(createRentalDto.getRentalTierQuantity());
            case DAY -> rentalStart.plusDays(createRentalDto.getRentalTierQuantity());
            case WEEK -> rentalStart.plusWeeks(createRentalDto.getRentalTierQuantity());
        };

        Renter renter = renterRepository.findById(createRentalDto.getRenterId())
                .orElseThrow(() -> new RenterNotFoundException(createRentalDto.getRenterId()));

        Rental rental = Rental.builder()
                .equipment(equipment)
                .renter(renter)
                .rentalStart(rentalStart)
                .rentalEnd(rentalEnd)
                .status(RentalStatus.ACTIVE)
                .totalPrice(calculateRentalTotalPrice(equipmentPrice, createRentalDto.getRentalTierQuantity(), currencyCode))
                .currencyCode(currencyCode)
                .creationDate(LocalDateTime.now())
                .build();

        equipment.getEquipmentAvailability().setCurrentQuantity(equipment.getEquipmentAvailability().getCurrentQuantity() - 1);
        equipmentRepository.save(equipment);

        return rentalRepository.save(rental);
    }

    private BigDecimal calculateRentalTotalPrice(EquipmentPrice equipmentPrice, Integer rentalTierQuantity, CurrencyCode currencyCode) throws ExchangeRateNotAvailableException {
        BigDecimal totalPriceInPln = equipmentPrice.getPrice().multiply(new BigDecimal(rentalTierQuantity));

        if (currencyCode.equals(CurrencyCode.EUR)) {
           return totalPriceInPln.divide(BigDecimal.valueOf(apiNbpPlClient.getEuroExchangeRate().getRates().get(0).getAverageExchangeRate()), 4, RoundingMode.HALF_UP);
       }

        return totalPriceInPln;
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public List<Rental> getRentalByStatuses(List<RentalStatus> statuses) {
        if (statuses.isEmpty()) {
            return getAllRentals();
        } else {
            return rentalRepository.findByStatusIn(statuses);
        }
    }

    public Rental updateRentalStatus(Long rentalId, RentalStatus rentalStatus) throws RentalNotFoundException, InvalidRentalStatusChangeException {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));

        if (!rental.getStatus().equals(RentalStatus.ACTIVE)) {
            throw new InvalidRentalStatusChangeException(rentalStatus, rental.getStatus());
        }

        if (!rentalStatus.equals(RentalStatus.ACTIVE)) {
            rental.setStatus(rentalStatus);
            rental.getEquipment().getEquipmentAvailability().setCurrentQuantity(rental.getEquipment().getEquipmentAvailability().getCurrentQuantity() + 1);
            rental.setModificationDate(LocalDateTime.now());
        }

        return rentalRepository.save(rental);
    }

    public List<Rental> getRentalsByRenterId(Long renterId) {
        return rentalRepository.findByRenterId(renterId);
    }
}
