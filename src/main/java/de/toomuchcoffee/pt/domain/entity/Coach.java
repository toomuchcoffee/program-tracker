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
@DiscriminatorValue("COACH")
public class Coach extends User {
    private String fullName;
}
