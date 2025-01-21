package com.northcoders.bandit.dto.request;

public class AddToFavouriteRequestDTO {
    private String favProfileId;
    private String yrFavProfileId;

    public AddToFavouriteRequestDTO(String favProfileId, String yrFavProfileId) {
        this.favProfileId = favProfileId;
        this.yrFavProfileId = yrFavProfileId;
    }

    public String getFavProfileId() {
        return favProfileId;
    }

    public String getYrFavProfileId() {
        return yrFavProfileId;
    }


}
