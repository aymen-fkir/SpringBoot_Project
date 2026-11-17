package tn.esprit.autoloc.autolocapi.io;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import tn.esprit.autoloc.autolocapi.repository.Agence;

/**
 * Décorateur de rapport qui affiche l'en-tête sur la console et retourne le total des tarifs.
 */
@Decorator
@Component
public class ConsoleRapportDecorator implements IRapport {

    private final IRapport wrappedRapport;

    public ConsoleRapportDecorator() {
        this(new RapportTextuel());
    }

    public ConsoleRapportDecorator(IRapport wrappedRapport) {
        this.wrappedRapport = wrappedRapport != null ? wrappedRapport : new RapportTextuel();
    }

    @Override
    public String generer(Agence agence, int nb_jour, VehiculeOffre offre, double total) {
        if (agence == null) {
            throw new IllegalArgumentException("L'agence ne peut pas être nulle.");
        }

        Long agenceId = agence.getId() != null ? agence.getId() : agence.getIdAgence();
        String nom = agence.getNom() != null ? agence.getNom() : "";
        String telephone = agence.getTelephone() != null ? agence.getTelephone() : "";

        // Affiche l'en-tête sur la console
        System.out.println("=== En-tête Agence ===");
        System.out.println("Id : " + (agenceId != null ? agenceId : "N/A"));
        System.out.println("Nom : " + nom);
        System.out.println("Numéro de tel : " + telephone);

        // Retourne le total sous forme textuelle (signature préservée)
        return String.valueOf(total);
    }

    public IRapport getWrappedRapport() {
        return wrappedRapport;
    }
}
