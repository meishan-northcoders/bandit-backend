package com.northcoders.bandit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.Set;

@Entity
public class Genre {

    @Id
    private long id;

    @Column
    private GenreType genre;

    //A set allows no duplicate elements e.g. the same profile twice attached to the same genre.
    @ManyToMany(mappedBy = "genres")
    private Set<Profile> profiles;

    public Genre(long id, GenreType genre, Set<Profile> profiles) {
        this.id = id;
        this.genre = genre;
        this.profiles = profiles;
    }

    public Genre() {
    }
}
