package com.northcoders.bandit.model;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

public class ProfileRequestDTO {

    @Pattern(regexp = "^(http|https):\\/\\/.*$", message = "invalid url")
    private String img_url;

    private String profileId; //generated within system and different from firebase user id.
    @Pattern(regexp = "[a-zA-Z0-9- ]+", message = "User Name must not contain any special characters")
    private String userName;
    @NotNull(message = "Profile type cannot be empty")
    private ProfileType profile_type;
    @NotNull(message = "Description type cannot be empty")
    @Pattern(regexp = "[a-zA-Z0-9- ]+", message = "description must not contain any special characters")
    private String description;
    @NotNull(message = "latitude cannot be empty")
    private float lat;
    @NotNull(message = "longitude cannot be empty")
    private float lon;

    private float max_distance;
    @NotNull(message = "Genre cannot be empty")
    private Set<Genre> genres;
    @NotNull(message = "Instrument cannot be empty")
    private Set<Instrument> instruments;

    public ProfileRequestDTO() {
    }

    public ProfileRequestDTO(String img_url, String profileId, String userName, ProfileType profile_type, String description, float lat, float lon, float max_distance, Set<Genre> genres, Set<Instrument> instruments) {
        this.img_url = img_url;
        this.profileId = profileId;
        this.userName = userName;
        this.profile_type = profile_type;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
        this.max_distance = max_distance;
        this.genres = genres;
        this.instruments = instruments;
    }
    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public ProfileType getProfile_type() {
        return profile_type;
    }

    public void setProfile_type(ProfileType profile_type) {
        this.profile_type = profile_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getMax_distance() {
        return max_distance;
    }

    public void setMax_distance(float max_distance) {
        this.max_distance = max_distance;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Set<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(Set<Instrument> instruments) {
        this.instruments = instruments;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
}
