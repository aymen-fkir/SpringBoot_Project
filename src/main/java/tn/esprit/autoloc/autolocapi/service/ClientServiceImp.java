package tn.esprit.autoloc.autolocapi.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.repository.Agence;
import tn.esprit.autoloc.autolocapi.repository.Client;

import javax.swing.text.html.parser.Entity;
import java.util.Optional;

@Service
public class ClientServiceImp extends GenericServiceImpl<Client,Long>{
    public ClientServiceImp(JpaRepository<Client, Long> repository) {
        super(repository);
    }
    @Override
    public boolean check(Client entity, boolean isNew) {
        return Utils.isNotEmpty(entity.getNom()) && Utils.isNotEmpty(entity.getPrenom());
    }

    @Override
    public String getEntityName() {
        return "Client";
    }

    @Override
    public Long getId(Client entity) {
        return entity.getIdClient();
    }

    @Override
    public void setFields(Long id, Client entity) {
        Optional<Client> client = this.repository.findById(id);
        if (client.isPresent()) {
            this.repository.save(entity);
        }

    }
}
