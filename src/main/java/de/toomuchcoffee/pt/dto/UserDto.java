package de.toomuchcoffee.pt.dto;

import de.toomuchcoffee.pt.domain.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserDto implements Serializable {
    private String username;
    private String password;
    private String fullName;
    private Role role;
}
