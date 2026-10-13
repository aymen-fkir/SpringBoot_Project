package tn.esprit.autoloc.autolocapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tn.esprit.autoloc.autolocapi.repository.*;

import static org.assertj.core.api.Assertions.assertThat;

interface basicEquipementRepository extends CrudRepository<Equipement, Long> {}

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EquipementTest {

    @Autowired private basicEquipementRepository basicEquipementRepository;
    @Autowired private IEquipementRepository fullEquipementRepository;

    private Equipement addEquipement(CrudRepository<Equipement, Long> repository) {
        Equipement e = new Equipement();
        e.setLibelle("GPS " + TestData.unique());
        return repository.save(e);
    }

    private void loadEquipement(CrudRepository<Equipement, Long> repository) {
        Iterable<Equipement> all = repository.findAll();
        all.forEach(e -> System.out.println(e.getIdEquipement() + " | " + e.getLibelle()));
        assertThat(all).isNotEmpty();
    }

    @Test public void basicAddEquipement() { assertThat(addEquipement(basicEquipementRepository).getIdEquipement()).isNotNull(); }
    @Test public void fullAddEquipement()  { assertThat(addEquipement(fullEquipementRepository).getIdEquipement()).isNotNull(); }
    @Test public void basicLoadEquipement() { loadEquipement(basicEquipementRepository); }
    @Test public void fullLoadEquipement()  { loadEquipement(fullEquipementRepository); }
}