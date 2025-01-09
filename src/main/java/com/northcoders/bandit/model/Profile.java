package com.northcoders.bandit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Profile {

    //Firebase will generate this id
    @Id
    private long id;

    public Profile(){

    }

    public Profile(long id){
        this.id = id;
    }



}
