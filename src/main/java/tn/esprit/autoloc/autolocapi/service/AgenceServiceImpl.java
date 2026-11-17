package tn.esprit.autoloc.autolocapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import tn.esprit.autoloc.autolocapi.repository.Agence;
import tn.esprit.autoloc.autolocapi.repository.IAgenceRepository;
import tn.esprit.autoloc.autolocapi.repository.Vehicule;
import tn.esprit.autoloc.autolocapi.service.interfaces.IAgenceService;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class AgenceServiceImpl extends GenericServiceImpl<Agence, Long> implements IAgenceService {

    private final VehiculeServiceImp vehiculeService;

    public AgenceServiceImpl(IAgenceRepository agenceRepository) {
        this(agenceRepository, new VehiculeServiceImp(null));
    }

    @Autowired
    public AgenceServiceImpl(IAgenceRepository agenceRepository, @Autowired(required = false) VehiculeServiceImp vehiculeService) {
        super(agenceRepository);
        this.vehiculeService = vehiculeService != null ? vehiculeService : new VehiculeServiceImp(null);
    }

    @Override
    public boolean check(Agence entity, boolean isNew) {
        return Utils.isNotEmpty(entity.getNom()) && Utils.isNotEmpty(entity.getTelephone());
    }

    @Override
    public String getEntityName() {
        return "Agence";
    }

    @Override
    public Long getId(Agence entity) {
        return entity.getId();
    }

    @Override
    public void setFields(Long id, Agence agence) {
        Optional<Agence> agence1 = this.repository.findById(id);
        if (agence1.isPresent()) {
            this.repository.save(agence);
        }
    }

    @Override
    public String simuler(Agence agence, int nb_jour, VehiculeOffre offre) {
        if (agence == null) {
            throw new IllegalArgumentException("L'agence ne peut pas être nulle.");
        }

        Long agenceId = agence.getId() != null ? agence.getId() : agence.getIdAgence();
        String nom = agence.getNom() != null ? agence.getNom() : "";
        String telephone = agence.getTelephone() != null ? agence.getTelephone() : "";

        double total = 0.0;
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
                double tarif = vehiculeService.calculTarif(v, nb_jour, offre);
                total += tarif;
                sb.append("- ")
                  .append(v.getImmatriculation() != null ? v.getImmatriculation() : ("Véhicule #" + v.getIdVehicule()))
                  .append(" (").append(v.getMarque() != null ? v.getMarque() : "")
                  .append(" ").append(v.getModele() != null ? v.getModele() : "").append(")")
                  .append(" : ").append(tarif).append(" DT\n");
            }
        } else {
            sb.append("Aucun véhicule associé à cette agence.\n");
        }

        sb.append("------------------------------------\n");
        sb.append("Total des tarifs des véhicules: ").append(total).append(" DT\n");
        sb.append("====================================");

        return sb.toString();
    }

    @Override
    public String simuler(Long id, int nb_jour, VehiculeOffre offre) {
        if (id == null) {
            throw new IllegalArgumentException("L'identifiant de l'agence ne peut pas être nul.");
        }
        Agence agence = findById(id);
        return simuler(agence, nb_jour, offre);
    }

    public String simuler(int nb_jour, VehiculeOffre offre, Agence agence) {
        return simuler(agence, nb_jour, offre);
    }

    public String simuler(int nb_jour, VehiculeOffre offre, Long id) {
        return simuler(id, nb_jour, offre);
    }

    public String simuler(int nb_jour, VehiculeOffre offre) {
        List<Agence> agences = findAll();
        StringBuilder sb = new StringBuilder();
        for (Agence a : agences) {
            sb.append(simuler(a, nb_jour, offre)).append("\n\n");
        }
        return sb.toString();
    }
}
