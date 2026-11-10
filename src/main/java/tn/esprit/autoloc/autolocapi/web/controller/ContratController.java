package tn.esprit.autoloc.autolocapi.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.autolocapi.repository.Contrat;
import tn.esprit.autoloc.autolocapi.service.ContractServiceImp;
import tn.esprit.autoloc.autolocapi.service.GenericServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/contrats")
public class ContratController {

    private final GenericServiceImpl<Contrat, Long> contratService;

    public ContratController(ContractServiceImp contratService) {
        this.contratService = contratService;
    }

    @GetMapping
    public List<Contrat> findAll() {
        return contratService.findAll();
    }

    @GetMapping("/{id}")
    public Contrat findById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid contract ID provided.");
        }

        Contrat contrat = contratService.findById(id);
        if (contrat == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contrat with ID " + id + " not found.");
        }
        return contrat;
    }

    @PostMapping
    public Contrat create(@RequestBody Contrat entity) {
        if (entity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contract cannot be null.");
        }

        try {
            return contratService.create(entity);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Map both validation and state errors to a clean 400 Bad Request
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
