package com.kodilla.outdoor.equiprent.service;

import com.kodilla.outdoor.equiprent.controller.exception.RenterAlreadyExistsException;
import com.kodilla.outdoor.equiprent.controller.exception.RenterNotFoundException;
import com.kodilla.outdoor.equiprent.domain.Renter;
import com.kodilla.outdoor.equiprent.repository.RenterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class RenterService {
    private final RenterRepository renterRepository;

    public Renter createRenter(Renter renter) throws RenterAlreadyExistsException {
        if (renterRepository.existsByEmail(renter.getEmail())) {
            throw new RenterAlreadyExistsException(renter.getEmail());
        }

        renter.setCreationDate(LocalDateTime.now());

        return renterRepository.save(renter);
    }

    public List<Renter> getAllRenters() {
        return renterRepository.findAll();
    }

    public Renter getRenter(Long renterId) throws RenterNotFoundException {
        return renterRepository.findById(renterId).orElseThrow(() -> new RenterNotFoundException(renterId));
    }
}
