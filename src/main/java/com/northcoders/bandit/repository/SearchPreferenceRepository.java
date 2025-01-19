package com.northcoders.bandit.repository;

import com.northcoders.bandit.model.SearchPreference;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchPreferenceRepository extends JpaRepository<SearchPreference, String> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE search_preference " +
            "SET search_query_ts = to_tsquery('simple', :tsquery) " +
            "WHERE profile_id = :profileId",
            nativeQuery = true)
    void updateTsQueryByProfileId(String profileId, String tsquery);

    @Transactional
    SearchPreference save(SearchPreference searchPreference);

}