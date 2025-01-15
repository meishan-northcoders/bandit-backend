package com.northcoders.bandit.service;

import com.northcoders.bandit.model.Favourites;
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
    public FavouritesServiceImpl (FavouritesRepository favouritesRepository){
        this.favouritesRepository = favouritesRepository;
    }


    @Override
    public List<Favourites> getUserFavourites(@PathVariable String id) {
        return favouritesRepository.findById(id).stream().toList();
        //return all the favourites of the people making the call
    }

    @Override
    public Favourites addFavourite(String id){
        Favourites accountToSave = favouritesRepository.findById(id).get();
        favouritesRepository.save(accountToSave);
        return accountToSave;
    }

    @Override
    public void removeFavouriteById(String id){
        favouritesRepository.deleteById(id);
    }

    @Override
    public boolean areMutuallyFavourited(String uid1, String uid2) {
        return false;
    }




}
