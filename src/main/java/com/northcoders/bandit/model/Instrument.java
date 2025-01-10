package com.northcoders.bandit.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "instrument")
public class Instrument {

    //long - not nullable, Long - nullable
    @Id
    private long instrument_id;

    @Column
    private InstType instType;

    @JsonIgnore
    @ManyToMany(mappedBy = "instruments")
    private Set<Profile> profiles;

    public Instrument(){

    }

    public Instrument(long instrument_id, InstType instType, Set<Profile> profiles) {
        this.instrument_id = instrument_id;
        this.instType = instType;
        this.profiles = profiles;
    }

    public long getInstrument_id() {
        return instrument_id;
    }

    public void setInstrument_id(long instrument_id) {
        this.instrument_id = instrument_id;
    }

    public InstType getInstType() {
        return instType;
    }

    public void setInstType(InstType instType) {
        this.instType = instType;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }
}
