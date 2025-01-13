package com.northcoders.bandit;

import com.northcoders.bandit.repository.FavouritesRepository;
import com.northcoders.bandit.service.FavouritesService;
import com.northcoders.bandit.service.FavouritesServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class FavouriteServiceTests {

    @Mock
    private FavouritesRepository repository;

    @InjectMocks
    private FavouritesServiceImpl favouritesService;



}
