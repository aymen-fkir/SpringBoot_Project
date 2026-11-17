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
    String simuler(Agence agence, int nb_jour, VehiculeOffre offre);
    String simuler(Long id, int nb_jour, VehiculeOffre offre);
}
