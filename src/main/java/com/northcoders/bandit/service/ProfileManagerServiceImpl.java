package com.northcoders.bandit.service;

import com.northcoders.bandit.model.Genre;
import com.northcoders.bandit.model.Instrument;
import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.repository.GenreManagerRepository;
import com.northcoders.bandit.repository.InstrumentManagerRepository;
import com.northcoders.bandit.repository.ProfileManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        //To prevent duplicate GenreType field in Genre table
        //If the genre is already present under a different id, use the existing genre/id combination
        //and add the new profile to the list of profiles connected to that genre.
        if(profile.getGenres() != null){
            profile.getGenres().forEach(genre -> {
                        if(genreManagerRepository.findByGenre(genre.getGenre()).isPresent()){
                            Genre existingGenre = genreManagerRepository.findByGenre(genre.getGenre()).get();
                            genre.setGenre_id(existingGenre.getGenre_id());
                            genre.setGenre(existingGenre.getGenre());
                            genreManagerRepository.save(genre);
                        }
                        else{
                            genreManagerRepository.save(genre);
                        }
                    });
        }
        if(profile.getInstruments() != null){
            profile.getInstruments().forEach(inst -> {
                if(instrumentManagerRepository.findByInstType(inst.getInstType()).isPresent()){
                    Instrument existingInstrument = instrumentManagerRepository.findByInstType(inst.getInstType()).get();
                    inst.setInstrument_id(existingInstrument.getInstrument_id());
                    inst.setInstType(existingInstrument.getInstType());
                    instrumentManagerRepository.save(inst);
                }
                else{
                    instrumentManagerRepository.save(inst);
                }
            });
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

    //TODO refactor to use firebase id assigned here
    @Override
    public Profile deleteById(long id) {
        Optional<Profile> profileOptional = profileManagerRepository.findById(id);
        if(profileOptional.isPresent()){
            profileManagerRepository.deleteById(id);
            return profileOptional.get();
        }
        else{
            return null;
        }
    }
}
