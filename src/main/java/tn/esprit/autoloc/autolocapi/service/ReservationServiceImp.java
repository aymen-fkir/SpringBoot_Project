package tn.esprit.autoloc.autolocapi.service;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.repository.Reservation;

import java.util.Optional;

public class ReservationServiceImp extends GenericServiceImpl<Reservation, Long> {

    public ReservationServiceImp(JpaRepository<Reservation, Long> repository) {
        super(repository);
    }

    @Override
    public boolean check(Reservation entity, boolean isNew) {
        return Utils.isNotEmpty(entity.getIdReservation().toString());
    }

    @Override
    public String getEntityName() {
        return "Reservation";
    }

    @Override
    public Long getId(Reservation entity) {
        return entity.getIdReservation();
    }

    @Override
    public void setFields(Long aLong, Reservation entity) {
        Optional<Reservation> reservation = repository.findById(aLong);
        if (reservation.isPresent()) {
            entity.setIdReservation(reservation.get().getIdReservation());
            repository.save(entity);
        }
    }
}
