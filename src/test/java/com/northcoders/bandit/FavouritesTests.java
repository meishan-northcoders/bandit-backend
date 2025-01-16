package com.northcoders.bandit;

import com.northcoders.bandit.controller.FavouritesController;
import com.northcoders.bandit.model.Favourite;
import com.northcoders.bandit.repository.FavouritesRepository;
import com.northcoders.bandit.service.FavouritesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class FavouritesTests {

    @Mock
    private FavouritesRepository repository;

    @InjectMocks
    private FavouritesServiceImpl favouritesService;
    @InjectMocks
    FavouritesController favouritesController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup(){

        mockMvc = MockMvcBuilders.standaloneSetup(favouritesController).build();
    }

//    @Test
//    @DisplayName("getUserFavourites returns all of the specified favourites when given valid list")
//    public void getAllFavourites(){
//        //Arrange
//        List<Favourite> favouriteList = new ArrayList<>();
//        favouriteList.add(new Favourite("4sdytfjhgfrt6uyhgfrtyh", "7ygfr56ygfrde3edfgyui8uygf"));
//        favouriteList.add(new Favourite("4hhgjkjnhjnhbjjnhh", "4rtyfo76fo7689yp89b09"));
//        favouriteList.add(new Favourite("4hhgjkjnhjnhbjjnhh", "87uivgopotyhbgyhjbv"));
//        favouriteList.add(new Favourite("4hhgjkjnhjnhbjjnhh", "p98uygvcftyhgvfhbvgh5"));
//        favouriteList.add(new Favourite("4sdytfjhgfrt6uyhgfrtyh", "f98y87gvt76oft9786gv"));
//        favouriteList.add(new Favourite("4sdytfjhgfrt6uyhgfrtyh", "ftfi87i8ydcdrz43sdytv"));
//    }
}
