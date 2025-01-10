package com.northcoders.bandit.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "profile")
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "profile_genre",
    joinColumns = @JoinColumn(name = "profile_id"),
    inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;

    @ManyToMany(fetch = FetchType.EAGER) //I don't really understand FetchType but this prevents HttpMessageNotWritableException for Delete mapping
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



    public long getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(long profile_id) {
        this.profile_id = profile_id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public boolean isBand_or_musician() {
        return band_or_musician;
    }

    public void setBand_or_musician(boolean band_or_musician) {
        this.band_or_musician = band_or_musician;
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

    @Override
    public String toString() {
        return "Profile{" +
                "profile_id=" + profile_id +
                ", img_url='" + img_url + '\'' +
                ", band_or_musician=" + band_or_musician +
                ", description='" + description + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", max_distance=" + max_distance +
                ", genres=" + genres +
                ", instruments=" + instruments +
                '}';
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
}
