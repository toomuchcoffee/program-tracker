package de.toomuchcoffee.pt.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDto implements Serializable {
    @NotEmpty
    private String coach;
    @NotEmpty
    private String client;
    @NotEmpty
    private String notes;
}
