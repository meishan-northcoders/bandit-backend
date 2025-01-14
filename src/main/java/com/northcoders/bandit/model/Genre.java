package com.northcoders.bandit.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "genre")
public class Genre {

    @Id
    private String genre;

    @JsonIgnore
    @ManyToMany(mappedBy = "genres")
    private Set<Profile> profiles;

    public Genre(String genre, Set<Profile> profiles) {
        this.genre = genre;
        this.profiles = profiles;
    }

    public Genre() {
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }
}
