package tn.esprit.autoloc.autolocapi;

import tn.esprit.autoloc.autolocapi.domain.*;
import tn.esprit.autoloc.autolocapi.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

final class TestData {
    private static final AtomicInteger SEQ = new AtomicInteger();

    private TestData() {}

    static String unique() {
        return (int) System.currentTimeMillis() + "" + SEQ.incrementAndGet();
    }

    static Agence agence() {
        return Agence.builder()
                .nom("Agence " + unique())
                .ville("Tunis")
                .adresse("1 Rue Hedi")
                .telephone("71585874")
                .vehicules(new HashSet<>())
                .build();
    }

    static Vehicule vehicule(Agence agence) {
        Vehicule v = new Vehicule();
        v.setImmatriculation("TU" + unique());
        v.setMarque("Toyota");
        v.setModele("Yaris");
        v.setCategorie(CategorieVehicule.CITADINE);
        v.setTarifJournalier(new BigDecimal("80.00"));
        v.setStatut(StatutVehicule.DISPONIBLE);
        v.setAgence(agence);
        return v;
    }

    static Client client() {
        Client c = new Client();
        c.setNom("Ben Salah");
        c.setPrenom("Ali");
        c.setEmail("ali" + unique() + "@mail.tn");
        c.setTelephone("20123456");
        c.setNumPermis("P" + unique());
        c.setDateInscription(LocalDate.now());
        return c;
    }

    static Reservation reservation(Client client, Vehicule vehicule) {
        Reservation r = new Reservation();
        r.setDateDebut(LocalDate.now());
        r.setDateFin(LocalDate.now().plusDays(3));
        r.setStatut(StatutReservation.EN_ATTENTE);
        r.setClient(client);
        r.setVehicule(vehicule);
        return r;
    }

    static Contrat contrat(Reservation reservation) {
        Contrat c = new Contrat();
        c.setDateSignature(LocalDate.now());
        c.setMontantTotal(new BigDecimal("240.00"));
        c.setValide(true);
        c.setReservation(reservation);
        return c;
    }
}