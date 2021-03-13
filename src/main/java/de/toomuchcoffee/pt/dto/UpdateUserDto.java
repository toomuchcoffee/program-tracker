package de.toomuchcoffee.pt.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto implements Serializable {
    private String username;
    private String password;
    private String fullName;
}
