package com.northcoders.bandit.service;

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
}
