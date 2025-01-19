package com.northcoders.bandit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.northcoders.bandit.mapper.ProfileResponseDTOMapper;
import com.northcoders.bandit.model.*;
import com.northcoders.bandit.repository.GenreManagerRepository;
import com.northcoders.bandit.repository.InstrumentManagerRepository;
import com.northcoders.bandit.repository.ProfileManagerRepository;
import com.northcoders.bandit.repository.SearchPreferenceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProfileManagerServiceImpl implements ProfileManagerService {

    @Autowired
    GenreManagerRepository genreManagerRepository;

    @Autowired
    InstrumentManagerRepository instrumentManagerRepository;

    @Autowired
    ProfileManagerRepository profileManagerRepository;

    @Autowired
    private SearchPreferenceRepository searchPreferenceRepository;

    @Autowired
    private UserInContextService userInContextService;

    @Autowired
    private OpenAIService openAIService;


    @Override
    public Profile postProfile(Profile profile, String searchQuery) {
        System.out.println(profile.toString());
        Optional<Profile> byfirebaseId = profileManagerRepository.findByfirebaseId(profile.getFirebaseId());
        Profile createdProfile = byfirebaseId.orElseGet(() -> profileManagerRepository.save(profile));
        updateProfileSearchVector(createdProfile);// vector of descriptions and other text fields
        createSearchPreference(createdProfile, searchQuery);
        return createdProfile;
    }

    private void createSearchPreference(Profile createdProfile, String searchQuery) {
        if (searchQuery == null || searchQuery.isBlank()) {
            searchQuery = createdProfile.getCity() + " " + createdProfile.getCountry();
            if (createdProfile.getGenres() != null && !createdProfile.getGenres().isEmpty()) {
                String genres = String.join(" ", createdProfile.getGenres().stream().map(Genre::getGenre)
                        .toList());
                searchQuery = searchQuery + " " + genres;
                searchQuery = searchQuery.toLowerCase();
            }
        }
        SearchPreference searchPreference = new SearchPreference();
        searchPreference.setProfile(createdProfile);
        searchPreference.setProfileId(createdProfile.getProfile_id());
        searchPreference.setSearchQuery(searchQuery);
        createdProfile.setSearchPreference(searchPreference);
        SearchPreference savedSearchPreference = searchPreferenceRepository.save(searchPreference);
        String tsQuery = openAIService.buildTsQuery(searchQuery);
        searchPreferenceRepository.updateTsQueryByProfileId(savedSearchPreference.getProfileId(), tsQuery);
    }

    private void updateProfileSearchVector(Profile profile) {
        String profileDesc = profile.getProfile_tags() == null ? profile.getDescription() : profile.getDescription() + " " + profile.getProfile_tags();
        List<String> tokens = openAIService.tokenize(profileDesc);
        if (profile.getCountry() != null) {
            tokens.add(profile.getCountry().toLowerCase());
        }
        if (profile.getCity() != null) {
            tokens.add(profile.getCity().toLowerCase());
        }
        if (profile.getGenres() != null && !profile.getGenres().isEmpty()) {
            String joinGenre = String.join(" ", profile.getGenres().stream()
                    .map(genre -> genre.getGenre().toLowerCase()).toList());
            tokens.add(joinGenre);
        }
        if (profile.getInstruments() != null && !profile.getInstruments().isEmpty()) {
            String joinInstruments = String.join(" ", profile.getInstruments().stream()
                    .map(instrument -> instrument.getInstrument().toLowerCase()).toList());
            tokens.add(joinInstruments);
        }
        String tokenString = String.join(" ", tokens);
        profileManagerRepository.updateSearchVector(profile.getProfile_id(), tokenString);
    }

    @Override
    public ArrayList<ProfileResponseDTO> getAllProfiles() {
        //Peter - I have converted between Iterator and ArrayList here in Service Layer
        ArrayList<ProfileResponseDTO> profiles = new ArrayList<>();
        profileManagerRepository.findAll()
                .forEach(profile -> profiles.add(ProfileResponseDTOMapper.profileToDTO(profile)));
        return profiles;
    }

    @Override
    public Profile updateProfile(Profile profile, String searchQuery) {

        //not sure if this is necessary but have added anyway in case - merges the new and existing if any fields in new are null.
        Optional<Profile> existingOpt = profileManagerRepository.findById(profile.getProfile_id());
        if (existingOpt.isPresent()) {
            Profile existing = existingOpt.get();
            if (profile.getProfile_type() == null) {
                profile.setProfile_type(existing.getProfile_type());
            }
            if (profile.getDescription() == null) {
                profile.setDescription(existing.getDescription());
            }
            if (profile.getImg_url() == null) {
                profile.setImg_url(existing.getImg_url());
            }
            if (profile.getLat() == 0) {
                profile.setLon(existing.getLon());
            }
            if (profile.getLon() == 0) {
                profile.setLon(existing.getLon());
            }
            if (profile.getMax_distance() == 0) {
                profile.setMax_distance(existing.getMax_distance());
            }
            if (profile.getGenres() == null) {
                profile.setGenres(existing.getGenres());
            }
            if (profile.getInstruments() == null) {
                profile.setInstruments(existing.getInstruments());
            }
        }

        Profile savedProfile = profileManagerRepository.save(profile);
        updateProfileSearchVector(savedProfile);

        Optional<SearchPreference> searchPreference = searchPreferenceRepository.findById(savedProfile.getProfile_id());

        if (searchQuery != null) {
            if (searchPreference.isEmpty()) {
                createSearchPreference(savedProfile, searchQuery);
            } else if (!searchQuery.equalsIgnoreCase(searchPreference.get().getSearchQuery())) {
                updateSearchPreference(searchPreference.get(), searchQuery);
            }
        }
        return savedProfile;
    }

    private void updateSearchPreference(SearchPreference searchPreference, String searchQuery) {
        searchPreference.setSearchQuery(searchQuery);
        SearchPreference savedSearchPreference = searchPreferenceRepository.save(searchPreference);
        String tsQuery = openAIService.buildTsQuery(searchQuery);
        searchPreferenceRepository.updateTsQueryByProfileId(savedSearchPreference.getProfileId(), tsQuery);
    }

    @Override
    @Transactional
    public boolean deleteById(String id) {
        Optional<Profile> profileOptional = profileManagerRepository.findByfirebaseId(id);
        if (profileOptional.isPresent()) {
            profileManagerRepository.deleteByfirebaseId(id);
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Profile> getFilteredProfiles() {

        //Profile currentUser = getCurrentUser();

        //get current logged-in user's profile, and perform operations to return relevant other profiles for user to swipe.

        //TODO firebase implementation in here, placeholder code to provide 5 profiles below:
        ArrayList<Profile> filtered = new ArrayList<>();
//
//        Set<Genre> genres = new HashSet<>();
//        Set<Instrument> instruments = new HashSet<>();
//
//        genres.add(new Genre("ROCK", null));
//        instruments.add(new Instrument("BASS", null));
//
//        for (int i = 0; i < 5; i++) {
//            Profile profile = new Profile("test" + i, "ABCD123X", "Meishan", "test url", ProfileType.MUSICIAN,
//                    "test description", 0f, 0f, 100f, "London", "UK", "Casual Advanced", genres, instruments);
//            genres.forEach(genre -> {
//                        Set<Profile> genreProfiles = new HashSet<>();
//                        genreProfiles.add(profile);
//                        genre.setProfiles(genreProfiles);
//                    }
//            );
//            instruments.forEach(instrument -> {
//                Set<Profile> instProfiles = new HashSet<>();
//                instProfiles.add(profile);
//                instrument.setProfiles(instProfiles);
//            });
//            filtered.add(profile);
//        }


        return filtered;

    }

    @Override
    public Optional<Profile> getUserProfile() {
        return getCurrentUser();
    }

    private Optional<Profile> getCurrentUser() {
        String currentUserId = userInContextService.getcurrentUser()
                .getUserId(); //TODO get user id from firebase instance
        Optional<Profile> currentUserOptional = profileManagerRepository.findByfirebaseId(userInContextService.getcurrentUser()
                .getUserId());
        return currentUserOptional;
    }

}
