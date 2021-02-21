package de.toomuchcoffee.pt.domain.repository;

import de.toomuchcoffee.pt.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}