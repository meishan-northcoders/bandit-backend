package com.northcoders.bandit.model;

import jakarta.persistence.*;

@Entity
@Table(name = "favourites")
public class Favourites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favId;

    @Column
    private String favProfileId;

    @Column
    private String yrFavProfileId;

    @Column
    @Enumerated(EnumType.STRING)
    private LikedOrDisliked isLikedOrDisliked;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable=false)
    private Profile profile;


    public Favourites() {
    }

    public Favourites(String profileId, String yrFavProfileId) {
        this.favProfileId = profileId;
        this.yrFavProfileId = yrFavProfileId;
    }

    public String getFavProfileId() {
        return favProfileId;
    }

    public void setFavProfileId(String favProfileId) {
        this.favProfileId = favProfileId;
    }

    public Long getFavId() {
        return favId;
    }

    public void setFavId(Long favId) {
        this.favId = favId;
    }

    public String getYrFavProfileId() {
        return yrFavProfileId;
    }

    public void setYrFavProfileId(String yrFavProfileId) {
        this.yrFavProfileId = yrFavProfileId;
    }

    public LikedOrDisliked getIsLikedOrDisliked() {
        return isLikedOrDisliked;
    }

    public void setIsLikedOrDisliked(LikedOrDisliked isLikedOrDisliked) {
        this.isLikedOrDisliked = isLikedOrDisliked;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
