package com.northcoders.bandit.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import java.util.Set;

@Entity
@Table(name = "profile")
public class Profile {

    //Firebase will generate this id as a STRING
    @Id
    /** Issue : profileID Generation issue
     * @GeneratedValue(strategy = GenerationType.UUID)
     * if id is annotated with generated,
     * its value is generated at the time of save, and therefore
     *  when trying to fetch the relationship between existing genre and profile(genre is the primary key in this case),
     *  it fails and tries to save Genre as new value due to Cascade all.
     *  */
    private String profile_id;

    @Column(name = "firebase_id")
    private String firebaseId;

    @Column
    private String profile_name;

    @Column
    private String img_url;

    @Column
    private ProfileType profile_type;

    @Column
    private String description;

    @Column
    private float lat;

    @Column
    private float lon;

    @Column
    private float max_distance;

    @Column
    private String city;

    @Column
    private String country;

    @Column
    private String profile_tags;


    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinTable(name = "profile_genre",
    joinColumns = @JoinColumn(name = "profile_id"),
    inverseJoinColumns = @JoinColumn(name = "genre"))
    private Set<Genre> genres;

    @ManyToMany(fetch = FetchType.EAGER) //I don't really understand FetchType but this prevents HttpMessageNotWritableException for Delete mapping
    @JoinTable(name = "profile_instrument",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "instrument_id"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Instrument> instruments;

    @OneToOne(mappedBy = "profile")
    private SearchPreference searchPreference;

    public SearchPreference getSearchPreference() {
        return searchPreference;
    }

    public void setSearchPreference(SearchPreference searchPreference) {
        this.searchPreference = searchPreference;
    }

//    @OneToOne(fetch = FetchType.EAGER, mappedBy = "profile", optional =true)
//    @Cascade(org.hibernate.annotations.CascadeType.ALL)
//    private SearchPreference searchPreference;



    public Profile() {

    }


    public Profile(String profile_id, String firebase_id , String profile_name, String img_url, ProfileType profile_type, String description, float lat, float lon, float max_distance, String city, String country, String profileTags, Set<Genre> genres, Set<Instrument> instruments) {
        this.profile_id = profile_id;
        this.firebaseId = firebase_id;
        this.img_url = img_url;
        this.profile_type = profile_type;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
        this.max_distance = max_distance;
        this.city = city;
        this.country = country;
        profile_tags = profileTags;
        this.genres = genres;
        this.instruments = instruments;
        this.profile_name = profile_name;
    }



    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
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

    @Override
    public String toString() {
        return "Profile{" +
                "profile_id=" + profile_id +
                ", img_url='" + img_url + '\'' +
                ", band_or_musician=" + profile_type +
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

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebase_id) {
        this.firebaseId = firebase_id;
    }


    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
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

    public String getProfile_tags() {
        return profile_tags;
    }

    public void setProfile_tags(String profile_tags) {
        this.profile_tags = profile_tags;
    }

//    public SearchPreference getSearchPreference() {
//        return searchPreference;
//    }
//
//    public void setSearchPreference(SearchPreference searchPreference) {
//        this.searchPreference = searchPreference;
//    }
}
