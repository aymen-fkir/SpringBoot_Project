package tn.esprit.autoloc.autolocapi.calcul;

import org.springframework.stereotype.Component;

@Component("LONGUE_DUREE")
public class LongueDureeOffreStrategy implements IOffreStrategy {

    @Override
    public VehiculeOffre getOffre() {
        return VehiculeOffre.LONGUE_DUREE;
    }

    @Override
    public double calculTarif(double tarifJournalier, int nbJours) {
        double sum = (tarifJournalier * 0.75) * nbJours;
        if (sum > 2000) {
            return sum - (tarifJournalier * 0.05);
        }
        return sum;
    }
}
