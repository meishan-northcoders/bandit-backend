package com.northcoders.bandit.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Profile {

    //Firebase will generate this id
    @Id
    private long profile_id;

    @Column
    private String img_url;

    @Column
    private boolean band_or_musician;

    @Column
    private String description;

    @Column
    private float lat;

    @Column
    private float lon;

    @Column
    private float max_distance;

    @ManyToMany
    @JoinTable(name = "profile_genre",
    joinColumns = @JoinColumn(name = "profile_id"),
    inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;

    @ManyToMany
    @JoinTable(name = "profile_instrument",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "instrument_id"))
    private Set<Instrument> instruments;

    public Profile() {

    }


    public Profile(long profile_id, String img_url, boolean band_or_musician, String description, float lat, float lon, float max_distance, Set<Genre> genres, Set<Instrument> instruments) {
        this.profile_id = profile_id;
        this.img_url = img_url;
        this.band_or_musician = band_or_musician;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
        this.max_distance = max_distance;
        this.genres = genres;
        this.instruments = instruments;
    }
}
