package tn.esprit.autoloc.autolocapi.calcul;

/**
 * Interface pour la stratégie de calcul des tarifs selon l'offre.
 */
public interface IOffreStrategy {
    VehiculeOffre getOffre();
    double calculTarif(double tarifJournalier, int nbJours);
}
