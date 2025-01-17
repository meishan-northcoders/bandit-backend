package com.northcoders.bandit.mapper;

import com.northcoders.bandit.model.FireBaseUser;
import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.model.ProfileRequestDTO;

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

    public static Profile DTOToProfile(ProfileRequestDTO profileRequestDTO, FireBaseUser fireBaseUser){
        Profile profile = new Profile();
        profile.setFirebaseId(fireBaseUser.getUserId());
        profile.setProfile_name(fireBaseUser.getUserName());
        profile.setDescription(profileRequestDTO.getDescription());
        profile.setProfile_type(profileRequestDTO.getProfile_type());
        profile.setImg_url(profileRequestDTO.getImg_url().isEmpty()? fireBaseUser.getGooglePictureLink() : profileRequestDTO.getImg_url());
        profile.setLat(profileRequestDTO.getLat());
        profile.setLon(profileRequestDTO.getLon());
        profile.setMax_distance(profileRequestDTO.getMax_distance());
        profile.setGenres(profileRequestDTO.getGenres());
        profile.setInstruments(profileRequestDTO.getInstruments());
        return profile;
    }
}
