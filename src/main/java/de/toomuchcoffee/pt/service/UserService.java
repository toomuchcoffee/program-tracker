package de.toomuchcoffee.pt.service;

import de.toomuchcoffee.pt.domain.entity.User;
import de.toomuchcoffee.pt.domain.repository.UserRepository;
import de.toomuchcoffee.pt.dto.CreateUserDto;
import de.toomuchcoffee.pt.dto.ReadUserDto;
import de.toomuchcoffee.pt.dto.UpdateUserDto;
import de.toomuchcoffee.pt.mapper.UserMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;

    @SneakyThrows({
            NoSuchMethodException.class,
            IllegalAccessException.class,
            InvocationTargetException.class,
            InstantiationException.class
    })
    @Transactional
    public void create(CreateUserDto createUserDto) {
        User user = createUserDto.getRole().getUserClass()
                .getDeclaredConstructor().newInstance();
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
