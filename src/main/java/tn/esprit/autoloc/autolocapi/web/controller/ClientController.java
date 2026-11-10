package tn.esprit.autoloc.autolocapi.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.autolocapi.repository.Client;
import tn.esprit.autoloc.autolocapi.service.ClientServiceImp;
import tn.esprit.autoloc.autolocapi.service.GenericServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final GenericServiceImpl<Client, Long> clientService;

    public ClientController(ClientServiceImp clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> findAll() {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public Client findById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid client ID provided.");
        }

        Client client = clientService.findById(id);
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client with ID " + id + " not found.");
        }
        return client;
    }

    @PostMapping
    public Client create(@RequestBody Client entity) {
        if (entity == null || entity.getNom() == null || entity.getNom().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client name cannot be null or empty.");
        }

        try {
            return clientService.create(entity);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Map both validation and state errors to a clean 400 Bad Request
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
