package de.toomuchcoffee.pt.mapper;

import de.toomuchcoffee.pt.domain.entity.User;
import de.toomuchcoffee.pt.dto.CreateUserDto;
import de.toomuchcoffee.pt.dto.ReadUserDto;
import de.toomuchcoffee.pt.dto.UpdateUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class)
public interface UserMapper {
    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    void updateUserEntity(UpdateUserDto updateUserDto, @MappingTarget User userEntity);

    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    @Mapping(target = "role", ignore = true)
    void updateUserEntity(CreateUserDto createUserDto, @MappingTarget User user);

    ReadUserDto mapToReadUserDto(User userEntity);
}