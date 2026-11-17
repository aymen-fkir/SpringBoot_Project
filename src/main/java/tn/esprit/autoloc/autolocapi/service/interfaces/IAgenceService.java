package tn.esprit.autoloc.autolocapi.service.interfaces;

import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import tn.esprit.autoloc.autolocapi.repository.Agence;

import java.util.List;

public interface IAgenceService extends IGenericService<Agence, Long> {
    boolean check(Agence agence,boolean isNew);
    Agence create(Agence agence);
    Agence findById(Long id);
    List<Agence> findAll();
    Agence update(Long id,Agence agence);
    void deleteByid(Long id);
    boolean hasVehicules(Long id);
    default String simuler(Agence agence, int nb_jour, VehiculeOffre offre) {
        throw new UnsupportedOperationException("La méthode simuler n'est pas supportée dans l'implémentation basique.");
    }
    default String simuler(Long id, int nb_jour, VehiculeOffre offre) {
        throw new UnsupportedOperationException("La méthode simuler n'est pas supportée dans l'implémentation basique.");
    }

    default Agence updateAgenceWithEmployes(Agence agence, List<tn.esprit.autoloc.autolocapi.repository.Employe> nouveauxEmployes) {
        throw new UnsupportedOperationException("Non supporté");
    }
    default Agence updateAgenceWithEmployes(Agence agence) {
        throw new UnsupportedOperationException("Non supporté");
    }
    default Agence updateAgenceWithEmployes(Long id, Agence agence) {
        throw new UnsupportedOperationException("Non supporté");
    }
}
