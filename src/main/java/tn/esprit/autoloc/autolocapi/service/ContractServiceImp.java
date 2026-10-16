package tn.esprit.autoloc.autolocapi.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.repository.Contrat;

import java.util.Optional;

@Service
public class ContractServiceImp extends GenericServiceImpl<Contrat,Long>{
    public ContractServiceImp(JpaRepository<Contrat, Long> repository) {
        super(repository);
    }

    @Override
    public boolean check(Contrat entity, boolean isNew) {
        return Utils.isNotEmpty(entity.getIdContrat().toString());
    }

    @Override
    public String getEntityName() {
        return "Contrat";
    }

    @Override
    public Long getId(Contrat entity) {
        return entity.getIdContrat();
    }

    @Override
    public void setFields(Long id, Contrat entity) {
        Optional<Contrat> contrat = this.repository.findById(id);
        if (contrat.isPresent()) {
            this.repository.save(entity);
        }

    }
}
