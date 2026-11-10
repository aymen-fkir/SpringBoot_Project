package tn.esprit.autoloc.autolocapi.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.autolocapi.repository.Employe;
import tn.esprit.autoloc.autolocapi.service.EmployeServiceImp;
import tn.esprit.autoloc.autolocapi.service.GenericServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/employes")
public class EmployeController {

    private final GenericServiceImpl<Employe, Long> employeService;

    public EmployeController(EmployeServiceImp employeService) {
        this.employeService = employeService;
    }

    @GetMapping
    public List<Employe> findAll() {
        return employeService.findAll();
    }

    @GetMapping("/{id}")
    public Employe findById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid employee ID provided.");
        }

        Employe employe = employeService.findById(id);
        if (employe == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employe with ID " + id + " not found.");
        }
        return employe;
    }

    @PostMapping
    public Employe create(@RequestBody Employe entity) {
        if (entity == null || entity.getNom() == null || entity.getNom().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee name cannot be null or empty.");
        }

        try {
            return employeService.create(entity);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Map both validation and state errors to a clean 400 Bad Request
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
