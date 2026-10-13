package tn.esprit.autoloc.autolocapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tn.esprit.autoloc.autolocapi.repository.*;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

interface basicMaintenanceRepository extends CrudRepository<Maintenance, Long> {}

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MaintenanceTest {

    @Autowired private basicMaintenanceRepository basicMaintenanceRepository;
    @Autowired private IMaintenanceRepository fullMaintenanceRepository;
    @Autowired private IAgenceRepository agenceRepository;
    @Autowired private IVehiculeRepository vehiculeRepository;

    private Maintenance addMaintenance(CrudRepository<Maintenance, Long> repository) {
        Agence agence = agenceRepository.save(TestData.agence());
        Vehicule vehicule = vehiculeRepository.save(TestData.vehicule(agence));

        Maintenance m = new Maintenance();
        m.setDateDebut(LocalDate.now());
        m.setDateFin(LocalDate.now().plusDays(2));
        m.setDescription("Vidange " + TestData.unique());
        m.setVehicule(vehicule);
        return repository.save(m);
    }

    private void loadMaintenance(CrudRepository<Maintenance, Long> repository) {
        Iterable<Maintenance> all = repository.findAll();
        all.forEach(m -> System.out.println(m.getIdMaintenance() + " | " + m.getDescription()));
        assertThat(all).isNotEmpty();
    }

    @Test public void basicAddMaintenance() { assertThat(addMaintenance(basicMaintenanceRepository).getIdMaintenance()).isNotNull(); }
    @Test public void fullAddMaintenance()  { assertThat(addMaintenance(fullMaintenanceRepository).getIdMaintenance()).isNotNull(); }
    @Test public void basicLoadMaintenance() { loadMaintenance(basicMaintenanceRepository); }
    @Test public void fullLoadMaintenance()  { loadMaintenance(fullMaintenanceRepository); }
}