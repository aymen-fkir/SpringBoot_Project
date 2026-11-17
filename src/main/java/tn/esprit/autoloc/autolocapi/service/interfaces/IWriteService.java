package tn.esprit.autoloc.autolocapi.service.interfaces;

public interface IWriteService <T,ID> {
    T create(T entity);
    T update(Long id, T entity);
    void deleteByid(Long id);
}
