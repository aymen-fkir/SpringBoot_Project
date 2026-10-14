package tn.esprit.autoloc.autolocapi.service;

import tn.esprit.autoloc.autolocapi.repository.Agence;

import java.util.List;

public interface IAgenceService extends IGenericService<Agence, Long> {
    boolean check(Agence agence,boolean isNew);
    Agence create(Agence agence);
    Agence findById(long id);
    List<Agence> findAll();
    Agence update(Long id,Agence agence);
    void deleteByid(Long id);
    boolean hasVehicules(Long id);
}
