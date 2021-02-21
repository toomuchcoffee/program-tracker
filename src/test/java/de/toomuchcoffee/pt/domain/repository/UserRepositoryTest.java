package de.toomuchcoffee.pt.domain.repository;

import de.toomuchcoffee.pt.domain.entity.Role;
import de.toomuchcoffee.pt.domain.entity.User;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.DOCKER;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureEmbeddedDatabase(provider = DOCKER)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findsAll() {
        User user = new User("foo", "bar", Role.CLIENT);
        userRepository.save(user);
        List<User> figures = userRepository.findAll();
        assertThat(figures).hasSize(1);
        assertThat(figures.get(0)).isEqualToComparingFieldByField(user);
    }

    @Test
    public void saves() {
        User user = new User("foo", "bar", Role.CLIENT);
        User save = userRepository.save(user);

        Optional<User> byId = userRepository.findById(save.getUsername());
        assertThat(byId).isPresent();
    }

}