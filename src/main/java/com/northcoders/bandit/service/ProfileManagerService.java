package com.northcoders.bandit.service;

import com.northcoders.bandit.model.Profile;

import java.util.ArrayList;

public interface ProfileManagerService {

    Profile postProfile(Profile profile);

    ArrayList<Profile> getAllProfiles();

    Profile deleteById(long id);
 }
