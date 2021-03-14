package de.toomuchcoffee.pt.domain.repository;

import de.toomuchcoffee.pt.domain.entity.Program;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.DOCKER;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureEmbeddedDatabase(provider = DOCKER)
public class ProgramRepositoryTest {

    @Autowired
    private ProgramRepository programRepository;

    @Before
    public void setUp() throws Exception {
        programRepository.deleteAll();
    }

    @Test
    public void findsAll() {
        Program program = Program.builder()
                .notes("foo bar")
                .build();
        programRepository.save(program);
        List<Program> programs = programRepository.findAll();
        assertThat(programs).hasSize(1);
        assertThat(programs.get(0)).isEqualToComparingFieldByField(program);
    }

}