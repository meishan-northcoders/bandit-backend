package com.northcoders.bandit.service;

import com.northcoders.bandit.model.Genre;
import com.northcoders.bandit.model.Instrument;
import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.model.ProfileType;
import com.northcoders.bandit.repository.GenreManagerRepository;
import com.northcoders.bandit.repository.InstrumentManagerRepository;
import com.northcoders.bandit.repository.ProfileManagerRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    public Profile updateProfile(Profile profile) {

        //not sure if this is necessary but have added anyway in case - merges the new and existing if any fields in new are null.
        Optional<Profile> existingOpt = profileManagerRepository.findById(profile.getProfile_id());
        if(existingOpt.isPresent()){
            Profile existing = existingOpt.get();
            if(profile.getProfile_type() == null){
                profile.setProfile_type(existing.getProfile_type());
            }
            if(profile.getDescription() == null){
                profile.setDescription(existing.getDescription());
            }
            if (profile.getImg_url() == null) {
                profile.setImg_url(existing.getImg_url());
            }
            if(profile.getLat() == 0){
                profile.setLon(existing.getLon());
            }
            if(profile.getLon() == 0){
                profile.setLon(existing.getLon());
            }
            if(profile.getMax_distance() == 0){
                profile.setMax_distance(existing.getMax_distance());
            }
            if(profile.getGenres() == null){
                profile.setGenres(existing.getGenres());
            }
            if(profile.getInstruments() == null){
                profile.setInstruments(existing.getInstruments());
            }
        }

        return profileManagerRepository.save(profile);
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

        //get current logged-in user's profile, and perform operations to return relevant other profiles for user to swipe.

        //TODO firebase implementation in here, placeholder code to provide 5 profiles below:
        ArrayList<Profile> filtered = new ArrayList<>();

        Set<Genre> genres = new HashSet<>();
        Set<Instrument> instruments = new HashSet<>();

        genres.add(new Genre("ROCK", null));
        instruments.add(new Instrument("BASS", null));

        for (int i = 0; i < 5; i++) {
            Profile profile = new Profile("test"+i, "test url", ProfileType.MUSICIAN,
                    "test description", 0f, 0f, 100f, genres, instruments);
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
    public Profile getUserProfile() {
        return getCurrentUser();
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
