package tn.esprit.autoloc.autolocapi.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.autolocapi.repository.Paiement;
import tn.esprit.autoloc.autolocapi.service.GenericServiceImpl;
import tn.esprit.autoloc.autolocapi.service.PaiementServiceImp;

import java.util.List;

@RestController
@RequestMapping("/paiements")
public class PaiementController {

    private final GenericServiceImpl<Paiement, Long> paiementService;

    public PaiementController(PaiementServiceImp paiementService) {
        this.paiementService = paiementService;
    }

    @GetMapping
    public List<Paiement> findAll() {
        return paiementService.findAll();
    }

    @GetMapping("/{id}")
    public Paiement findById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid payment ID provided.");
        }

        Paiement paiement = paiementService.findById(id);
        if (paiement == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paiement with ID " + id + " not found.");
        }
        return paiement;
    }

    @PostMapping
    public Paiement create(@RequestBody Paiement entity) {
        if (entity == null || entity.getMontant() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment amount cannot be null.");
        }

        try {
            return paiementService.create(entity);
        } catch (IllegalArgumentException | IllegalStateException | UnsupportedOperationException e) {
            // Map both validation and state errors to a clean 400 Bad Request
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
