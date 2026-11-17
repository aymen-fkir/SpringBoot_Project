package tn.esprit.autoloc.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.Agence;
import tn.esprit.autoloc.autolocapi.repository.IAgenceRepository;
import tn.esprit.autoloc.autolocapi.repository.Vehicule;
import tn.esprit.autoloc.autolocapi.service.interfaces.IAgenceService;

import java.util.List;

@Service
public class BasicAgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;
    private final VehiculeServiceImp vehiculeService;

    public BasicAgenceServiceImpl(IAgenceRepository agenceRepository) {
        this.agenceRepository = agenceRepository;
        this.vehiculeService = new VehiculeServiceImp(null);
    }

    @Autowired
    public BasicAgenceServiceImpl(IAgenceRepository agenceRepository, @Autowired(required = false) VehiculeServiceImp vehiculeService) {
        this.agenceRepository = agenceRepository;
        this.vehiculeService = vehiculeService != null ? vehiculeService : new VehiculeServiceImp(null);
    }

    @Override
    public boolean check(Agence agence, boolean isNew) {
        return Utils.isNotEmpty(agence.getNom()) && Utils.isNotEmpty(agence.getTelephone());
    }

    @Override
    public Agence create(Agence agence) {
        if (!check(agence, true)) {
            throw new IllegalArgumentException("id can't be null");
        }
        if (!check(agence, true)) {
            throw new IllegalStateException("Agence non valide");
        }
        return agenceRepository.save(agence);
    }

    @Override
    public Agence findById(Long id) {
        return agenceRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Agence" , Long.toString(id)));
    }

    @Override
    public List<Agence> findAll() {
        return agenceRepository.findAll();
    }

    @Override
    public Agence update(Long id, Agence agence) {
        if (!this.check(agence, false)) {
            return null;
        }
        this.findById(id);
        return agenceRepository.save(agence);
    }

    @Override
    public void deleteByid(Long id) {
        this.findById(id);
        agenceRepository.deleteById(id);
    }

    @Override
    public boolean hasVehicules(Long id) {
        return false;
    }

    @Override
    public String simuler(Agence agence, int nb_jour, VehiculeOffre offre) {
        throw new UnsupportedOperationException("La méthode simuler n'est pas supportée dans l'implémentation basique.");
    }

    @Override
    public String simuler(Long id, int nb_jour, VehiculeOffre offre) {
        throw new UnsupportedOperationException("La méthode simuler n'est pas supportée dans l'implémentation basique.");
    }
}
