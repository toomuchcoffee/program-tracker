package de.toomuchcoffee.pt.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Entity
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Table(name = "pt_user")
@DiscriminatorValue("COACH")
public class Coach extends User {
    private String fullName;

    @OneToMany(mappedBy = "coach", fetch = EAGER)
    private Set<Client> clients = new HashSet<>();

    @PreRemove
    public void clearClientsBeforeRemoval() {
        clients.forEach(client -> client.setCoach(null));
    }
}
