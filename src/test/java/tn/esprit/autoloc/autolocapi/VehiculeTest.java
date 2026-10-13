package tn.esprit.autoloc.autolocapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tn.esprit.autoloc.autolocapi.repository.*;

import static org.assertj.core.api.Assertions.assertThat;

interface basicVehiculeRepository extends CrudRepository<Vehicule, Long> {}

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class VehiculeTest {

    @Autowired private basicVehiculeRepository basicVehiculeRepository;
    @Autowired private IVehiculeRepository fullVehiculeRepository;
    @Autowired private IAgenceRepository agenceRepository;

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
}