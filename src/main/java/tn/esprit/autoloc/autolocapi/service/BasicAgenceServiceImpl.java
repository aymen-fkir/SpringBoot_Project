package tn.esprit.autoloc.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.Agence;
import tn.esprit.autoloc.autolocapi.repository.IAgenceRepository;
import tn.esprit.autoloc.autolocapi.repository.Vehicule;
import tn.esprit.autoloc.autolocapi.service.interfaces.IAgenceService;

import java.util.List;

@Service
public class BasicAgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;
    private final VehiculeServiceImp vehiculeService;

    public BasicAgenceServiceImpl(IAgenceRepository agenceRepository) {
        this.agenceRepository = agenceRepository;
        this.vehiculeService = new VehiculeServiceImp(null);
    }

    @Autowired
    public BasicAgenceServiceImpl(IAgenceRepository agenceRepository, @Autowired(required = false) VehiculeServiceImp vehiculeService) {
        this.agenceRepository = agenceRepository;
        this.vehiculeService = vehiculeService != null ? vehiculeService : new VehiculeServiceImp(null);
    }

    @Override
    public boolean check(Agence agence, boolean isNew) {
        return Utils.isNotEmpty(agence.getNom()) && Utils.isNotEmpty(agence.getTelephone());
    }

    @Override
    public Agence create(Agence agence) {
        if (!check(agence, true)) {
            throw new IllegalArgumentException("id can't be null");
        }
        if (!check(agence, true)) {
            throw new IllegalStateException("Agence non valide");
        }
        return agenceRepository.save(agence);
    }

    @Override
    public Agence findById(Long id) {
        return agenceRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Agence" , Long.toString(id)));
    }

    @Override
    public List<Agence> findAll() {
        return agenceRepository.findAll();
    }

    @Override
    public Agence update(Long id, Agence agence) {
        if (!this.check(agence, false)) {
            return null;
        }
        this.findById(id);
        return agenceRepository.save(agence);
    }

    @Override
    public void deleteByid(Long id) {
        this.findById(id);
        agenceRepository.deleteById(id);
    }

    @Override
    public boolean hasVehicules(Long id) {
        return false;
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
}
