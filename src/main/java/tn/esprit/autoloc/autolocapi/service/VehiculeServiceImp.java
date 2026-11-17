package tn.esprit.autoloc.autolocapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.calcul.*;
import tn.esprit.autoloc.autolocapi.repository.Vehicule;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class VehiculeServiceImp extends GenericServiceImpl<Vehicule, Long> {

    private final Map<String, IOffreStrategy> strategies;

    @Autowired
    public VehiculeServiceImp(
            JpaRepository<Vehicule, Long> repository,
            @Autowired(required = false) Map<String, IOffreStrategy> strategies) {
        super(repository);
        this.strategies = (strategies != null && !strategies.isEmpty()) ? strategies : initDefaultStrategies();
    }

    public VehiculeServiceImp(JpaRepository<Vehicule, Long> repository) {
        this(repository, initDefaultStrategies());
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
}
