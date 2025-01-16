package com.northcoders.bandit.service;

import com.northcoders.bandit.model.Favourite;
import com.northcoders.bandit.repository.FavouritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavouritesServiceImpl implements FavouritesService {

    @Autowired
    private final FavouritesRepository favouritesRepository;

    @Autowired
    public FavouritesServiceImpl (FavouritesRepository favouritesRepository){
        this.favouritesRepository = favouritesRepository;
    }

    @Override
    public List<Favourite> getAllFavourites(){
        List<Favourite> foundFavourites = new ArrayList<>();
        favouritesRepository.findAll().forEach(foundFavourites::add);
        return foundFavourites;
    }


    @Override
    public List<Favourite> getUserFavourites(@PathVariable String id) {
        return favouritesRepository.findByFavProfileId(id).stream().toList();
        //return all the favourites of the people making the call
    }

    @Override
    public Favourite addFavourite(String yrFavProfileId ){
        String favProfileId = "test0"; // hardcoded, must delete for final build
        Favourite existsFavourite = favouritesRepository.findByFavProfileIdAndYrFavProfileId(favProfileId, yrFavProfileId);
        if(existsFavourite == null){
            Favourite favouriteToAdd = new Favourite();
            favouriteToAdd.setFavProfileId(favProfileId);
            favouriteToAdd.setYrFavProfileId(yrFavProfileId);
            return favouritesRepository.save(favouriteToAdd);
        }
        return existsFavourite;
    }

    @Override
    public void removeFavouriteById(String id){
        favouritesRepository.deleteByFavProfileId(id);
    }

    // Nice to have; will refactor after MVP is met;
    @Override
    public boolean areMutuallyFavourited(String uid1, String uid2) {
        return false;
    }




}
