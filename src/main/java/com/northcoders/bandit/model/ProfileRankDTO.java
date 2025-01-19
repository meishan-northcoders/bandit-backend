package com.northcoders.bandit.model;

public class ProfileRankDTO {

    private String profileId;
    private Float profileRank;

    public ProfileRankDTO(String profileId, Float profileRank) {
        this.profileId = profileId;
        this.profileRank = profileRank;
    }

    public Float getProfileRank() {
        return profileRank;
    }

    public void setProfileRank(Float profileRank) {
        this.profileRank = profileRank;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
}
