package tn.esprit.autoloc.autolocapi.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.autolocapi.repository.Maintenance;
import tn.esprit.autoloc.autolocapi.service.GenericServiceImpl;
import tn.esprit.autoloc.autolocapi.service.MaintenanceServiceImp;

import java.util.List;

@RestController
@RequestMapping("/maintenances")
public class MaintenanceController {

    private final GenericServiceImpl<Maintenance, Long> maintenanceService;

    public MaintenanceController(MaintenanceServiceImp maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @GetMapping
    public List<Maintenance> findAll() {
        return maintenanceService.findAll();
    }

    @GetMapping("/{id}")
    public Maintenance findById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid maintenance ID provided.");
        }

        Maintenance maintenance = maintenanceService.findById(id);
        if (maintenance == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Maintenance with ID " + id + " not found.");
        }
        return maintenance;
    }

    @PostMapping
    public Maintenance create(@RequestBody Maintenance entity) {
        if (entity == null || entity.getDescription() == null || entity.getDescription().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maintenance description cannot be null or empty.");
        }

        try {
            return maintenanceService.create(entity);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Map both validation and state errors to a clean 400 Bad Request
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
