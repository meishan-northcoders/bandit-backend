package com.northcoders.bandit.service;

import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.repository.ProfileManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProfileManagerServiceImpl implements ProfileManagerService {


    @Autowired
    ProfileManagerRepository profileManagerRepository;

    @Override
    public Profile postProfile(Profile profile) {
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
