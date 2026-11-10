package tn.esprit.autoloc.autolocapi.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.autolocapi.repository.Agence;
import tn.esprit.autoloc.autolocapi.service.AgenceServiceImpl;
import tn.esprit.autoloc.autolocapi.service.GenericServiceImpl;

import java.util.List;



@RestController
@RequestMapping("/agences")
public class AgenceController {

    private final GenericServiceImpl<Agence, Long> agenceService;

    public AgenceController(AgenceServiceImpl agenceService) {
        this.agenceService = agenceService;
    }

    @GetMapping
    public List<Agence> findAll() {
        return agenceService.findAll();
    }

    @GetMapping("/{id}")
    public Agence findById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid agency ID provided.");
        }

        Agence agence = agenceService.findById(id);
        if (agence == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Agence with ID " + id + " not found.");
        }
        return agence;
    }

    @PostMapping
    public Agence create(@RequestBody Agence entity) {
        if (entity == null || entity.getNom() == null || entity.getNom().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agency name cannot be null or empty.");
        }

        try {
            return agenceService.create(entity);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Map both validation and state errors to a clean 400 Bad Request
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}