package com.northcoders.bandit.repository;

import com.northcoders.bandit.model.Profile;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileManagerRepository extends CrudRepository<Profile, String> {
    public Optional<Profile> findByfirebaseId(String firebaseId);

    public void deleteByfirebaseId(String firebaseId);


    @Modifying
    @Transactional
    @Query(value = "UPDATE profile SET profile_search_vector = to_tsvector('simple', :tokenString) where profile_id = :profileId",
            nativeQuery = true)
    void updateSearchVector(String profileId, String tokenString);

}
