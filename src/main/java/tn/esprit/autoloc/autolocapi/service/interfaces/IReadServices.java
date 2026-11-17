package tn.esprit.autoloc.autolocapi.service.interfaces;

import java.util.List;

public interface IReadServices <T,ID>{
    T findById(ID id);
    List<T> findAll();
}
