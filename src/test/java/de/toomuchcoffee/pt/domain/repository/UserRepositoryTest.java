package de.toomuchcoffee.pt.domain.repository;

import de.toomuchcoffee.pt.domain.entity.Client;
import de.toomuchcoffee.pt.domain.entity.Coach;
import de.toomuchcoffee.pt.domain.entity.User;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.Before;
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

    @Before
    public void setUp() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    public void findsAll() {
        User user = Client.builder()
                .username("foo")
                .password("bar")
                .build();
        userRepository.save(user);
        List<User> figures = userRepository.findAll();
        assertThat(figures).hasSize(1);
        assertThat(figures.get(0)).isEqualToComparingFieldByField(user);
    }

    @Test
    public void saveClient() {
        Client client = Client.builder()
                .username("foo")
                .password("bar")
                .fullName("foobar")
                .build();
        User save = userRepository.save(client);

        Optional<User> byId = userRepository.findById(save.getUsername());
        assertThat(byId).isPresent();
        assertThat(byId.get()).isInstanceOf(Client.class);
    }

    @Test
    public void saveCoach() {
        Coach coach = Coach.builder()
                .username("foo")
                .password("bar")
                .fullName("foobar")
                .build();
        User save = userRepository.save(coach);

        Optional<User> byId = userRepository.findById(save.getUsername());
        assertThat(byId).isPresent();
        assertThat(byId.get()).isInstanceOf(Coach.class);
    }

}