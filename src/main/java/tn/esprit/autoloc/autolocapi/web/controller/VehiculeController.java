package tn.esprit.autoloc.autolocapi.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.autolocapi.repository.Vehicule;
import tn.esprit.autoloc.autolocapi.service.GenericServiceImpl;
import tn.esprit.autoloc.autolocapi.service.VehiculeServiceImp;

import java.util.List;

@RestController
@RequestMapping("/vehicules")
public class VehiculeController {

    private final GenericServiceImpl<Vehicule, Long> vehiculeService;

    public VehiculeController(VehiculeServiceImp vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @GetMapping
    public List<Vehicule> findAll() {
        return vehiculeService.findAll();
    }

    @GetMapping("/{id}")
    public Vehicule findById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid vehicle ID provided.");
        }

        Vehicule vehicule = vehiculeService.findById(id);
        if (vehicule == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicule with ID " + id + " not found.");
        }
        return vehicule;
    }

    @PostMapping
    public Vehicule create(@RequestBody Vehicule entity) {
        if (entity == null || entity.getImmatriculation() == null || entity.getImmatriculation().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle registration cannot be null or empty.");
        }

        try {
            return vehiculeService.create(entity);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Map both validation and state errors to a clean 400 Bad Request
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
