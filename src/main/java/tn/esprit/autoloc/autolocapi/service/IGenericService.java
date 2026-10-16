package tn.esprit.autoloc.autolocapi.service;

import tn.esprit.autoloc.autolocapi.repository.Agence;

import java.util.List;

public interface IGenericService <T,ID>{
    boolean check(T entity, boolean isNew);
    T create(T entity);
    T findById(ID id);
    List<T> findAll();
    T update(Long id,T entity);
    void deleteByid(Long id);
    boolean hasVehicules(Long id);
}
