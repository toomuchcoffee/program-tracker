package de.toomuchcoffee.pt.dto;

import de.toomuchcoffee.pt.domain.entity.Role;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadUserDto implements Serializable {
    private String username;
    private String fullName;
    private Role role;
}
