package de.toomuchcoffee.pt.dto;

import lombok.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto implements Serializable {
    @Pattern(regexp = "^((?!(admin)).)*$")
    private String username;
    @Size(min = 6)
    private String password;
    private String fullName;
}
