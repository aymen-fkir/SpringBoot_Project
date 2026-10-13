package tn.esprit.autoloc.autolocapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tn.esprit.autoloc.autolocapi.repository.*;

import static org.assertj.core.api.Assertions.assertThat;

interface basicContratRepository extends CrudRepository<Contrat, Long> {}

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ContratTest {

    @Autowired private basicContratRepository basicContratRepository;
    @Autowired private IContratRepository fullContratRepository;
    @Autowired private IAgenceRepository agenceRepository;
    @Autowired private IVehiculeRepository vehiculeRepository;
    @Autowired private IClientRepository clientRepository;
    @Autowired private IReservationRepository reservationRepository;

    private Contrat addContrat(CrudRepository<Contrat, Long> repository) {
        Agence agence = agenceRepository.save(TestData.agence());
        Vehicule vehicule = vehiculeRepository.save(TestData.vehicule(agence));
        Client client = clientRepository.save(TestData.client());
        Reservation reservation = reservationRepository.save(TestData.reservation(client, vehicule));
        return repository.save(TestData.contrat(reservation));
    }

    private void loadContrat(CrudRepository<Contrat, Long> repository) {
        Iterable<Contrat> all = repository.findAll();
        all.forEach(c -> System.out.println(c.getIdContrat() + " | " + c.getDateSignature()
                + " | " + c.getMontantTotal() + " | valide=" + c.isValide()));
        assertThat(all).isNotEmpty();
    }

    @Test public void basicAddContrat() { assertThat(addContrat(basicContratRepository).getIdContrat()).isNotNull(); }
    @Test public void fullAddContrat()  { assertThat(addContrat(fullContratRepository).getIdContrat()).isNotNull(); }
    @Test public void basicLoadContrat() { loadContrat(basicContratRepository); }
    @Test public void fullLoadContrat()  { loadContrat(fullContratRepository); }
}