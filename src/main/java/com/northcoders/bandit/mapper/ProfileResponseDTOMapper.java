package com.northcoders.bandit.mapper;

import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.model.ProfileResponseDTO;

import java.util.Optional;
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

//
}
