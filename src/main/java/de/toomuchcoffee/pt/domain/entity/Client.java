package de.toomuchcoffee.pt.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Table(name = "pt_user")
@DiscriminatorValue("CLIENT")
public class Client extends User {
    private String fullName;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private Coach coach;
}
