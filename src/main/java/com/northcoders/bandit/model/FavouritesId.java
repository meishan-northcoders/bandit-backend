package com.northcoders.bandit.model;

import java.io.Serializable;

public class FavouritesId implements Serializable {

    private Long interestedUser;
    private Long userOfInterest;


    public FavouritesId(Long interestedUser, Long userOfInterest){
        this.userOfInterest = userOfInterest;
        this.interestedUser = interestedUser;
    }


}
