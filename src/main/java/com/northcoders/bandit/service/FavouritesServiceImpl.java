package com.northcoders.bandit.service;

import com.northcoders.bandit.model.Favourites;
import com.northcoders.bandit.repository.FavouritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavouritesServiceImpl implements FavouritesService {

    private final FavouritesRepository favouritesRepository;

    @Autowired
    public FavouritesServiceImpl (FavouritesRepository favouritesRepository){
        this.favouritesRepository = favouritesRepository;
    }


    @Override
    public List<Favourites> getUserFavourites(Long id) {
        return favouritesRepository.findById(id).stream().toList();
        //return all the favourites of the people making the call
    }

    @Override
    public ResponseEntity<Favourites> addFavourite(Long id){
        Optional<Favourites> accountToSave = favouritesRepository.findById(id);
        return null;
        //Add User found by Id to List of favourites of the person making the call
    }

    @Override
    public void removeFavouriteById(Long id){
        favouritesRepository.deleteById(id);
    }

    @Override
    public boolean areMutuallyFavourited(String uid1, String uid2) {
        return false;
    }


}
