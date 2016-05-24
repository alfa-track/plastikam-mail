import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.plastikam.mail.EmailApplication;
import ru.plastikam.mail.model.Client;
import ru.plastikam.mail.repository.ClientRepository;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EmailApplication.class)
public class ClientRepositoryTest {

    @Autowired
    ClientRepository repository;

    @Test
    public void whenFindClient_shouldIgnoreCase() {
        Client client = repository.findOneByEmailIgnoreCase("info@ascod.RU");
        assertThat(client, not(nullValue()));
    }

    @Test
    public void whenSearchByDomain_shouldFind() {
        int count = repository.countByEmailEndsWithIgnoreCase("yanDeX.rU");
        assertThat(count, greaterThan(10));
    }
}
