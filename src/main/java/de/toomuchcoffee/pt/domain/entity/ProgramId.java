package de.toomuchcoffee.pt.domain.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class ProgramId implements Serializable {
    private String coachId;
    private String clientId;
}
