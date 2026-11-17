package tn.esprit.autoloc.autolocapi.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import tn.esprit.autoloc.autolocapi.repository.Vehicule;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class VehiculeServiceImp extends GenericServiceImpl<Vehicule,Long>{
    public VehiculeServiceImp(JpaRepository<Vehicule, Long> repository) {
        super(repository);
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
    public double calculTarif(Vehicule entity,int nb_jour, VehiculeOffre offre ){
        double tarif = entity.getTarifJournalier().doubleValue();
        if (offre == null){
            return tarif*nb_jour;
        }
        switch (offre) {
            case WEEKEND -> {
                return (tarif * 0.8) * (nb_jour);
            }
            case LONGUE_DUREE -> {
                double sum = (tarif * 0.75) * (nb_jour);
                if (sum> 2000){
                    return sum - (tarif * 0.05);
                }
                return sum;
            }
            default -> {
                return tarif * nb_jour;
            }
        }

    }
}
