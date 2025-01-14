package com.northcoders.bandit.model;

import jakarta.persistence.*;

@Entity
@Table

public class Favourites {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    Long favId;

    @Column
    private String profileId;
    @Column
    private String yrFavProfileId;

    public Favourites() {
    }

    public Favourites(String profileId, String yrFavProfileId) {
        this.profileId = profileId;
        this.yrFavProfileId = yrFavProfileId;
    }


}
