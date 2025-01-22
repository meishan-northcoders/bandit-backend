package com.northcoders.bandit.service;

import com.northcoders.bandit.dto.request.AddToFavouriteRequestDTO;
import com.northcoders.bandit.exception.InvalidDTOException;
import com.northcoders.bandit.model.Favourites;
import com.northcoders.bandit.model.LikedOrDisliked;
import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.repository.FavouritesRepository;
import com.northcoders.bandit.repository.ProfileManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class FavouritesServiceImpl implements FavouritesService {

    // Linked to favourites repository and profile repository
    @Autowired
    private final FavouritesRepository favouritesRepository;

    @Autowired
    private ProfileManagerService profileManagerService;

    @Autowired
    private UserInContextService userInContextService;
    @Autowired
    private ProfileManagerRepository profileManagerRepository;

    @Autowired
    public FavouritesServiceImpl (FavouritesRepository favouritesRepository){
        this.favouritesRepository = favouritesRepository;
    }

    @Override
    public List<Favourites> getAllFavourites(){
        List<Favourites> foundFavourites = new ArrayList<>();
        favouritesRepository.findAll().forEach(foundFavourites::add);
        return foundFavourites;
    }


    @Override
    public List<Favourites> getUserFavourites() {

        Profile currentUser = profileManagerService.getUserProfile().get();


        return favouritesRepository.findByYrFavProfileId(currentUser.getProfile_id());
        //return all the favourites of the people making the call
    }


    // User swipes right; Add profile to favourites
    @Override
    public Favourites addFavourite(AddToFavouriteRequestDTO requestDTO){
        //String favProfileId = requestDTO.getFavProfileId();
        String userId = userInContextService.getcurrentUser().getUserId();
        Profile currUserProfile = profileManagerRepository.findByfirebaseId(userId).orElseThrow();
        String currUserProfileId = currUserProfile.getProfile_id();
        String likedFavProfileId = requestDTO.getYrFavProfileId();

        //check likedFavProfileId isn't already liked by currUserProfileId
        List<Favourites> currentUserFavourites = getUserFavourites();
        AtomicBoolean hasLiked = new AtomicBoolean(false);
        currentUserFavourites.forEach(favourite -> {
            if(Objects.equals(favourite.getFavProfileId(), likedFavProfileId)){
                //user has already liked this user, therefore we DON'T want to add them to the favourites
                 hasLiked.set(true);
            }
        });

        if(!hasLiked.get()){
            if (!profileManagerService.existsByProfileId(likedFavProfileId)) {
                throw new InvalidDTOException(String.format("Profile id you liked %s not found", likedFavProfileId));
            }
            Profile likedProfile = profileManagerRepository.findById(likedFavProfileId).orElseThrow();


            Favourites accountToSave = new Favourites();
            accountToSave.setFavProfileId(likedFavProfileId);
            accountToSave.setYrFavProfileId(currUserProfileId);
            accountToSave.setIsLikedOrDisliked(LikedOrDisliked.LIKE);
            accountToSave.setProfile(likedProfile);

            return favouritesRepository.save(accountToSave);
        }
        return null;

    }

    @Override
    public void removeFavourite(AddToFavouriteRequestDTO requestDTO) {
        String favProfileId = requestDTO.getFavProfileId();
        String yrFavProfileId = requestDTO.getYrFavProfileId();

        Favourites favourites = favouritesRepository.findByFavProfileIdAndYrFavProfileId(favProfileId, yrFavProfileId);
        if (favourites == null) {
            throw new InvalidDTOException(String.format("Favourites record not found. %s and %s", favProfileId, yrFavProfileId));
        }

        favouritesRepository.delete(favourites);
    }

    // Nice to have; will refactor after MVP is met;
    @Override
    public boolean areMutuallyFavourited(String uid1, String uid2) {
        return false;
    }

    @Override
    public List<Favourites> getYrFavouritesProfileByFavProfileId(String favProfileId) {
        List<Favourites> favouritesList = favouritesRepository.findAllByFavProfileId(favProfileId);

        return favouritesList;
    }
}
