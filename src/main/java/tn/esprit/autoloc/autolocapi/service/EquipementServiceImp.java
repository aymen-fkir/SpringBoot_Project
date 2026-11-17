package tn.esprit.autoloc.autolocapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.repository.Equipement;
import tn.esprit.autoloc.autolocapi.repository.IVehiculeRepository;
import tn.esprit.autoloc.autolocapi.repository.Vehicule;

import java.util.Optional;

@Service
public class EquipementServiceImp extends GenericServiceImpl<Equipement, Long> {

    private final IVehiculeRepository vehiculeRepository;

    @Autowired
    public EquipementServiceImp(
            JpaRepository<Equipement, Long> repository,
            @Autowired(required = false) IVehiculeRepository vehiculeRepository) {
        super(repository);
        this.vehiculeRepository = vehiculeRepository;
    }

    public EquipementServiceImp(JpaRepository<Equipement, Long> repository) {
        this(repository, null);
    }

    @Override
    public boolean check(Equipement entity, boolean isNew) {
        return entity != null && Utils.isNotEmpty(entity.getLibelle());
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

    // 9. Supprimer l'équipement sans aucun problème (délie des véhicules avant suppression)
    @Override
    @Transactional
    public void deleteByid(Long id) {
        Equipement equipement = findById(id);
        if (equipement.getVehicules() != null) {
            for (Vehicule v : equipement.getVehicules()) {
                if (v.getEquipements() != null) {
                    v.getEquipements().remove(equipement);
                    if (vehiculeRepository != null) {
                        vehiculeRepository.save(v);
                    }
                }
            }
        }
        repository.delete(equipement);
    }
}
