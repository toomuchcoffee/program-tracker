package de.toomuchcoffee.pt.dto;

import de.toomuchcoffee.pt.domain.entity.Role;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto implements Serializable {
    @NotEmpty
    @Size(min = 3)
    @Pattern(regexp = "^((?!(admin)).)*$")
    private String username;
    @NotEmpty
    @Size(min = 6)
    private String password;
    @NotEmpty
    private Role role;
}
