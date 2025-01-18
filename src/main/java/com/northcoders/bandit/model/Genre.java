package com.northcoders.bandit.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

@Entity
@Table(name = "genre")
public class Genre {

    @Id
    @NotNull(message = "Genre cannot be empty")
    @Pattern(regexp = "[a-zA-Z0-9- ]+", message = "Genre must not contain any special characters")
    private String genre;

    @JsonIgnore
    @ManyToMany(mappedBy = "genres")
    private Set<Profile> profiles;

    public Genre(String genre, Set<Profile> profiles) {
        this.genre = genre.toUpperCase();
        this.profiles = profiles;
    }

    public Genre() {
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre.toUpperCase();
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }
}
