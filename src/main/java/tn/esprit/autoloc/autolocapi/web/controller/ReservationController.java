package tn.esprit.autoloc.autolocapi.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.autolocapi.repository.Reservation;
import tn.esprit.autoloc.autolocapi.service.GenericServiceImpl;
import tn.esprit.autoloc.autolocapi.service.ReservationServiceImp;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final GenericServiceImpl<Reservation, Long> reservationService;

    public ReservationController(ReservationServiceImp reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<Reservation> findAll() {
        return reservationService.findAll();
    }

    @GetMapping("/{id}")
    public Reservation findById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reservation ID provided.");
        }

        Reservation reservation = reservationService.findById(id);
        if (reservation == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation with ID " + id + " not found.");
        }
        return reservation;
    }

    @PostMapping
    public Reservation create(@RequestBody Reservation entity) {
        if (entity == null || entity.getDateDebut() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation start date cannot be null.");
        }

        try {
            return reservationService.create(entity);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Map both validation and state errors to a clean 400 Bad Request
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
