package de.toomuchcoffee.pt.mapper;


import de.toomuchcoffee.pt.domain.entity.Program;
import de.toomuchcoffee.pt.dto.ProgramDto;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProgramMapperTest {

    private final ProgramMapper programMapper = Mappers.getMapper(ProgramMapper.class);

    @Test
    public void shouldMapToEntity() {
        ProgramDto dto = ProgramDto.builder()
                .coach("coach")
                .client("client")
                .notes("foobar")
                .build();

        Program program = programMapper.toEntity(dto);

        assertThat(program.getNotes()).isEqualTo(dto.getNotes());
        assertThat(program.getId().getCoachId()).isEqualTo(dto.getCoach());
        assertThat(program.getId().getClientId()).isEqualTo(dto.getClient());
    }
}