package tn.esprit.autoloc.autolocapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.calcul.*;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.Agence;
import tn.esprit.autoloc.autolocapi.repository.Equipement;
import tn.esprit.autoloc.autolocapi.repository.IAgenceRepository;
import tn.esprit.autoloc.autolocapi.repository.IEquipementRepository;
import tn.esprit.autoloc.autolocapi.repository.Vehicule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@Service
public class VehiculeServiceImp extends GenericServiceImpl<Vehicule, Long> {

    private final Map<String, IOffreStrategy> strategies;
    private final IAgenceRepository agenceRepository;
    private final IEquipementRepository equipementRepository;

    @Autowired
    public VehiculeServiceImp(
            JpaRepository<Vehicule, Long> repository,
            @Autowired(required = false) Map<String, IOffreStrategy> strategies,
            @Autowired(required = false) IAgenceRepository agenceRepository,
            @Autowired(required = false) IEquipementRepository equipementRepository) {
        super(repository);
        this.strategies = (strategies != null && !strategies.isEmpty()) ? strategies : initDefaultStrategies();
        this.agenceRepository = agenceRepository;
        this.equipementRepository = equipementRepository;
    }

    public VehiculeServiceImp(JpaRepository<Vehicule, Long> repository) {
        this(repository, initDefaultStrategies(), null, null);
    }

    private static Map<String, IOffreStrategy> initDefaultStrategies() {
        Map<String, IOffreStrategy> map = new HashMap<>();
        map.put("STANDARD", new StandardOffreStrategy());
        map.put("WEEKEND", new WeekendOffreStrategy());
        map.put("LONGUE_DUREE", new LongueDureeOffreStrategy());
        return map;
    }

    @Override
    public boolean check(Vehicule entity, boolean isNew) {
        return entity != null && Utils.isNotEmpty(entity.getImmatriculation());
    }

    @Override
    public String getEntityName() {
        return "Vehicule";
    }

    @Override
    public Long getId(Vehicule entity) {
        return entity.getIdVehicule();
    }

    @Override
    public void setFields(Long aLong, Vehicule entity) {
        Optional<Vehicule> vehicule = Optional.ofNullable(entity);
        if (vehicule.isPresent()) {
            Vehicule vehiculeEntity = vehicule.get();
            entity.setIdVehicule(vehiculeEntity.getIdVehicule());
            repository.save(entity);
        }
    }

    public double calculTarif(Vehicule entity, int nb_jour, VehiculeOffre offre) {
        if (entity == null || entity.getTarifJournalier() == null) {
            return 0.0;
        }
        double tarif = entity.getTarifJournalier().doubleValue();
        String key = (offre != null) ? offre.name() : "STANDARD";
        IOffreStrategy strategy = strategies.get(key);
        if (strategy == null) {
            strategy = strategies.get("STANDARD");
        }
        return strategy != null ? strategy.calculTarif(tarif, nb_jour) : (tarif * nb_jour);
    }

    public Map<String, IOffreStrategy> getStrategies() {
        return strategies;
    }

    // 1. Désaffecter un véhicule (code minimaliste)
    @Transactional
    public Vehicule desaffecter(Long idVehicule) {
        Vehicule v = findById(idVehicule);
        v.setAgence(null);
        return repository.save(v);
    }

    public Vehicule desaffecterVehicule(Long idVehicule) {
        return desaffecter(idVehicule);
    }

    // 4. Affecter une agence à un véhicule choisi
    @Transactional
    public Vehicule affecterAgence(Long idVehicule, Long idAgence) {
        Vehicule v = findById(idVehicule);
        Agence a = (agenceRepository != null)
                ? agenceRepository.findById(idAgence)
                    .orElseThrow(() -> new ResourceNotFoundException("Agence", String.valueOf(idAgence)))
                : null;
        v.setAgence(a);
        return repository.save(v);
    }

    public Vehicule affecterAgenceAVehicule(Long idAgence, Long idVehicule) {
        return affecterAgence(idVehicule, idAgence);
    }

    // 6. Affecter un équipement à un véhicule
    @Transactional
    public Vehicule affecterEquipement(Long idVehicule, Long idEquipement) {
        Vehicule v = findById(idVehicule);
        Equipement e = (equipementRepository != null)
                ? equipementRepository.findById(idEquipement)
                    .orElseThrow(() -> new ResourceNotFoundException("Equipement", String.valueOf(idEquipement)))
                : null;
        if (v.getEquipements() == null) {
            v.setEquipements(new HashSet<>());
        }
        if (e != null) {
            v.getEquipements().add(e);
        }
        return repository.save(v);
    }

    public Vehicule affecterEquipementAVehicule(Long idEquipement, Long idVehicule) {
        return affecterEquipement(idVehicule, idEquipement);
    }

    // 8. Suppression sécurisée : exception si le véhicule possède des équipements
    @Override
    @Transactional
    public void deleteByid(Long id) {
        Vehicule v = findById(id);
        if (v.getEquipements() != null && !v.getEquipements().isEmpty()) {
            throw new IllegalStateException("Impossible de supprimer le véhicule " + id + " car il possède des équipements associés.");
        }
        repository.deleteById(id);
    }
}
