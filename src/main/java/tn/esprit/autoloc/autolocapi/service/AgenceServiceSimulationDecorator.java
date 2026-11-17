package tn.esprit.autoloc.autolocapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import tn.esprit.autoloc.autolocapi.io.ConsoleRapportDecorator;
import tn.esprit.autoloc.autolocapi.io.Decorator;
import tn.esprit.autoloc.autolocapi.io.IRapport;
import tn.esprit.autoloc.autolocapi.repository.Agence;
import tn.esprit.autoloc.autolocapi.service.interfaces.ISimulationService;

/**
 * Décorateur de service appliquant le pattern Decorator (annoté avec @Decorator)
 * pour simuler en affichant l'en-tête sur la console et en retournant le total.
 */
@Decorator
@Service("agenceServiceSimulationDecorator")
public class AgenceServiceSimulationDecorator implements ISimulationService {

    private final AgenceServiceImpl delegate;
    private final IRapport rapport;

    @Autowired
    public AgenceServiceSimulationDecorator(AgenceServiceImpl delegate) {
        this.delegate = delegate;
        this.rapport = new ConsoleRapportDecorator();
    }

    public AgenceServiceSimulationDecorator(AgenceServiceImpl delegate, IRapport rapport) {
        this.delegate = delegate;
        this.rapport = rapport != null ? rapport : new ConsoleRapportDecorator();
    }

    @Override
    public String simuler(Agence agence, int nb_jour, VehiculeOffre offre) {
        double total = delegate.calculerTotalTarifs(agence, nb_jour, offre);
        return rapport.generer(agence, nb_jour, offre, total);
    }

    @Override
    public String simuler(Long id, int nb_jour, VehiculeOffre offre) {
        Agence agence = delegate.findById(id);
        return simuler(agence, nb_jour, offre);
    }

    public AgenceServiceImpl getDelegate() {
        return delegate;
    }
}
