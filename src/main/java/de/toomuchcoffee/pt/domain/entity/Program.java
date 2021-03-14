package de.toomuchcoffee.pt.domain.entity;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Program {
    @EmbeddedId
    private ProgramId id;

    @NotEmpty
    private String notes;
}
