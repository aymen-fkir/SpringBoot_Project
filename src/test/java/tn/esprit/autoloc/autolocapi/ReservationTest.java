package tn.esprit.autoloc.autolocapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tn.esprit.autoloc.autolocapi.repository.*;

import static org.assertj.core.api.Assertions.assertThat;

interface basicReservationRepository extends CrudRepository<Reservation, Long> {}

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ReservationTest {

    @Autowired private basicReservationRepository basicReservationRepository;
    @Autowired private IReservationRepository fullReservationRepository;
    @Autowired private IAgenceRepository agenceRepository;
    @Autowired private IVehiculeRepository vehiculeRepository;
    @Autowired private IClientRepository clientRepository;

    private Reservation addReservation(CrudRepository<Reservation, Long> repository) {
        Agence agence = agenceRepository.save(TestData.agence());
        Vehicule vehicule = vehiculeRepository.save(TestData.vehicule(agence));
        Client client = clientRepository.save(TestData.client());
        return repository.save(TestData.reservation(client, vehicule));
    }

    private void loadReservation(CrudRepository<Reservation, Long> repository) {
        Iterable<Reservation> all = repository.findAll();
        all.forEach(r -> System.out.println(r.getIdReservation() + " | " + r.getDateDebut()
                + " -> " + r.getDateFin() + " | " + r.getStatut()));
        assertThat(all).isNotEmpty();
    }

    @Test public void basicAddReservation() { assertThat(addReservation(basicReservationRepository).getIdReservation()).isNotNull(); }
    @Test public void fullAddReservation()  { assertThat(addReservation(fullReservationRepository).getIdReservation()).isNotNull(); }
    @Test public void basicLoadReservation() { loadReservation(basicReservationRepository); }
    @Test public void fullLoadReservation()  { loadReservation(fullReservationRepository); }
}