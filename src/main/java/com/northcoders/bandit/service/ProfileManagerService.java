package com.northcoders.bandit.service;

import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.model.ProfileResponseDTO;

import java.util.ArrayList;

public interface ProfileManagerService {

    Profile postProfile(Profile profile);

    ArrayList<Profile> getAllProfiles();

    Profile updateProfile(Profile profile);

    boolean deleteById(String id);

    ArrayList<Profile> getFilteredProfiles();

    Profile getUserProfile();
}
