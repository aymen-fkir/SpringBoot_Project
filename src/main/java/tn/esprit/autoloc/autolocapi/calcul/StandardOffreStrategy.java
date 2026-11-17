package tn.esprit.autoloc.autolocapi.calcul;

import org.springframework.stereotype.Component;

@Component("STANDARD")
public class StandardOffreStrategy implements IOffreStrategy {

    @Override
    public VehiculeOffre getOffre() {
        return VehiculeOffre.STANDARD;
    }

    @Override
    public double calculTarif(double tarifJournalier, int nbJours) {
        return tarifJournalier * nbJours;
    }
}
