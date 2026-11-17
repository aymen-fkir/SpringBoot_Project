package tn.esprit.autoloc.autolocapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import tn.esprit.autoloc.autolocapi.io.IRapport;
import tn.esprit.autoloc.autolocapi.io.RapportFactory;
import tn.esprit.autoloc.autolocapi.io.RapportType;
import tn.esprit.autoloc.autolocapi.repository.Agence;
import tn.esprit.autoloc.autolocapi.repository.Employe;
import tn.esprit.autoloc.autolocapi.repository.IAgenceRepository;
import tn.esprit.autoloc.autolocapi.repository.Vehicule;
import tn.esprit.autoloc.autolocapi.service.interfaces.IAgenceService;
import tn.esprit.autoloc.autolocapi.service.interfaces.ISimulationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class AgenceServiceImpl extends GenericServiceImpl<Agence, Long> implements IAgenceService, ISimulationService {

    private final VehiculeServiceImp vehiculeService;
    private final EmployeServiceImp employeService;
    private IRapport rapport = RapportFactory.createRapport(RapportType.TEXTUEL);

    public AgenceServiceImpl(IAgenceRepository agenceRepository) {
        this(agenceRepository, new VehiculeServiceImp(null), null);
    }

    public AgenceServiceImpl(IAgenceRepository agenceRepository, VehiculeServiceImp vehiculeService) {
        this(agenceRepository, vehiculeService, null);
    }

    @Autowired
    public AgenceServiceImpl(
            IAgenceRepository agenceRepository,
            @Autowired(required = false) VehiculeServiceImp vehiculeService,
            @Autowired(required = false) EmployeServiceImp employeService) {
        super(agenceRepository);
        this.vehiculeService = vehiculeService != null ? vehiculeService : new VehiculeServiceImp(null);
        this.employeService = employeService;
    }

    public void setRapport(IRapport rapport) {
        this.rapport = rapport != null ? rapport : RapportFactory.createRapport(RapportType.TEXTUEL);
    }

    public IRapport getRapport() {
        return this.rapport;
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

    // 12 & 17. Mise à jour de l'agence avec ajout d'une liste de nouveaux employés
    @Override
    @Transactional
    public Agence updateAgenceWithEmployes(Agence agence, List<Employe> nouveauxEmployes) {
        // 1. Mettre à jour d’abord l’agence
        Agence savedAgence = this.repository.save(agence);

        // 2. Utiliser le service des employés pour insérer les employés
        if (nouveauxEmployes != null && !nouveauxEmployes.isEmpty()) {
            for (Employe employe : nouveauxEmployes) {
                employe.setIdEmploye(null); // S'assurer que l'ID est nul pour une création
                savedAgence.addEmploye(employe); // 10. Symétrisation relation
                if (employeService != null) {
                    employeService.create(employe); // 12. Utiliser le service des employés
                }
            }
            // 3. Mettre à jour le champ créé précédemment (effectif) en utilisant la taille de la liste des employés
            savedAgence.setEffectif(nouveauxEmployes.size());
            savedAgence = this.repository.save(savedAgence);
        }
        return savedAgence;
    }

    @Override
    @Transactional
    public Agence updateAgenceWithEmployes(Agence agence) {
        List<Employe> employesList = new ArrayList<>();
        if (agence.getEmployes() != null && !agence.getEmployes().isEmpty()) {
            employesList.addAll(agence.getEmployes());
        }
        return updateAgenceWithEmployes(agence, employesList);
    }

    @Override
    @Transactional
    public Agence updateAgenceWithEmployes(Long id, Agence agence) {
        if (id != null) {
            agence.setId(id);
            agence.setIdAgence(id);
        }
        return updateAgenceWithEmployes(agence);
    }

    public double calculerTotalTarifs(Agence agence, int nb_jour, VehiculeOffre offre) {
        double total = 0.0;
        if (agence != null && agence.getVehicules() != null) {
            for (Vehicule v : agence.getVehicules()) {
                total += vehiculeService.calculTarif(v, nb_jour, offre);
            }
        }
        return total;
    }

    @Override
    public String simuler(Agence agence, int nb_jour, VehiculeOffre offre) {
        if (agence == null) {
            throw new IllegalArgumentException("L'agence ne peut pas être nulle.");
        }
        double total = calculerTotalTarifs(agence, nb_jour, offre);
        return rapport.generer(agence, nb_jour, offre, total);
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
