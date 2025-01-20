package com.northcoders.bandit.service;

import com.northcoders.bandit.mapper.ProfileRequestDTOMapper;
import com.northcoders.bandit.mapper.ProfileResponseDTOMapper;
import com.northcoders.bandit.model.*;
import com.northcoders.bandit.exception.InvalidDTOException;
import com.northcoders.bandit.model.*;
import com.northcoders.bandit.repository.GenreManagerRepository;
import com.northcoders.bandit.repository.InstrumentManagerRepository;
import com.northcoders.bandit.repository.ProfileManagerRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
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

    @Lazy
    @Autowired
    private FavouritesService favouritesService;

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
        updateProfileSearchVector(createdProfile);// vector of descriptions and other text fields with number of times the word occurs
        createSearchPreferenceQuery(createdProfile, profile.getSearch_query());
        return createdProfile;
    }

    private void updateProfileSearchVector(Profile profile) {
        String profileDesc = profile.getProfile_tags() == null ? profile.getDescription() : profile.getDescription() + " " + profile.getProfile_tags();
        List<String> tokens = openAIService.tokenize(profileDesc); // create bag of words using english-tokens resource included in project and
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
        if(profile.getProfile_type() != null){
            tokens.add(profile.getProfile_type().name());
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
    public Profile updateProfile(ProfileRequestDTO profileRequestDTO) {
            String userId = userInContextService.getcurrentUser().getUserId();
            Profile existingProfile = profileManagerRepository.findByfirebaseId(userId).orElseThrow();

                if (profileRequestDTO.getProfile_type() != null ) {
                    existingProfile.setProfile_type(profileRequestDTO.getProfile_type());
                }
                if (profileRequestDTO.getDescription() != null && !profileRequestDTO.getDescription().isBlank()) {
                    existingProfile.setDescription(profileRequestDTO.getDescription());
                }
                if (profileRequestDTO.getImg_url() != null && !profileRequestDTO.getImg_url().isBlank()) {
                    existingProfile.setImg_url(profileRequestDTO.getImg_url());
                }
                if (profileRequestDTO.getLat() !=0 ) {
                    existingProfile.setLat(profileRequestDTO.getLat());
                }
                if (profileRequestDTO.getLon() != 0) {
                    existingProfile.setLon(profileRequestDTO.getLon());
                }
                if (profileRequestDTO.getMax_distance() != 0) {
                    existingProfile.setMax_distance(profileRequestDTO.getMax_distance());
                }
                if (profileRequestDTO.getGenres() != null) {
                    existingProfile.setGenres(profileRequestDTO.getGenres());
                }
                if (profileRequestDTO.getInstruments() != null) {
                    existingProfile.setInstruments(profileRequestDTO.getInstruments());
                }
                if (profileRequestDTO.getSearchQuery() != null && !profileRequestDTO.getSearchQuery().isBlank()) {
                    existingProfile.setSearch_query(profileRequestDTO.getSearchQuery());
                }
                if (profileRequestDTO.getCity() != null && !profileRequestDTO.getCity().isBlank()) {
                    existingProfile.setCity(profileRequestDTO.getCity());
                }
                if (profileRequestDTO.getCountry() != null && !profileRequestDTO.getCountry().isBlank()) {
                    existingProfile.setCountry(profileRequestDTO.getCountry());
                }

                Profile savedProfile = profileManagerRepository.save(existingProfile);
                updateProfileSearchVector(savedProfile);
                if (savedProfile.getSearch_query() == null) {
                    createSearchPreferenceQuery(savedProfile, savedProfile.getSearch_query());
                } else if (!profileRequestDTO.getSearchQuery().equalsIgnoreCase(savedProfile.getSearch_query())) {
                    updateSearchPreferenceQuery(savedProfile, profileRequestDTO.getSearchQuery());
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
        String tsQuery = openAIService.buildTsQuery(searchQuery); //chat GPT to return query constructed
        // to use POSTGRES TSQUERY ,eg: rock & drummer & ( manchester | london )
        profileManagerRepository.updateSearchQuery(dbProfile.getProfile_id(), searchQuery, tsQuery);
        // storing my search_query_ts using native query
    }

    @Override
    @Transactional
    public boolean deleteById(String id) {
        Optional<Profile> profileOptional = profileManagerRepository.findByfirebaseId(id);
        if (profileOptional.isPresent()) {
            profileOptional.get().getGenres().clear();
            profileOptional.get().getInstruments().clear();
            profileManagerRepository.save(profileOptional.get()); // Update the database to remove join table entries

            // Delete the profile
            profileManagerRepository.delete(profileOptional.get());
            return true;
        }
            return false;
    }

    @Override
    public List<Profile> getFilteredProfiles() {
        Profile userProfile = getUserProfile().orElseThrow();

        if (userProfile.getSearch_query() == null) {
            return profileManagerRepository.findProfilesLimit(userProfile.getProfile_id());  // max 20 records
        }
        List<ProfileRankDTO> profilesWithRank = profileManagerRepository.findProfilesWithRank(userProfile.getProfile_id());
        Optional<Float> maxRankOpt = profilesWithRank.stream()
                .max(Comparator.comparing(ProfileRankDTO::getProfileRank))
                .map(ProfileRankDTO::getProfileRank);

        if (profilesWithRank.isEmpty()) {   // fail safe , when for whatever reason we dont get data
            //run alt query
            profilesWithRank = profileManagerRepository.findProfilesWithRankByOr(userProfile.getProfile_id());  // instead of
            // query with & on search query keys use | this time
        } else {
            Float maxRank = maxRankOpt.orElseThrow();
            if (maxRank.compareTo(Float.valueOf("0.00001")) < 0) {
                //run alt query
                profilesWithRank = profileManagerRepository.findProfilesWithRankByOr(userProfile.getProfile_id()); // if rank
                // exists but  signifies less similarities
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
    public List<Profile> getUserFavourites(List<Favourites> favourites) {
        List<Profile> favouritesList = new ArrayList<>();
        for (Favourites f : favourites){
            favouritesList.add(profileManagerRepository.findById(f.getFavProfileId()).get());
        }
        return favouritesList;
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

    @Override
    public boolean existsByProfileId(String profileId) {
        return profileManagerRepository.existsById(profileId);
    }

    @Override
    public Profile findById(String profileId) {
        Optional<Profile> profileOptional = profileManagerRepository.findById(profileId);
        if (!profileOptional.isPresent()) {
            throw new InvalidDTOException(String.format("Profile id %s not found", profileId));
        }
        return profileOptional.get();
    }

    @Override
    public List<Profile> getListOfFavProfile(String favProfileId) {
        List<Favourites> favouritesList = favouritesService.getYrFavouritesProfileByFavProfileId(favProfileId);

        if (favouritesList != null && !favouritesList.isEmpty()) {
            Set<String> yrFavProfileIdSet = favouritesList.stream()
                    .map(Favourites::getYrFavProfileId)
                    .collect(Collectors.toSet());

            return profileManagerRepository.findByProfileIdIn(yrFavProfileIdSet);
        }

        return List.of();
    }
}
