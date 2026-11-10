package tn.esprit.autoloc.autolocapi.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.autolocapi.repository.Equipement;
import tn.esprit.autoloc.autolocapi.service.EquipementServiceImp;
import tn.esprit.autoloc.autolocapi.service.GenericServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/equipements")
public class EquipementController {

    private final GenericServiceImpl<Equipement, Long> equipementService;

    public EquipementController(EquipementServiceImp equipementService) {
        this.equipementService = equipementService;
    }

    @GetMapping
    public List<Equipement> findAll() {
        return equipementService.findAll();
    }

    @GetMapping("/{id}")
    public Equipement findById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid equipment ID provided.");
        }

        Equipement equipement = equipementService.findById(id);
        if (equipement == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipement with ID " + id + " not found.");
        }
        return equipement;
    }

    @PostMapping
    public Equipement create(@RequestBody Equipement entity) {
        if (entity == null || entity.getLibelle() == null || entity.getLibelle().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Equipment label cannot be null or empty.");
        }

        try {
            return equipementService.create(entity);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Map both validation and state errors to a clean 400 Bad Request
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
