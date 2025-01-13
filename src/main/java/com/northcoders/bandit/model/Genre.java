package com.northcoders.bandit.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "genre")
public class Genre {

    @Id
    private long genre_id;

    //TODO refactor into string for hashtag style extension in frontend
    @Column
    private String genre;

    //A set allows no duplicate elements e.g. the same profile twice attached to the same genre.
    @JsonIgnore
    @ManyToMany(mappedBy = "genres")
    private Set<Profile> profiles;

    public Genre(long id, String genre, Set<Profile> profiles) {
        this.genre_id = id;
        this.genre = genre;
        this.profiles = profiles;
    }

    public Genre() {
    }

    public long getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(long genre_id) {
        this.genre_id = genre_id;
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
