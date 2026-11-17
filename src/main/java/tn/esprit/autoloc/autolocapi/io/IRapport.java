package tn.esprit.autoloc.autolocapi.io;

import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import tn.esprit.autoloc.autolocapi.repository.Agence;

public interface IRapport {
    String generer(Agence agence, int nb_jour, VehiculeOffre offre, double total);
}
