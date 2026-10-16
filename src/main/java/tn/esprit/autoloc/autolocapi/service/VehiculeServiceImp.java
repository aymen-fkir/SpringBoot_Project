package tn.esprit.autoloc.autolocapi.service;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.repository.Vehicule;

import java.util.Optional;

public class VehiculeServiceImp extends GenericServiceImpl<Vehicule,Long>{
    public VehiculeServiceImp(JpaRepository<Vehicule, Long> repository) {
        super(repository);
    }

    @Override
    public boolean check(Vehicule entity, boolean isNew) {
        return Utils.isNotEmpty(entity.getIdVehicule().toString());
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
}
