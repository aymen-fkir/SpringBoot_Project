package tn.esprit.autoloc.autolocapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tn.esprit.autoloc.autolocapi.repository.*;

import static org.assertj.core.api.Assertions.assertThat;

interface basicClientRepository extends CrudRepository<Client, Long> {}

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ClientTest {

    @Autowired private basicClientRepository basicClientRepository;
    @Autowired private IClientRepository fullClientRepository;

    private Client addClient(CrudRepository<Client, Long> repository) {
        return repository.save(TestData.client());
    }

    private void loadClient(CrudRepository<Client, Long> repository) {
        Iterable<Client> all = repository.findAll();
        all.forEach(c -> System.out.println(c.getIdClient() + " | " + c.getNom() + " | " + c.getEmail()));
        assertThat(all).isNotEmpty();
    }

    @Test public void basicAddClient() { assertThat(addClient(basicClientRepository).getIdClient()).isNotNull(); }
    @Test public void fullAddClient()  { assertThat(addClient(fullClientRepository).getIdClient()).isNotNull(); }
    @Test public void basicLoadClient() { loadClient(basicClientRepository); }
    @Test public void fullLoadClient()  { loadClient(fullClientRepository); }
}