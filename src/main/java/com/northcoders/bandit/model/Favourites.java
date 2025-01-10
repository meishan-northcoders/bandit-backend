package com.northcoders.bandit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table
@IdClass(FavouritesId.class)
public class Favourites {

    @Id
    Long interestedUser;
    @Id
    Long userOfInterest;

    public Favourites() {
    }

    public Long getInterestedUser() {
        return interestedUser;
    }
    public Long getUserOfInterest(){
        return userOfInterest;
    }
    public void setUserOfInterest(Long interestedUser) {
        this.interestedUser = interestedUser;
    }

    public void setInterestedUser(Long interestedUser) {
        this.interestedUser = interestedUser;
    }
}
