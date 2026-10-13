package tn.esprit.autoloc.autolocapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tn.esprit.autoloc.autolocapi.domain.RoleEmploye;
import tn.esprit.autoloc.autolocapi.repository.*;

import static org.assertj.core.api.Assertions.assertThat;

interface basicEmployeRepository extends CrudRepository<Employe, Long> {}

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EmployeTest {

    @Autowired private basicEmployeRepository basicEmployeRepository;
    @Autowired private IEmployeRepository fullEmployeRepository;
    @Autowired private IAgenceRepository agenceRepository;

    private Employe addEmploye(CrudRepository<Employe, Long> repository) {
        Agence agence = agenceRepository.save(TestData.agence());

        Employe e = new Employe();
        e.setNom("Trabelsi " + TestData.unique());
        e.setPrenom("Sami");
        e.setRole(RoleEmploye.AGENT);
        e.setAgence(agence);
        return repository.save(e);
    }

    private void loadEmploye(CrudRepository<Employe, Long> repository) {
        Iterable<Employe> all = repository.findAll();
        all.forEach(e -> System.out.println(e.getIdEmploye() + " | " + e.getNom() + " | " + e.getRole()));
        assertThat(all).isNotEmpty();
    }

    @Test public void basicAddEmploye() { assertThat(addEmploye(basicEmployeRepository).getIdEmploye()).isNotNull(); }
    @Test public void fullAddEmploye()  { assertThat(addEmploye(fullEmployeRepository).getIdEmploye()).isNotNull(); }
    @Test public void basicLoadEmploye() { loadEmploye(basicEmployeRepository); }
    @Test public void fullLoadEmploye()  { loadEmploye(fullEmployeRepository); }
}