package tn.esprit.autoloc.autolocapi.io;

import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import tn.esprit.autoloc.autolocapi.repository.Agence;
import tn.esprit.autoloc.autolocapi.repository.Vehicule;

public class RapportTextuel implements IRapport {

    @Override
    public String generer(Agence agence, int nb_jour, VehiculeOffre offre, double total) {
        Long agenceId = agence.getId() != null ? agence.getId() : agence.getIdAgence();
        String nom = agence.getNom() != null ? agence.getNom() : "";
        String telephone = agence.getTelephone() != null ? agence.getTelephone() : "";

        StringBuilder sb = new StringBuilder();
        sb.append("=== Rapport de Simulation Agence ===\n");
        sb.append("En-tête:\n");
        sb.append("Id: ").append(agenceId != null ? agenceId : "N/A").append("\n");
        sb.append("Nom: ").append(nom).append("\n");
        sb.append("Numéro de tel: ").append(telephone).append("\n");
        sb.append("------------------------------------\n");
        sb.append("Paramètres de simulation:\n");
        sb.append("Nombre de jours: ").append(nb_jour).append("\n");
        sb.append("Offre: ").append(offre != null ? offre.name() : "STANDARD").append("\n");
        sb.append("------------------------------------\n");
        sb.append("Détail des véhicules:\n");

        if (agence.getVehicules() != null && !agence.getVehicules().isEmpty()) {
            for (Vehicule v : agence.getVehicules()) {
                sb.append("- ")
                  .append(v.getImmatriculation() != null ? v.getImmatriculation() : ("Véhicule #" + v.getIdVehicule()))
                  .append(" (").append(v.getMarque() != null ? v.getMarque() : "")
                  .append(" ").append(v.getModele() != null ? v.getModele() : "").append(")\n");
            }
        } else {
            sb.append("Aucun véhicule associé à cette agence.\n");
        }

        sb.append("------------------------------------\n");
        sb.append("Total des tarifs des véhicules: ").append(total).append(" DT\n");
        sb.append("====================================");

        return sb.toString();
    }
}
