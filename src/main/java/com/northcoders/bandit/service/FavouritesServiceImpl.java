package com.northcoders.bandit.service;

import com.northcoders.bandit.dto.request.AddToFavouriteRequestDTO;
import com.northcoders.bandit.exception.InvalidDTOException;
import com.northcoders.bandit.model.Favourites;
import com.northcoders.bandit.model.LikedOrDisliked;
import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.repository.FavouritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class FavouritesServiceImpl implements FavouritesService {

    @Autowired
    private final FavouritesRepository favouritesRepository;

    @Autowired
    private ProfileManagerService profileManagerService;

    @Autowired
    public FavouritesServiceImpl (FavouritesRepository favouritesRepository){
        this.favouritesRepository = favouritesRepository;
    }


    @Override
    public List<Favourites> getUserFavourites(@PathVariable String id) {
        return favouritesRepository.findById(id).stream().toList();
        //return all the favourites of the people making the call
    }

    @Override
    public Favourites addFavourite(AddToFavouriteRequestDTO requestDTO){
        String favProfileId = requestDTO.getFavProfileId();
        String yrFavProfileId = requestDTO.getYrFavProfileId();

        Profile profile = profileManagerService.findById(favProfileId);

        if (!profileManagerService.existsByProfileId(yrFavProfileId)) {
            throw new InvalidDTOException(String.format("Your Profile id %s not found", yrFavProfileId));
        }

        Favourites accountToSave = new Favourites();
        accountToSave.setFavProfileId(favProfileId);
        accountToSave.setYrFavProfileId(yrFavProfileId);
        accountToSave.setIsLikedOrDisliked(LikedOrDisliked.DEFAULT);
        accountToSave.setProfile(profile);

        return favouritesRepository.save(accountToSave);
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
