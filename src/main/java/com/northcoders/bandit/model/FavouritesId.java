package com.northcoders.bandit.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serializable;

public class FavouritesId implements Serializable {

    @Id
    @GeneratedValue
    Long favId;
    @Column
    private String profileId;
    @Column
    private String yrFavProfileId;


    public FavouritesId(String profileId, String yrFavProfileId){
        this.profileId = profileId;
        this.yrFavProfileId = yrFavProfileId;
    }


}
