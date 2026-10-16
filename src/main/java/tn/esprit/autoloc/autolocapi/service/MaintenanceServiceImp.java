package tn.esprit.autoloc.autolocapi.service;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.repository.Maintenance;

import javax.swing.text.html.Option;
import java.util.Optional;

public class MaintenanceServiceImp extends GenericServiceImpl<Maintenance,Long>{

    public MaintenanceServiceImp(JpaRepository<Maintenance, Long> repository) {
        super(repository);
    }

    @Override
    public boolean check(Maintenance entity, boolean isNew) {
        return Utils.isNotEmpty(entity.getIdMaintenance().toString());
    }

    @Override
    public String getEntityName() {
        return "Maintenance";
    }

    @Override
    public Long getId(Maintenance entity) {
        return entity.getIdMaintenance();
    }

    @Override
    public void setFields(Long aLong, Maintenance entity) {
        Optional<Maintenance> maintenance = repository.findById(entity.getIdMaintenance());
        if (maintenance.isPresent()) {
            entity.setIdMaintenance(maintenance.get().getIdMaintenance());
            repository.save(entity);
        }
    }
}
