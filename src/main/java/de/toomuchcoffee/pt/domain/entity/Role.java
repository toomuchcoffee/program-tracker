package de.toomuchcoffee.pt.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    CLIENT(Client.class), COACH(Coach.class);

    @Getter
    private final Class<? extends User> userClass;
}
