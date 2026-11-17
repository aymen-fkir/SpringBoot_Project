package tn.esprit.autoloc.autolocapi.service.interfaces;

public interface IBusiness<T,ID> {
    boolean check(T entity, boolean isNew);
    boolean hasVehicules(Long id);
}
