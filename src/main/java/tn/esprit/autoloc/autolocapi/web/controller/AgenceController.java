package tn.esprit.autoloc.autolocapi.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import tn.esprit.autoloc.autolocapi.repository.Agence;
import tn.esprit.autoloc.autolocapi.service.AgenceServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/agences")
public class AgenceController {

    private final AgenceServiceImpl agenceService;

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

    @GetMapping("/{id}/simuler")
    public String simuler(
            @PathVariable Long id,
            @RequestParam(name = "nbJours", required = false) Integer nbJours,
            @RequestParam(name = "nb_jour", required = false) Integer nb_jour,
            @RequestParam(name = "offre", required = false) VehiculeOffre offre) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid agency ID provided.");
        }
        int jours = nbJours != null ? nbJours : (nb_jour != null ? nb_jour : 1);
        try {
            return agenceService.simuler(id, jours, offre);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/{id}/simuler/{nbJours}/{offre}")
    public String simulerPath(
            @PathVariable Long id,
            @PathVariable int nbJours,
            @PathVariable VehiculeOffre offre) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid agency ID provided.");
        }
        return agenceService.simuler(id, nbJours, offre);
    }
}
