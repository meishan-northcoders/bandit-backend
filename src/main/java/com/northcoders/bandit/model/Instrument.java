package com.northcoders.bandit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

@Entity
@Table(name = "instrument")
public class Instrument {

    //TODO refactor into string for hashtag style extension in frontend
    @Id
    @Pattern(regexp = "[a-zA-Z0-9- ]+", message = "Genre must not contain any special characters")
    private String instrument;

    @JsonIgnore
    @ManyToMany(mappedBy = "instruments")
    private Set<Profile> profiles;

    public Instrument(){

    }

    public Instrument( String instType, Set<Profile> profiles) {
        this.instrument = instType.toUpperCase();
        this.profiles = profiles;
    }


    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument.toUpperCase();
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }
}
