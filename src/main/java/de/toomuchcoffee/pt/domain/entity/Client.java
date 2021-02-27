package de.toomuchcoffee.pt.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "pt_user")
@DiscriminatorValue("CLIENT")
public class Client extends User {
    private String fullName;
}
