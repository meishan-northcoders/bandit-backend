package com.northcoders.bandit.mapper;

import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.model.ProfileResponseDTO;

import java.util.Random;

public class ProfileResponseDTOMapper {

    static int tempIdCounter;

    public static ProfileResponseDTO profileToDTO(Profile profile){

        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO();
        profileResponseDTO.setProfile_id(profile.getProfile_id());
        profileResponseDTO.setDescription(profile.getDescription());
        profileResponseDTO.setProfile_type(profile.getProfile_type());
        profileResponseDTO.setUserName(profile.getProfile_name());
        profileResponseDTO.setImg_url(profile.getImg_url());
        profileResponseDTO.setLat(profile.getLat());
        profileResponseDTO.setLon(profile.getLon());
        profileResponseDTO.setMax_distance(profile.getLon());
        profileResponseDTO.setGenres(profile.getGenres());
        profileResponseDTO.setInstruments(profile.getInstruments());

        return profileResponseDTO;
    }

    public static Profile DTOToProfile(ProfileResponseDTO profileResponseDTO){

        Profile profile = new Profile();

        tempIdCounter++;

        profile.setProfile_id("replace this id with firebase id" + tempIdCounter);//TODO get id from firebase helper class
        profile.setDescription(profileResponseDTO.getDescription());
        profile.setProfile_type(profileResponseDTO.getProfile_type());
        profile.setImg_url(profileResponseDTO.getImg_url());
        profile.setLat(profileResponseDTO.getLat());
        profile.setLon(profileResponseDTO.getLon());
        profile.setMax_distance(profileResponseDTO.getMax_distance());
        profile.setGenres(profileResponseDTO.getGenres());
        profile.setInstruments(profileResponseDTO.getInstruments());

        return profile;

    }
}
