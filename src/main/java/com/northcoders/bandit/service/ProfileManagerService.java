package com.northcoders.bandit.service;

import com.northcoders.bandit.model.Favourites;
import com.northcoders.bandit.model.Favourites;
import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.model.ProfileRequestDTO;
import com.northcoders.bandit.model.ProfileResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ProfileManagerService {

    Profile postProfile(Profile profile);

    ArrayList<ProfileResponseDTO> getAllProfiles();

    Profile updateProfile(ProfileRequestDTO profile);

    boolean deleteById(String id);

    List<Profile> getFilteredProfiles();

    Optional<Profile> getUserProfile();

    List<Profile> getUserFavourites(List<Favourites> favourites);
    boolean existsByProfileId(String profileId);
    Profile findById(String profileId);
    List<Profile> getListOfFavProfile(String favProfileId);



 }
