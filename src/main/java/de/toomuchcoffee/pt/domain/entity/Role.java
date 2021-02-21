package de.toomuchcoffee.pt.domain.entity;

import lombok.Getter;

public enum Role {
    ADMIN("CLIENT", "COACH", "ADMIN", "USER"),
    CLIENT("CLIENT", "USER"),
    COACH("COACH", "USER");

    @Getter
    private final String[] authorities;

    private Role(String... authorities) {
        this.authorities = authorities;
    }
}
