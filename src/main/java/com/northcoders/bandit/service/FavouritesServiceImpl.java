package com.northcoders.bandit.service;

import com.northcoders.bandit.model.Favourite;
import com.northcoders.bandit.repository.FavouritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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
    public List<Favourite> getUserFavourites(@PathVariable String id) {
        return favouritesRepository.findByFavProfileId(id).stream().toList();
        //return all the favourites of the people making the call
    }

    @Override
    public Favourite addFavourite(String id){
        Favourite accountToSave = favouritesRepository.findById(id).get();
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
