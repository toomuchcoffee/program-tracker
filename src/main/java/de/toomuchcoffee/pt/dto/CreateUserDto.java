package de.toomuchcoffee.pt.dto;

import de.toomuchcoffee.pt.domain.entity.Role;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto implements Serializable {
    private String username;
    private String password;
    private Role role;
}
