package tn.esprit.autoloc.autolocapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.*;
import tn.esprit.autoloc.autolocapi.service.EquipementServiceImp;
import tn.esprit.autoloc.autolocapi.service.VehiculeServiceImp;
import tn.esprit.autoloc.autolocapi.web.controller.VehiculeController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

interface basicVehiculeRepository extends CrudRepository<Vehicule, Long> {}

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class VehiculeTest {

    @Autowired private basicVehiculeRepository basicVehiculeRepository;
    @Autowired private IVehiculeRepository fullVehiculeRepository;
    @Autowired private IAgenceRepository agenceRepository;
    @Autowired private IEquipementRepository equipementRepository;
    @Autowired private VehiculeServiceImp vehiculeService;
    @Autowired private EquipementServiceImp equipementService;
    @Autowired private VehiculeController vehiculeController;

    private Vehicule addVehicule(CrudRepository<Vehicule, Long> repository) {
        Agence agence = agenceRepository.save(TestData.agence());
        return repository.save(TestData.vehicule(agence));
    }

    private void loadVehicule(CrudRepository<Vehicule, Long> repository) {
        Iterable<Vehicule> all = repository.findAll();
        all.forEach(v -> System.out.println(v.getIdVehicule() + " | " + v.getImmatriculation() + " | " + v.getStatut()));
        assertThat(all).isNotEmpty();
    }

    @Test public void basicAddVehicule() { assertThat(addVehicule(basicVehiculeRepository).getIdVehicule()).isNotNull(); }
    @Test public void fullAddVehicule()  { assertThat(addVehicule(fullVehiculeRepository).getIdVehicule()).isNotNull(); }
    @Test public void basicLoadVehicule() { loadVehicule(basicVehiculeRepository); }
    @Test public void fullLoadVehicule()  { loadVehicule(fullVehiculeRepository); }

    // 3. Tester le service et vérifier que le véhicule existe encore mais sans parent
    @Test
    @org.junit.jupiter.api.Test
    public void testDesaffecterVehiculeExisteSansParent() {
        Vehicule v = addVehicule(fullVehiculeRepository);
        assertThat(v.getAgence()).isNotNull();

        Vehicule desaffecte = vehiculeService.desaffecter(v.getIdVehicule());
        assertThat(desaffecte).isNotNull();
        assertThat(desaffecte.getAgence()).isNull();

        Vehicule reloaded = vehiculeService.findById(v.getIdVehicule());
        assertThat(reloaded).isNotNull();
        assertThat(reloaded.getAgence()).isNull();
    }

    // 5. Tester l’affectation en utilisant une API qui accepte les deux identifiants dans l’URL
    @Test
    @org.junit.jupiter.api.Test
    public void testAffecterAgenceViaApi() throws Exception {
        Vehicule v = addVehicule(fullVehiculeRepository);
        // Désaffecter d'abord pour tester une nouvelle affectation
        vehiculeService.desaffecter(v.getIdVehicule());

        Agence nouvelleAgence = agenceRepository.save(TestData.agence());

        // Test via MockMvc avec les deux identifiants dans l'URL
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(vehiculeController).build();
        mockMvc.perform(put("/vehicules/" + v.getIdVehicule() + "/agence/" + nouvelleAgence.getIdAgence()))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        Vehicule reloaded = vehiculeService.findById(v.getIdVehicule());
        assertThat(reloaded.getAgence()).isNotNull();
        assertThat(reloaded.getAgence().getIdAgence()).isEqualTo(nouvelleAgence.getIdAgence());

        // Test également via méthode du contrôleur
        Agence autreAgence = agenceRepository.save(TestData.agence());
        Vehicule resultController = vehiculeController.affecterAgence(v.getIdVehicule(), autreAgence.getIdAgence());
        assertThat(resultController).isNotNull();
        assertThat(resultController.getAgence()).isNotNull();
        assertThat(resultController.getAgence().getIdAgence()).isEqualTo(autreAgence.getIdAgence());
    }

    // 7. Tester et vérifier que l’équipement a été ajouté au véhicule
    @Test
    @org.junit.jupiter.api.Test
    public void testAffecterEquipementEtVerifierAjout() {
        Vehicule v = addVehicule(fullVehiculeRepository);

        Equipement eq = new Equipement();
        eq.setLibelle("GPS Professionnel " + TestData.unique());
        eq = equipementRepository.save(eq);

        Vehicule updated = vehiculeService.affecterEquipement(v.getIdVehicule(), eq.getIdEquipement());
        assertThat(updated).isNotNull();
        assertThat(updated.getEquipements()).isNotEmpty();
        assertThat(updated.getEquipements())
                .extracting(Equipement::getIdEquipement)
                .contains(eq.getIdEquipement());

        Vehicule reloaded = vehiculeService.findById(v.getIdVehicule());
        assertThat(reloaded.getEquipements())
                .extracting(Equipement::getIdEquipement)
                .contains(eq.getIdEquipement());
    }

    // 8. Supprimer le véhicule et vérifier qu'il y a une exception
    @Test
    @org.junit.jupiter.api.Test
    public void testSupprimerVehiculeAvecEquipementThrowsException() {
        Vehicule v = addVehicule(fullVehiculeRepository);

        Equipement eq = new Equipement();
        eq.setLibelle("Siège Bébé " + TestData.unique());
        eq = equipementRepository.save(eq);

        vehiculeService.affecterEquipement(v.getIdVehicule(), eq.getIdEquipement());

        // La suppression doit lever une exception car le véhicule possède un équipement
        assertThrows(Exception.class, () -> {
            vehiculeService.deleteByid(v.getIdVehicule());
        });

        // Vérifier que le véhicule existe toujours dans la base
        assertThat(vehiculeService.findById(v.getIdVehicule())).isNotNull();
    }

    // 9. Supprimer l'équipement et vérifier qu'il n'y a aucun problème
    @Test
    @org.junit.jupiter.api.Test
    public void testSupprimerEquipementSansProbleme() {
        Vehicule v = addVehicule(fullVehiculeRepository);

        Equipement eqInit = new Equipement();
        eqInit.setLibelle("Barres de Toit " + TestData.unique());
        final Equipement eq = equipementRepository.save(eqInit);
        final Long eqId = eq.getIdEquipement();

        vehiculeService.affecterEquipement(v.getIdVehicule(), eqId);

        // Suppression de l'équipement sans aucun problème
        assertDoesNotThrow(() -> {
            equipementService.deleteByid(eqId);
        });

        // Vérifier que l'équipement a bien été supprimé
        assertThrows(ResourceNotFoundException.class, () -> {
            equipementService.findById(eqId);
        });

        // Vérifier que le véhicule existe toujours
        Vehicule vApres = vehiculeService.findById(v.getIdVehicule());
        assertThat(vApres).isNotNull();
    }
}
