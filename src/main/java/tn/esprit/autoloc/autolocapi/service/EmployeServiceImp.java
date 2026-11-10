package tn.esprit.autoloc.autolocapi.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.repository.Employe;

import java.util.Optional;

@Service
public class EmployeServiceImp extends GenericServiceImpl<Employe, Long> {


    public EmployeServiceImp(JpaRepository<Employe, Long> repository) {
        super(repository);
    }

    @Override
    public boolean check(Employe entity, boolean isNew) {
        return entity != null && Utils.isNotEmpty(entity.getNom()) && Utils.isNotEmpty(entity.getPrenom());
    }

    @Override
    public String getEntityName() {
        return "Employe";
    }

    @Override
    public Long getId(Employe entity) {
        return entity.getIdEmploye();
    }

    @Override
    public void setFields(Long aLong, Employe entity) {
        Optional<Employe> employe = repository.findById(aLong);
        if (employe.isPresent()) {
            entity.setIdEmploye(employe.get().getIdEmploye());
            repository.save(entity);
        }

    }
}
