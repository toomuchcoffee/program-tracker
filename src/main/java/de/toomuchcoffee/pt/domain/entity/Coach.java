package de.toomuchcoffee.pt.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

import static javax.persistence.CascadeType.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Table(name = "pt_user")
@DiscriminatorValue("COACH")
public class Coach extends User {
    private String fullName;

    @OneToMany(mappedBy = "coach", cascade = {PERSIST, MERGE, DETACH})
    private Set<Client> clients;
}
