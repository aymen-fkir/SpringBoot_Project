package tn.esprit.autoloc.autolocapi.service;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.repository.Paiement;

public class PaiementServiceImp extends GenericServiceImpl<Paiement,Long>{
    public PaiementServiceImp(JpaRepository<Paiement, Long> repository) {
        super(repository);
    }

    @Override
    public Paiement create(Paiement entity) {
        throw new UnsupportedOperationException("Le paiement est en lecture seule.");
    }

    @Override
    public Paiement update(Long id, Paiement entity) {
        throw new UnsupportedOperationException("Le paiement est en lecture seule.");
    }

    @Override
    public void deleteByid(Long id) {
        throw new UnsupportedOperationException("Le paiement est en lecture seule.");
    }


    @Override
    public boolean check(Paiement entity, boolean isNew) {
        return Utils.isNotEmpty(entity.getIdPaiement().toString());
    }

    @Override
    public String getEntityName() {
        return "Paiement";
    }

    @Override
    public Long getId(Paiement entity) {
        return entity.getIdPaiement();
    }

    @Override
    public void setFields(Long aLong, Paiement entity) {
        throw new UnsupportedOperationException("Le paiement est en lecture seule.");
    }
}
