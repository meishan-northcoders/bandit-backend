package com.northcoders.bandit.service;

import com.northcoders.bandit.mapper.ProfileResponseDTOMapper;
import com.northcoders.bandit.model.Genre;
import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.model.ProfileRankDTO;
import com.northcoders.bandit.model.ProfileResponseDTO;
import com.northcoders.bandit.repository.GenreManagerRepository;
import com.northcoders.bandit.repository.InstrumentManagerRepository;
import com.northcoders.bandit.repository.ProfileManagerRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfileManagerServiceImpl implements ProfileManagerService {

    @Autowired
    GenreManagerRepository genreManagerRepository;

    @Autowired
    InstrumentManagerRepository instrumentManagerRepository;

    @Autowired
    ProfileManagerRepository profileManagerRepository;

    @Autowired
    private UserInContextService userInContextService;

    @Autowired
    private OpenAIService openAIService;




    @Override
    public Profile postProfile(Profile profile) {
        System.out.println(profile.toString());
        Optional<Profile> byfirebaseId = profileManagerRepository.findByfirebaseId(profile.getFirebaseId());
        if (byfirebaseId.isPresent()) {
            throw new EntityExistsException("Active profile already exists for the current user");
        }
        Profile createdProfile = byfirebaseId.orElseGet(() -> profileManagerRepository.save(profile));
        updateProfileSearchVector(createdProfile);// vector of descriptions and other text fields
        createSearchPreferenceQuery(createdProfile, profile.getSearch_query());
        return createdProfile;
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
    public Profile updateProfile(Profile profile) {

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
                profile.setLat(existing.getLat());
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
            if (profile.getSearch_query() == null) {
                profile.setSearch_query(existing.getSearch_query());
            }
        }

        Profile savedProfile = profileManagerRepository.save(profile);
        updateProfileSearchVector(savedProfile);

        if (profile.getSearch_query() == null) {
            createSearchPreferenceQuery(savedProfile, savedProfile.getSearch_query());
        } else if (!profile.getSearch_query().equalsIgnoreCase(savedProfile.getSearch_query())) {
            updateSearchPreferenceQuery(savedProfile, profile.getSearch_query());
        }
        return savedProfile;
    }

    private void createSearchPreferenceQuery(Profile createdProfile, String searchQuery) {
        if (searchQuery == null || searchQuery.isBlank()) {
            searchQuery = createdProfile.getCity() + " " + createdProfile.getCountry();
            if (createdProfile.getGenres() != null && !createdProfile.getGenres().isEmpty()) {
                String genres = String.join(" ", createdProfile.getGenres().stream().map(Genre::getGenre)
                        .toList());
                searchQuery = searchQuery + " " + genres;
                searchQuery = searchQuery.toLowerCase();
            }
        }
        updateSearchPreferenceQuery(createdProfile, searchQuery);
    }

    private void updateSearchPreferenceQuery(Profile dbProfile, String searchQuery) {
        String tsQuery = openAIService.buildTsQuery(searchQuery);
        profileManagerRepository.updateSearchQuery(dbProfile.getProfile_id(), searchQuery, tsQuery);
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
    public List<Profile> getFilteredProfiles() {
        //Profile currentUser = getCurrentUser();
        //get current logged-in user's profile, and perform operations to return relevant other profiles for user to swipe.
        Profile userProfile = getUserProfile().orElseThrow();

        if (userProfile.getSearch_query() == null) {
            return profileManagerRepository.findProfilesLimit(userProfile.getProfile_id());
        }


        List<ProfileRankDTO> profilesWithRank = profileManagerRepository.findProfilesWithRank(userProfile.getProfile_id());
        Optional<Float> maxRankOpt = profilesWithRank.stream().max(Comparator.comparing(ProfileRankDTO::getProfileRank))
                .map(ProfileRankDTO::getProfileRank);


        if (profilesWithRank.isEmpty()) {
            //run alt query
            profilesWithRank = profileManagerRepository.findProfilesWithRankByOr(userProfile.getProfile_id());
        } else {
            Float maxRank = maxRankOpt.orElseThrow();
            if (maxRank.compareTo(Float.valueOf("0.00001")) < 0) {
                //run alt query
                profilesWithRank = profileManagerRepository.findProfilesWithRankByOr(userProfile.getProfile_id());
            }
        }

        Map<String, Float> profileRankMap = profilesWithRank.stream().collect(Collectors.toMap(ProfileRankDTO::getProfileId, ProfileRankDTO::getProfileRank));

        List<Profile> profilesBy = profileManagerRepository.findProfilesBy(profileRankMap.keySet()).stream()
                .map(profile -> {
                    profile.setProfileRank(profileRankMap.get(profile.getProfile_id()));
                    return profile;
                }).sorted(Comparator.comparing(Profile::getProfileRank, Comparator.reverseOrder())).toList();
        if (profilesBy.isEmpty()) {
            return profileManagerRepository.findProfilesLimit(userProfile.getProfile_id());

        }
        return profilesBy;
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
