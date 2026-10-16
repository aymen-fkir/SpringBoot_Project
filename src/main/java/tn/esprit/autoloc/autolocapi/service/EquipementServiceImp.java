package tn.esprit.autoloc.autolocapi.service;

import jdk.jshell.execution.Util;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.repository.Equipement;

import java.util.Optional;

public class EquipementServiceImp extends GenericServiceImpl<Equipement,Long>{
    public EquipementServiceImp(JpaRepository<Equipement, Long> repository) {
        super(repository);
    }

    @Override
    public boolean check(Equipement entity, boolean isNew) {
        return Utils.isNotEmpty(entity.getIdEquipement().toString());
    }

    @Override
    public String getEntityName() {
        return "Equipement";
    }

    @Override
    public Long getId(Equipement entity) {
        return entity.getIdEquipement();
    }

    @Override
    public void setFields(Long aLong, Equipement entity) {
        Optional<Equipement> equipementOptional = repository.findById(aLong);
        if (equipementOptional.isPresent()) {
            entity.setIdEquipement(equipementOptional.get().getIdEquipement());
            repository.save(entity);
        }
    }
}
