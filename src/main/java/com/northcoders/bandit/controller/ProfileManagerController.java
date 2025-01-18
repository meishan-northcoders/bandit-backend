package com.northcoders.bandit.controller;

import com.northcoders.bandit.mapper.ProfileRequestDTOMapper;
import com.northcoders.bandit.mapper.ProfileResponseDTOMapper;
import com.northcoders.bandit.model.FireBaseUser;
import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.model.ProfileRequestDTO;
import com.northcoders.bandit.model.ProfileResponseDTO;
import com.northcoders.bandit.service.ProfileManagerService;
import com.northcoders.bandit.service.UserInContextService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/profiles")
public class ProfileManagerController {

    @Autowired
    ProfileManagerService profileManagerService;

    @Autowired
    UserInContextService userInContextService;

    @GetMapping
    public ResponseEntity<ArrayList<ProfileResponseDTO>> getAllProfiles(@RequestHeader("Authorization") String authHeader){

        return new ResponseEntity<>(profileManagerService.getAllProfiles(), HttpStatus.OK);
    }

    //Current logged in user mappings:
    @PostMapping
    public ResponseEntity<ProfileResponseDTO> postProfile(@Valid @RequestBody ProfileRequestDTO profileRequestDTO, @RequestHeader("Authorization") String authHeader){
        FireBaseUser fireBaseUser = userInContextService.getcurrentUser();
        Profile profile = ProfileRequestDTOMapper.DTOToProfile(profileRequestDTO,fireBaseUser ); //the profile will contain the id.
        return new ResponseEntity<>(ProfileResponseDTOMapper.profileToDTO(profileManagerService.postProfile(profile)), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ProfileResponseDTO> putProfile(@RequestHeader("Authorization") String authHeader, @RequestBody ProfileRequestDTO profileRequestDTO){
        FireBaseUser fireBaseUser = userInContextService.getcurrentUser();
        Profile profile = ProfileRequestDTOMapper.DTOToProfile(profileRequestDTO, fireBaseUser);
        return new ResponseEntity<>(ProfileResponseDTOMapper.profileToDTO(profileManagerService.updateProfile(profile)), HttpStatus.OK);

    }

    @GetMapping("/user")
    public ResponseEntity<Optional<ProfileResponseDTO>> getUserProfile(){
        Optional<ProfileResponseDTO> profileResponseDTO = profileManagerService.getUserProfile().map(profile ->
                new ProfileResponseDTO(profile.getImg_url(),
                        profile.getProfile_id(),
                        profile.getProfile_type(),
                        profile.getDescription(),
                        profile.getLat(),
                        profile.getLon(),
                        profile.getMax_distance(),
                        profile.getGenres(),
                        profile.getInstruments()
                ));
        return new ResponseEntity<>(profileResponseDTO, HttpStatus.OK);
    }

    //TODO refactor to use firebase id - surely only delete OWN USER Profile?
    @DeleteMapping
    public ResponseEntity<String> deleteProfile(@RequestHeader("Authorization")String header){
        FireBaseUser fireBaseUser = userInContextService.getcurrentUser();
        String userId = fireBaseUser.getUserId();
        boolean isDeleted = profileManagerService.deleteById(userId);
        if(isDeleted){
            return new ResponseEntity<>("Successfully deleted profile with id :" + userId, HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to delete profile with id :" + userId,HttpStatus.NOT_FOUND);
    }

    //TODO discuss name scheme for filtered profiles (e.g. the recommended profiles based on service layer algorithm)
    //I have kept no request param as filtering will take place using the user's firebase id entirely in backend service layer
    @GetMapping("/filtered")
    public ResponseEntity<ArrayList<Profile>> getFilteredProfiles(){

        return new ResponseEntity<>(profileManagerService.getFilteredProfiles(), HttpStatus.OK);
    }

}
