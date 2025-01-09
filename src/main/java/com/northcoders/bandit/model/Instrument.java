package com.northcoders.bandit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.Set;

@Entity
public class Instrument {

    //long - not nullable, Long - nullable
    @Id
    private long id;

    @Column
    private InstType instType;

    @ManyToMany(mappedBy = "instruments")
    private Set<Profile> profiles;

    public Instrument(){

    }

    public Instrument(long id, InstType instType, Set<Profile> profiles) {
        this.id = id;
        this.instType = instType;
    }
}
