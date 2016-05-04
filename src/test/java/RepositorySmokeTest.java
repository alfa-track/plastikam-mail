import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.plastikam.mail.EmailApplication;
import ru.plastikam.mail.model.EmailIn;
import ru.plastikam.mail.repository.EmailRepository;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Проверка работоспособности конфигурации БД и поверхостная проверка репозиториев
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EmailApplication.class)
public class RepositorySmokeTest {

    @Autowired
    EmailRepository userRepository;

    @Test
    public void shouldInjectRepository() {
        //Если инициализировать userRepository не получится - то до теста не дойдёт
        //Эта проверка для явности
        assertThat(userRepository, notNullValue());
    }

    @Test
    public void whenSavingUser_shouldNotFail() {
        EmailIn user = new EmailIn();
        user = userRepository.save(user);
        assertThat(user.getId(), greaterThan(0l));
    }
}
