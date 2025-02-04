package com.northcoders.bandit.model;

import java.util.Set;

public class ProfileResponseDTO {

    private String img_url;

    private String userName;
    private String profile_id;

    private ProfileType profile_type;

    private String description;

    private float lat;

    private float lon;

    private float max_distance;

    private Set<Genre> genres;

    private Set<Instrument> instruments;

    private String city;

    private String country;

    private String searchQuery;

    private Float profileRank;


    public ProfileResponseDTO() {
    }

    public ProfileResponseDTO(String img_url,String userName, String profile_id, ProfileType profile_type, String description, float lat, float lon, float max_distance, Set<Genre> genres, Set<Instrument> instruments, String city, String country, String searchQuery, Float profileRank) {
        this.img_url = img_url;
        this.userName =userName;
        this.profile_id = profile_id;
        this.profile_type = profile_type;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
        this.max_distance = max_distance;
        this.genres = genres;
        this.instruments = instruments;
        this.city = city;
        this.country = country;
        this.searchQuery = searchQuery;
        this.profileRank = profileRank;
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

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Float getProfileRank() {
        return profileRank;
    }

    public void setProfileRank(Float profileRank) {
        this.profileRank = profileRank;
    }
}
