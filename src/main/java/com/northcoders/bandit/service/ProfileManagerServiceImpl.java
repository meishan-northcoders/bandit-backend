package com.northcoders.bandit.service;

import com.northcoders.bandit.model.Instrument;
import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.repository.GenreManagerRepository;
import com.northcoders.bandit.repository.InstrumentManagerRepository;
import com.northcoders.bandit.repository.ProfileManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProfileManagerServiceImpl implements ProfileManagerService {

    @Autowired
    GenreManagerRepository genreManagerRepository;

    @Autowired
    InstrumentManagerRepository instrumentManagerRepository;

    @Autowired
    ProfileManagerRepository profileManagerRepository;

    @Override
    public Profile postProfile(Profile profile) {
        System.out.println(profile.toString());

        if(profile.getGenres() != null){
            System.out.printf("Genres in profile");
            profile.getGenres().forEach(genreManagerRepository::save);
        }
        if(profile.getInstruments() != null){
            System.out.println("Insts in profile");
            profile.getInstruments().forEach(instrumentManagerRepository::save);
        }
        return profileManagerRepository.save(profile);
    }

    @Override
    public ArrayList<Profile> getAllProfiles() {
        //Peter - I have converted between Iterator and ArrayList here in Service Layer
        ArrayList<Profile> profiles = new ArrayList<>();
        profileManagerRepository.findAll().forEach(profiles::add);
        return profiles;
    }
}
