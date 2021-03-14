package de.toomuchcoffee.pt.mapper;

import de.toomuchcoffee.pt.domain.entity.Program;
import de.toomuchcoffee.pt.dto.ProgramDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgramMapper {

    @Mapping(source = "coach", target = "id.coachId")
    @Mapping(source = "client", target = "id.clientId")
    Program toEntity(ProgramDto programDto);

    @Mapping(target = "coach", source = "id.coachId")
    @Mapping(target = "client", source = "id.clientId")
    ProgramDto toDto(Program program);
}