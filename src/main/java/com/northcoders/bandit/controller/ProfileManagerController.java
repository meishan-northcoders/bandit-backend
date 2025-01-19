package com.northcoders.bandit.controller;


import com.northcoders.bandit.model.Favourites;
import com.google.firebase.auth.FirebaseAuth;
import com.northcoders.bandit.mapper.ProfileRequestDTOMapper;
import com.northcoders.bandit.mapper.ProfileResponseDTOMapper;
import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.model.ProfileRequestDTO;
import com.northcoders.bandit.model.ProfileResponseDTO;
import com.northcoders.bandit.service.ProfileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/profiles")
public class ProfileManagerController {

    @Autowired
    ProfileManagerService profileManagerService;

    @GetMapping
    public ResponseEntity<ArrayList<Profile>> getAllProfiles(){

        return new ResponseEntity<>(profileManagerService.getAllProfiles(), HttpStatus.OK);
    }

    //Current logged in user mappings:
    @PostMapping
    public ResponseEntity<ProfileResponseDTO> postProfile(@RequestBody ProfileRequestDTO profileRequestDTO){
        Profile profile = ProfileRequestDTOMapper.DTOToProfile(profileRequestDTO); //the profile will contain the id.

        return new ResponseEntity<>(ProfileResponseDTOMapper.profileToDTO(profileManagerService.postProfile(profile)), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ProfileResponseDTO> putProfile(@RequestBody ProfileRequestDTO profileRequestDTO){

        Profile profile = ProfileRequestDTOMapper.DTOToProfile(profileRequestDTO);

        return new ResponseEntity<>(ProfileResponseDTOMapper.profileToDTO(profileManagerService.updateProfile(profile)), HttpStatus.OK);

    }

    @GetMapping("/favourites/{favProfileId}")
    public ResponseEntity<List<Profile>> getListOfFavProfile(@PathVariable(name = "favProfileId") String favProfileId) {
        List<Profile> profileList = profileManagerService.getListOfFavProfile(favProfileId);

        return new ResponseEntity<>(profileList, HttpStatus.OK);
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


    public ResponseEntity<List<Profile>> getUserFavourites(List<Favourites> favourites){
        return new ResponseEntity<>(profileManagerService.getUserFavourites(favourites), HttpStatus.OK);
    }

    //TODO discuss name scheme for filtered profiles (e.g. the recommended profiles based on service layer algorithm)
    //I have kept no request param as filtering will take place using the user's firebase id entirely in backend service layer
    @GetMapping("/filtered")
    public ResponseEntity<ArrayList<Profile>> getFilteredProfiles(){

        return new ResponseEntity<>(profileManagerService.getFilteredProfiles(), HttpStatus.OK);
    }

}
