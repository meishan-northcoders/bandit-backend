package com.northcoders.bandit.service;

import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.model.ProfileResponseDTO;

import java.util.ArrayList;
import java.util.Optional;

public interface ProfileManagerService {

    Profile postProfile(Profile profile, String searchQuery);

    ArrayList<ProfileResponseDTO> getAllProfiles();

    Profile updateProfile(Profile profile, String searchQuery);

    boolean deleteById(String id);

    ArrayList<Profile> getFilteredProfiles();

    Optional<Profile> getUserProfile();
}
