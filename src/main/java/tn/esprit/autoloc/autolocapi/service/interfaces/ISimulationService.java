package tn.esprit.autoloc.autolocapi.service.interfaces;

import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import tn.esprit.autoloc.autolocapi.repository.Agence;

public interface ISimulationService {
    String simuler(Agence agence, int nb_jour, VehiculeOffre offre);
    String simuler(Long id, int nb_jour, VehiculeOffre offre);
}
