package tn.esprit.autoloc.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.Client;

import java.util.List;

@RequiredArgsConstructor
public abstract class GenericServiceImpl<T, ID> implements IGenericService<T, ID> {

    // Use JpaRepository (or a generic custom repository interface) instead of IAgenceRepository
    protected final JpaRepository<T, ID> repository;


    public abstract boolean check(T entity, boolean isNew);

    @Override
    public T create(T entity) {
        if (getId(entity) != null) {
            throw new IllegalArgumentException("id can't be null");
        }
        if (!check(entity, true)) {
            throw new IllegalStateException("Entity non valide");
        }
        return repository.save(entity);
    }


    public T findById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getEntityName(), String.valueOf(id)));
    }

    @Override
    public List<T> findAll() {
        return repository.findAll(); // Fixed: returns actual data from repository instead of empty list
    }

    public T update(ID id, T entity) {
        if (!this.check(entity, false)) {
            return null;
        }
        // Throws exception if not found
        this.findById(id);

        // Apply fields if your implementation requires it
        setFields(id, entity);

        return repository.save(entity);
    }

    public void deleteByid(ID id) {
        this.findById(id);
        repository.deleteById(id);
    }


    public boolean hasVehicules(ID id) {
        return false;
    }

    // --- Abstract methods to be implemented by child services ---

    public abstract String getEntityName();

    // Allows the generic class to check the ID without knowing the specific entity getter (e.g., getIdAgence())
    public abstract ID getId(T entity);

    public abstract void setFields(ID id, T entity);

}