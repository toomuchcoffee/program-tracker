package de.toomuchcoffee.pt.service;

import de.toomuchcoffee.pt.domain.entity.Role;
import de.toomuchcoffee.pt.domain.entity.User;
import de.toomuchcoffee.pt.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthenticatedUserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional
    public void save(String username, String password, Role role) {
        User user = userRepository.findById(username)
                .map(found -> {
                    found.setPassword(encoder.encode(password));
                    found.setRole(role);
                    return found;
                })
                .orElse(new User(username, encoder.encode(password), role));
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteByUsername(String username) {
        userRepository.deleteById(username);
    }
}
