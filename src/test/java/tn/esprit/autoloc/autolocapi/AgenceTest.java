package tn.esprit.autoloc.autolocapi;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tn.esprit.autoloc.autolocapi.domain.CategorieVehicule;
import tn.esprit.autoloc.autolocapi.domain.StatutVehicule;
import tn.esprit.autoloc.autolocapi.repository.Agence;
import tn.esprit.autoloc.autolocapi.repository.Vehicule;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

interface AgenceRepository extends CrudRepository<Agence, Long> {

}
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AgenceTest {

    @Autowired
    private AgenceRepository agencerepository;

    @Test
    public void addAgence() {
        Agence ag = Agence.builder()
                .nom("Agence ariana")
                .ville("Tunis")
                .adresse("1 Rue Hedi")
                .telephone("71585874")
                .vehicules(new HashSet<>())
                .build();

        Vehicule vh1 = new Vehicule();
        vh1.setImmatriculation("785414TU96");
        vh1.setMarque("Isuzu");
        vh1.setModele("DMax");
        vh1.setCategorie(CategorieVehicule.SUV);
        vh1.setTarifJournalier(new BigDecimal("100.00"));
        vh1.setStatut(StatutVehicule.MAINTENANCE);
        vh1.setAgence(ag);

        Vehicule vh2 = new Vehicule();
        vh2.setImmatriculation("785414TU95");
        vh2.setMarque("Toyota");
        vh2.setModele("Yaris");
        vh2.setCategorie(CategorieVehicule.UTILITAIRE);
        vh2.setTarifJournalier(new BigDecimal("80.00"));
        vh2.setStatut(StatutVehicule.DISPONIBLE);
        vh2.setAgence(ag);

        ag.getVehicules().add(vh1);
        ag.getVehicules().add(vh2);

        Agence savedAgence = agencerepository.save(ag);

        assertThat(savedAgence.getIdAgence()).isNotNull();
        assertThat(savedAgence.getVehicules()).hasSize(2);
    }
    @Test
    public void loadAgence(){
        Iterable<Agence> all = agencerepository.findAll();
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
        Assertions.fail(String.valueOf(res));
    }
}