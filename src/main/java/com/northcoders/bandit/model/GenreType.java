package com.northcoders.bandit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import java.lang.reflect.Type;

public enum GenreType {
    ROCK,
    POP,
    JAZZ
}
