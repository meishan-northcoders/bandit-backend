package com.northcoders.bandit.mapper;

import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.model.ProfileRequestDTO;
import com.northcoders.bandit.model.ProfileResponseDTO;

public class ProfileRequestDTOMapper {

    static int tempIdCounter;

    public static ProfileRequestDTO profileToDTO(Profile profile){

        ProfileRequestDTO profileRequestDTO = new ProfileRequestDTO();

        profileRequestDTO.setDescription(profile.getDescription());
        profileRequestDTO.setProfile_type(profile.getProfile_type());
        profileRequestDTO.setImg_url(profile.getImg_url());
        profileRequestDTO.setLat(profile.getLat());
        profileRequestDTO.setLon(profile.getLon());
        profileRequestDTO.setMax_distance(profile.getLon());
        profileRequestDTO.setGenres(profile.getGenres());
        profileRequestDTO.setInstruments(profile.getInstruments());

        return profileRequestDTO;
    }

    public static Profile DTOToProfile(ProfileRequestDTO profileRequestDTO){

        Profile profile = new Profile();

        tempIdCounter++;

        profile.setProfile_id("replace this id with firebase id" + tempIdCounter);//TODO get id from firebase helper class
        profile.setDescription(profileRequestDTO.getDescription());
        profile.setProfile_type(profileRequestDTO.getProfile_type());
        profile.setImg_url(profileRequestDTO.getImg_url());
        profile.setLat(profileRequestDTO.getLat());
        profile.setLon(profileRequestDTO.getLon());
        profile.setMax_distance(profileRequestDTO.getMax_distance());
        profile.setGenres(profileRequestDTO.getGenres());
        profile.setInstruments(profileRequestDTO.getInstruments());

        return profile;

    }
}
