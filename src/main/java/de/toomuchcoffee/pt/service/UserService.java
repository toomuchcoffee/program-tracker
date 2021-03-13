package de.toomuchcoffee.pt.service;

import de.toomuchcoffee.pt.domain.entity.Client;
import de.toomuchcoffee.pt.domain.entity.Coach;
import de.toomuchcoffee.pt.domain.entity.User;
import de.toomuchcoffee.pt.domain.repository.UserRepository;
import de.toomuchcoffee.pt.dto.CreateUserDto;
import de.toomuchcoffee.pt.dto.ReadUserDto;
import de.toomuchcoffee.pt.dto.UpdateUserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public User findByUsername(String username) {
        return userRepository.findById(username).orElse(null);
    }

    @Transactional
    public void save(CreateUserDto createUserDto) {
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
        user.setUsername(createUserDto.getUsername());
        user.setPassword(encoder.encode(createUserDto.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void update(UpdateUserDto user) {
        userRepository.findById(user.getUsername())
                .ifPresentOrElse(u -> {
                    BeanUtils.copyProperties(user, u);
                    userRepository.save(u);
                }, () -> {
                    throw new IllegalArgumentException("Could not find existing user with username " + user.getUsername());
                });
    }

    public List<ReadUserDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> {
                    ReadUserDto dto = new ReadUserDto();
                    BeanUtils.copyProperties(user, dto);
                    return dto;
                }).collect(toList());
    }

    public void deleteByUsername(String username) {
        userRepository.deleteById(username);
    }

    public List<String> findAvailableClients() {
        return userRepository.findAll().stream()
                .filter(u -> u instanceof Client)
                .map(Client.class::cast)
                .filter(client -> client.getCoach() == null)
                .map(Client::getUsername)
                .collect(toList());
    }

    public List<String> findClientsForCoach(String username) {
        return userRepository.findById(username)
                .filter(u -> u instanceof Coach)
                .map(Coach.class::cast)
                .map(Coach::getClients)
                .map(clients -> clients.stream()
                        .map(User::getUsername)
                        .collect(toList()))
                .orElse(new ArrayList<>());
    }

    public void addClient(UpdateUserDto coach, String clientUsername) {
        userRepository.findById(coach.getUsername())
                .map(Coach.class::cast)
                .ifPresentOrElse(c -> userRepository.findById(clientUsername)
                        .map(Client.class::cast)
                        .ifPresent(u -> {
                            u.setCoach(c);
                            userRepository.save(u);
                        }), () -> {
                    throw new IllegalArgumentException("Could not find existing user with username " + coach.getUsername());
                });
    }

    public void removeClient(UpdateUserDto coach, String clientUsername) {
        userRepository.findById(coach.getUsername())
                .map(Coach.class::cast)
                .ifPresentOrElse(c -> {
                    userRepository.findById(clientUsername)
                            .map(Client.class::cast)
                            .ifPresent(client -> {
                                client.setCoach(null);
                                userRepository.save(client);
                            });
                }, () -> {
                    throw new IllegalArgumentException("Could not find existing user with username " + coach.getUsername());
                });
    }
}
