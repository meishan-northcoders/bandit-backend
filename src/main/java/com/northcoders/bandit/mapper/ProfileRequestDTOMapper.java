package com.northcoders.bandit.mapper;

import com.northcoders.bandit.model.FireBaseUser;
import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.model.ProfileRequestDTO;

import java.util.UUID;

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
        /**
         * * to solve profileID Generation issue, setting uuid here, refer to #profileID Generation issue, described in Profile Entity */
        profile.setProfile_id(UUID.randomUUID().toString());
        profile.setFirebaseId(fireBaseUser.getUserId());
        profile.setProfile_name((profileRequestDTO.getUserName()== null || profileRequestDTO.getUserName().isBlank())? fireBaseUser.getUserName(): profileRequestDTO.getUserName());
        profile.setDescription(profileRequestDTO.getDescription());
        profile.setProfile_type(profileRequestDTO.getProfile_type());
        profile.setImg_url((profileRequestDTO.getImg_url()== null || profileRequestDTO.getImg_url().isBlank())? fireBaseUser.getGooglePictureLink() : profileRequestDTO.getImg_url());
        profile.setLat(profileRequestDTO.getLat());
        profile.setLon(profileRequestDTO.getLon());
        profile.setMax_distance(profileRequestDTO.getMax_distance());
        profile.setGenres(profileRequestDTO.getGenres());
        profile.setInstruments(profileRequestDTO.getInstruments());
        profile.setCity(profileRequestDTO.getCity());
        profile.setCountry(profileRequestDTO.getCountry());
        profile.setSearch_query(profileRequestDTO.getSearchQuery());
        return profile;
    }


}
