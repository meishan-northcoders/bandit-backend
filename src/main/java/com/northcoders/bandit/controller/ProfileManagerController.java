package com.northcoders.bandit.controller;

import com.northcoders.bandit.mapper.ProfileRequestDTOMapper;
import com.northcoders.bandit.mapper.ProfileResponseDTOMapper;
import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.model.ProfileRequestDTO;
import com.northcoders.bandit.model.ProfileResponseDTO;
import com.northcoders.bandit.service.ProfileManagerService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/profiles")
public class ProfileManagerController {

    @Autowired
    ProfileManagerService profileManagerService;

    @GetMapping
    public ResponseEntity<ArrayList<ProfileResponseDTO>> getAllProfiles(){
        ArrayList<Profile> profiles = profileManagerService.getAllProfiles();

        ArrayList<ProfileResponseDTO> profileResponseDTOs = new ArrayList<>();
        profiles.forEach(profile -> {
            profileResponseDTOs.add(ProfileResponseDTOMapper.profileToDTO(profile));
        });

        return new ResponseEntity<>(profileResponseDTOs, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProfileResponseDTO> postProfile(@RequestBody ProfileRequestDTO profileRequestDTO){
        Profile profile = ProfileRequestDTOMapper.DTOToProfile(profileRequestDTO);

        return new ResponseEntity<>(ProfileResponseDTOMapper.profileToDTO(profileManagerService.postProfile(profile)), HttpStatus.OK);
    }

    //TODO refactor to use firebase id
    @DeleteMapping
    public ResponseEntity<String> deleteProfile(@RequestParam(value = "id") String id){
        boolean isDeleted = profileManagerService.deleteById(id);
        if(isDeleted){
            return new ResponseEntity<>("Successfully deleted profile with id :" + id, HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to delete profile with id :" + id,HttpStatus.NOT_FOUND);
    }

    //TODO discuss name scheme for filtered profiles (e.g. the recommended profiles based on service layer algorithm)
    //I have kept no request param as filtering will take place using the user's firebase id entirely in backend service layer
    @GetMapping("/filtered")
    public ResponseEntity<ArrayList<ProfileResponseDTO>> getFilteredProfiles(){

        ArrayList<Profile> filteredProfiles = profileManagerService.getFilteredProfiles();

        ArrayList<ProfileResponseDTO> filteredProfileDTOs = new ArrayList<>();
        filteredProfiles.forEach(profile -> {
            filteredProfileDTOs.add(ProfileResponseDTOMapper.profileToDTO(profile));
        });

        return new ResponseEntity<>(filteredProfileDTOs, HttpStatus.OK);
    }


}
