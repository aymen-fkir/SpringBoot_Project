package tn.esprit.autoloc.autolocapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tn.esprit.autoloc.autolocapi.domain.ModePaiement;
import tn.esprit.autoloc.autolocapi.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

interface basicPaiementRepository extends CrudRepository<Paiement, Long> {}

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PaiementTest {

    @Autowired private basicPaiementRepository basicPaiementRepository;
    @Autowired private IPaiementRepository fullPaiementRepository;
    @Autowired private IAgenceRepository agenceRepository;
    @Autowired private IVehiculeRepository vehiculeRepository;
    @Autowired private IClientRepository clientRepository;
    @Autowired private IReservationRepository reservationRepository;
    @Autowired private IContratRepository contratRepository;

    private Paiement addPaiement(CrudRepository<Paiement, Long> repository) {
        Agence agence = agenceRepository.save(TestData.agence());
        Vehicule vehicule = vehiculeRepository.save(TestData.vehicule(agence));
        Client client = clientRepository.save(TestData.client());
        Reservation reservation = reservationRepository.save(TestData.reservation(client, vehicule));
        Contrat contrat = contratRepository.save(TestData.contrat(reservation));

        Paiement p = new Paiement();
        p.setMontant(new BigDecimal("120.00"));
        p.setDatePaiement(LocalDate.now());
        p.setModePaiement(ModePaiement.CARTE);
        p.setContrat(contrat);
        return repository.save(p);
    }

    private void loadPaiement(CrudRepository<Paiement, Long> repository) {
        Iterable<Paiement> all = repository.findAll();
        all.forEach(p -> System.out.println(p.getIdPaiement() + " | " + p.getMontant() + " | " + p.getModePaiement()));
        assertThat(all).isNotEmpty();
    }

    @Test public void basicAddPaiement() { assertThat(addPaiement(basicPaiementRepository).getIdPaiement()).isNotNull(); }
    @Test public void fullAddPaiement()  { assertThat(addPaiement(fullPaiementRepository).getIdPaiement()).isNotNull(); }
    @Test public void basicLoadPaiement() { loadPaiement(basicPaiementRepository); }
    @Test public void fullLoadPaiement()  { loadPaiement(fullPaiementRepository); }
}