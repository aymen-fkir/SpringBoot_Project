package tn.esprit.autoloc.autolocapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import tn.esprit.autoloc.autolocapi.domain.CategorieVehicule;
import tn.esprit.autoloc.autolocapi.domain.StatutVehicule;
import tn.esprit.autoloc.autolocapi.io.ConsoleRapportDecorator;
import tn.esprit.autoloc.autolocapi.io.IRapport;
import tn.esprit.autoloc.autolocapi.io.RapportFactory;
import tn.esprit.autoloc.autolocapi.io.RapportType;
import tn.esprit.autoloc.autolocapi.repository.Agence;
import tn.esprit.autoloc.autolocapi.repository.IAgenceRepository;
import tn.esprit.autoloc.autolocapi.repository.Vehicule;
import tn.esprit.autoloc.autolocapi.service.AgenceServiceImpl;
import tn.esprit.autoloc.autolocapi.service.AgenceServiceSimulationDecorator;
import tn.esprit.autoloc.autolocapi.service.BasicAgenceServiceImpl;
import tn.esprit.autoloc.autolocapi.service.VehiculeServiceImp;
import tn.esprit.autoloc.autolocapi.service.interfaces.IGenericService;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

interface AgenceRepositoryMock extends CrudRepository<Agence, Long> {

}
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AgenceTest {

    @Autowired
    private AgenceRepositoryMock agencerepository;
    @Autowired
    private IAgenceRepository fullAgenceRepository  ;
    @Autowired
    private AgenceServiceImpl agenceService;
    @Autowired
    private BasicAgenceServiceImpl basicAgenceService;
    @Autowired
    private AgenceServiceSimulationDecorator agenceServiceSimulationDecorator;
    @Autowired
    private VehiculeServiceImp vehiculeService;


    private Agence addAgence(CrudRepository<Agence,Long> repository) {
        Agence ag = Agence.builder()
                .nom("Agence ariana")
                .ville("Tunis")
                .adresse("1 Rue Hedi")
                .telephone("71585874")
                .vehicules(new HashSet<>())
                .build();

        Vehicule vh1 = new Vehicule();
        int matricule = (int) System.currentTimeMillis();
        vh1.setImmatriculation("785414TU96"+matricule);
        vh1.setMarque("Isuzu");
        vh1.setModele("DMax");
        vh1.setCategorie(CategorieVehicule.SUV);
        vh1.setTarifJournalier(new BigDecimal("100.00"));
        vh1.setStatut(StatutVehicule.MAINTENANCE);
        vh1.setAgence(ag);
        matricule = (int) System.currentTimeMillis();
        Vehicule vh2 = new Vehicule();
        vh2.setImmatriculation("785414TU95"+matricule);
        vh2.setMarque("Toyota");
        vh2.setModele("Yaris");
        vh2.setCategorie(CategorieVehicule.UTILITAIRE);
        vh2.setTarifJournalier(new BigDecimal("80.00"));
        vh2.setStatut(StatutVehicule.DISPONIBLE);
        vh2.setAgence(ag);

        ag.getVehicules().add(vh1);
        ag.getVehicules().add(vh2);

        return repository.save(ag);
    }

    public Agence createAgence(IGenericService<Agence,Long> agenceService,Agence agence) {
        return  agenceService.create(agence);
    }

    public String loadAgence(CrudRepository<Agence,Long> repository){
        Iterable<Agence> all = repository.findAll();
        StringBuilder res = new StringBuilder();
        for (Agence ag : all) {
            // add new line
            res.append(ag.getIdAgence()).append(" | ").append(ag.getNom()).append("\n");
            Set<Vehicule> vehicules = ag.getVehicules();
            res.append("vehicluse count ").append(vehicules.size()).append("\n");
            for  (Vehicule vehicule : vehicules) {
                res.append("== ").append(vehicule.getIdVehicule()).append(" | ").append(vehicule.getImmatriculation()).append("\n");
            }
        }
        return String.valueOf(res);
    }

    private String loadSortedAgences(CrudRepository<Agence,Long> repository){
        Iterable<Agence> iterable =  repository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .sorted(Comparator.comparing(Agence::getIdAgence))
                .toString();
    }

    @Test
    public void basicLoadAgence() {
        String all = loadAgence(this.agencerepository);
        assertThat(all).isNotEmpty();
    }
    @Test
    public void fullLoadAgence() {
        String all = loadAgence(this.fullAgenceRepository);
        assertThat(all).isNotEmpty();
    }
    @Test
    public void basicAddAgence() {
        Agence saved = addAgence(this.agencerepository);
        assertThat(saved.getIdAgence()).isNotNull();
    }

    @Test
    public void fullAddAgence() {
        Agence saved = addAgence(this.fullAgenceRepository);
        assertThat(saved.getIdAgence()).isNotNull();
    }
    @Test
    public void testCreateAgence(){
        Agence ag = Agence.builder()
                // Remove .idAgence(200L) so it defaults to null for a new record
                .nom("Agence ariana")
                .ville("Tunis")
                .adresse("1 Rue Hedi")
                .telephone("71585874")
                .vehicules(new HashSet<>())
                .build();
        assertThat(this.createAgence(this.agenceService, ag)).isNotNull();
    }

    @Test
    @org.junit.jupiter.api.Test
    public void testSimulerAgence() {
        Agence saved = addAgence(this.fullAgenceRepository);
        String rapport = this.agenceService.simuler(saved, 2, VehiculeOffre.WEEKEND);
        assertThat(rapport).isNotNull();
        assertThat(rapport).contains("Id:");
        assertThat(rapport).contains(saved.getNom());
        assertThat(rapport).contains(saved.getTelephone());
        assertThat(rapport).contains("Nombre de jours: 2");
        assertThat(rapport).contains("Offre: WEEKEND");
        assertThat(rapport).contains("Total des tarifs des véhicules");
        // Vehicle 1: 100 * 0.8 * 2 = 160.0, Vehicle 2: 80 * 0.8 * 2 = 128.0 -> Total = 288.0
        assertThat(rapport).contains("288.0");
    }

    @Test
    @org.junit.jupiter.api.Test
    public void testSimulerAgenceById() {
        Agence saved = addAgence(this.fullAgenceRepository);
        Long id = saved.getId() != null ? saved.getId() : saved.getIdAgence();
        String rapport = this.agenceService.simuler(id, 3, VehiculeOffre.STANDARD);
        assertThat(rapport).isNotNull();
        assertThat(rapport).contains(String.valueOf(id));
        assertThat(rapport).contains(saved.getNom());
        assertThat(rapport).contains("Nombre de jours: 3");
        assertThat(rapport).contains("Total des tarifs des véhicules");
        // Vehicle 1: 100 * 3 = 300.0, Vehicle 2: 80 * 3 = 240.0 -> Total = 540.0
        assertThat(rapport).contains("540.0");
    }

    @Test
    @org.junit.jupiter.api.Test
    public void testBasicAgenceServiceImplThrowsException() {
        Agence saved = addAgence(this.fullAgenceRepository);
        org.junit.jupiter.api.Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> this.basicAgenceService.simuler(saved, 2, VehiculeOffre.WEEKEND)
        );
    }

    @Test
    @org.junit.jupiter.api.Test
    public void testSimulerWithFactoryConsoleTotalDecorator() {
        Agence saved = addAgence(this.fullAgenceRepository);
        // Switch report to ConsoleTotal via Factory / Decorator
        IRapport consoleRapport = RapportFactory.createRapport(RapportType.CONSOLE_TOTAL);
        this.agenceService.setRapport(consoleRapport);

        String result = this.agenceService.simuler(saved, 2, VehiculeOffre.WEEKEND);
        // Returns the total as String: 100 * 0.8 * 2 + 80 * 0.8 * 2 = 288.0
        assertThat(result).isEqualTo("288.0");

        // Reset to standard text report
        this.agenceService.setRapport(RapportFactory.createRapport(RapportType.TEXTUEL));
    }

    @Test
    @org.junit.jupiter.api.Test
    public void testAgenceServiceSimulationDecorator() {
        Agence saved = addAgence(this.fullAgenceRepository);
        String result = this.agenceServiceSimulationDecorator.simuler(saved, 2, VehiculeOffre.WEEKEND);
        // Decorator prints header to console and returns the total
        assertThat(result).isEqualTo("288.0");
    }

    @Test
    @org.junit.jupiter.api.Test
    public void testStrategyOffresViaMap() {
        Vehicule v = new Vehicule();
        v.setTarifJournalier(new BigDecimal("100.00"));

        // STANDARD: 100 * 3 = 300.0
        double tarifStandard = this.vehiculeService.calculTarif(v, 3, VehiculeOffre.STANDARD);
        assertThat(tarifStandard).isEqualTo(300.0);

        // WEEKEND: 100 * 0.8 * 2 = 160.0
        double tarifWeekend = this.vehiculeService.calculTarif(v, 2, VehiculeOffre.WEEKEND);
        assertThat(tarifWeekend).isEqualTo(160.0);

        // LONGUE_DUREE (sum <= 2000): 100 * 0.75 * 10 = 750.0
        double tarifLongueDuree = this.vehiculeService.calculTarif(v, 10, VehiculeOffre.LONGUE_DUREE);
        assertThat(tarifLongueDuree).isEqualTo(750.0);

        // LONGUE_DUREE (sum > 2000): 100 * 0.75 * 30 = 2250.0 -> 2250 - (100 * 0.05) = 2245.0
        double tarifLongueDureeBonus = this.vehiculeService.calculTarif(v, 30, VehiculeOffre.LONGUE_DUREE);
        assertThat(tarifLongueDureeBonus).isEqualTo(2245.0);
    }
}