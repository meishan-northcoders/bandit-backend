package com.northcoders.bandit.service;

import com.northcoders.bandit.model.Favourites;
import com.northcoders.bandit.repository.FavouritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return null;
    }

    @Override
    public Favourites addFavourite(Long id){
        Optional<Favourites> accountToSave = favouritesRepository.findById(id);
        return null;
    }

    @Override
    public void removeFavouriteById(Long id){
        favouritesRepository.deleteById(id);
    }
}
