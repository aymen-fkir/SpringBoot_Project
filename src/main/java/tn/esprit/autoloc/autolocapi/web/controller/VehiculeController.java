package tn.esprit.autoloc.autolocapi.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.Vehicule;
import tn.esprit.autoloc.autolocapi.service.VehiculeServiceImp;

import java.util.List;

@RestController
@RequestMapping("/vehicules")
public class VehiculeController {

    private final VehiculeServiceImp vehiculeService;

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    // 2. API REST pour désaffecter un véhicule
    @PutMapping("/{id}/desaffecter")
    public Vehicule desaffecter(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid vehicle ID provided.");
        }
        try {
            return vehiculeService.desaffecter(id);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/desaffecter/{id}")
    public Vehicule desaffecterAlternative(@PathVariable Long id) {
        return desaffecter(id);
    }

    // 5. API REST pour affecter une agence acceptant les deux identifiants dans l'URL
    @PutMapping("/{idVehicule}/agence/{idAgence}")
    public Vehicule affecterAgence(
            @PathVariable Long idVehicule,
            @PathVariable Long idAgence) {
        if (idVehicule == null || idVehicule <= 0 || idAgence == null || idAgence <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid vehicle or agency ID provided.");
        }
        try {
            return vehiculeService.affecterAgence(idVehicule, idAgence);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/affecter/{idVehicule}/{idAgence}")
    public Vehicule affecterAgenceAlternative(
            @PathVariable Long idVehicule,
            @PathVariable Long idAgence) {
        return affecterAgence(idVehicule, idAgence);
    }

    // API REST pour affecter un équipement à un véhicule acceptant les deux identifiants dans l'URL
    @PutMapping("/{idVehicule}/equipement/{idEquipement}")
    public Vehicule affecterEquipement(
            @PathVariable Long idVehicule,
            @PathVariable Long idEquipement) {
        if (idVehicule == null || idVehicule <= 0 || idEquipement == null || idEquipement <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid vehicle or equipment ID provided.");
        }
        try {
            return vehiculeService.affecterEquipement(idVehicule, idEquipement);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid vehicle ID provided.");
        }
        try {
            vehiculeService.deleteByid(id);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }
}
