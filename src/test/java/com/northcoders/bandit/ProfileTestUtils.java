package com.northcoders.bandit;

import com.northcoders.bandit.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ProfileTestUtils {

    public static Profile createTestProfile(long id){
        Profile test = new Profile();
        test.setProfile_id(id);
        test.setProfile_type(ProfileType.MUSICIAN);
        test.setDescription("test");
        test.setImg_url("testurl");
        test.setLat(100f);
        test.setLon(100f);
        test.setMax_distance(100f);

        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(1, "Rock", null));
        test.setGenres(genres);
        Set<Instrument> instruments = new HashSet<>();
        instruments.add(new Instrument(1, "Bass", null));
        test.setInstruments(instruments);
        return test;
    }

    public static ArrayList<Profile> createTestProfileArray(){
        Profile profile = createTestProfile(1L);
        return new ArrayList<>();
    }
}
