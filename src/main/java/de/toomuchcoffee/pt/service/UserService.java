package de.toomuchcoffee.pt.service;

import de.toomuchcoffee.pt.domain.entity.Client;
import de.toomuchcoffee.pt.domain.entity.Coach;
import de.toomuchcoffee.pt.domain.entity.User;
import de.toomuchcoffee.pt.domain.repository.UserRepository;
import de.toomuchcoffee.pt.dto.CreateUserDto;
import de.toomuchcoffee.pt.dto.ReadUserDto;
import de.toomuchcoffee.pt.dto.UpdateUserDto;
import de.toomuchcoffee.pt.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void create(CreateUserDto createUserDto) {
        User user;
        switch (createUserDto.getRole()) {
            case CLIENT:
                user = new Client();
                break;
            case COACH:
                user = new Coach();
                break;
            case ADMIN:
            default:
                throw new IllegalArgumentException("Creation of admin user not supported");
        }
        userMapper.updateUserEntity(createUserDto, user);
        userRepository.save(user);
    }

    @Transactional
    public void update(UpdateUserDto user) {
        userRepository.findById(user.getUsername())
                .ifPresentOrElse(u -> {
                    userMapper.updateUserEntity(user, u);
                    userRepository.save(u);
                }, () -> {
                    throw new IllegalArgumentException("Could not find existing user with username " + user.getUsername());
                });
    }

    public void delete(String username) {
        userRepository.deleteById(username);
    }

    public List<ReadUserDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> userMapper.mapToReadUserDto(user))
                .collect(toList());
    }

}
