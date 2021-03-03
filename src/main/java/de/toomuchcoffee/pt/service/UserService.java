package de.toomuchcoffee.pt.service;

import de.toomuchcoffee.pt.domain.entity.Client;
import de.toomuchcoffee.pt.domain.entity.Coach;
import de.toomuchcoffee.pt.domain.entity.Role;
import de.toomuchcoffee.pt.domain.entity.User;
import de.toomuchcoffee.pt.domain.repository.UserRepository;
import de.toomuchcoffee.pt.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void save(String username, String password, Role role) {
        User user;
        switch (role) {
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
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        userRepository.save(user);
    }

    @Transactional
    public void update(UserDto user) {
        userRepository.findById(user.getUsername()).ifPresent(u -> {
            BeanUtils.copyProperties(user, u);
            userRepository.save(u);
        });
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<Client> findAllClients() {
        return userRepository.findAll().stream()
                .filter(u -> u instanceof Client)
                .map(u -> (Client) u)
                .collect(toList());
    }

    public void deleteByUsername(String username) {
        userRepository.deleteById(username);
    }
}
