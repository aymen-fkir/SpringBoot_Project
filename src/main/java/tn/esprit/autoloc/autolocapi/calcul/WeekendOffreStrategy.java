package tn.esprit.autoloc.autolocapi.calcul;

import org.springframework.stereotype.Component;

@Component("WEEKEND")
public class WeekendOffreStrategy implements IOffreStrategy {

    @Override
    public VehiculeOffre getOffre() {
        return VehiculeOffre.WEEKEND;
    }

    @Override
    public double calculTarif(double tarifJournalier, int nbJours) {
        return (tarifJournalier * 0.8) * nbJours;
    }
}
