package com.northcoders.bandit.service;

import com.northcoders.bandit.model.*;
import com.northcoders.bandit.repository.GenreManagerRepository;
import com.northcoders.bandit.repository.InstrumentManagerRepository;
import com.northcoders.bandit.repository.ProfileManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

        return profileManagerRepository.save(profile);
    }

    @Override
    public ArrayList<Profile> getAllProfiles() {
        //Peter - I have converted between Iterator and ArrayList here in Service Layer
        ArrayList<Profile> profiles = new ArrayList<>();
        profileManagerRepository.findAll().forEach(profiles::add);
        return profiles;
    }

    @Override
    public boolean deleteById(String id) {
        Optional<Profile> profileOptional = profileManagerRepository.findById(id);
        if(profileOptional.isPresent()){
            profileManagerRepository.deleteById(id);
            return true;
        }
            return false;
    }


    @Override
    public ArrayList<Profile> getFilteredProfiles() {

        //Profile currentUser = getCurrentUser();

        //TODO firebase implementation in here, placeholder code to provide 5 profiles below:
        ArrayList<Profile> filtered = new ArrayList<>();

        Set<Genre> genres = new HashSet<>();
        Set<Instrument> instruments = new HashSet<>();

        genres.add(new Genre("ROCK", null));
        instruments.add(new Instrument("BASS", null));

        for (int i = 0; i < 5; i++) {
            Profile profile = new Profile("test"+i, "test url", ProfileType.MUSICIAN,
                    "test description", 0f, 0f, 100f, genres, instruments, null);
            genres.forEach(genre ->{
                        Set<Profile> genreProfiles = new HashSet<>();
                        genreProfiles.add(profile);
                        genre.setProfiles(genreProfiles);
                    }
            );
            instruments.forEach(instrument -> {
                Set<Profile> instProfiles = new HashSet<>();
                instProfiles.add(profile);
                instrument.setProfiles(instProfiles);
            });
            filtered.add(profile);
        }

        return filtered;

    }

    @Override
    public List<Profile> getUserFavourites(List<Favourite> favourites) {
        List<Profile> favouritesList = new ArrayList<>();
        for (Favourite f : favourites){
            favouritesList.add(profileManagerRepository.findById(f.getFavProfileId()).get());
        }
        return favouritesList;
    }

    private Profile getCurrentUser(){
        String currentUserId = getAllProfiles().getFirst().getProfile_id(); //TODO get user id from firebase instance
        Optional<Profile> currentUserOptional = profileManagerRepository.findById(currentUserId);
        if(currentUserOptional.isPresent()){
            return currentUserOptional.get();
        }
        throw new RuntimeException("No current user found with firebase id: " + currentUserId); //TODO create custom exception
    }

}
