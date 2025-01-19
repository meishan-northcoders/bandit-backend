package com.northcoders.bandit.repository;

import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.model.ProfileRankDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileManagerRepository extends JpaRepository<Profile, String> {
    public Optional<Profile> findByfirebaseId(String firebaseId);

    public void deleteByfirebaseId(String firebaseId);


    @Modifying
    @Transactional
    @Query(value = "UPDATE profile SET profile_search_vector = to_tsvector('simple', :tokenString) where profile_id = :profileId",
            nativeQuery = true)
    void updateSearchVector(String profileId, String tokenString);



    @Modifying
    @Transactional
    @Query(value = "UPDATE profile " +
            "SET search_query_ts = to_tsquery('simple', :tsquery), search_query = :searchQuery " +
            "WHERE profile_id = :profileId",
            nativeQuery = true)
    void updateSearchQuery(String profileId, String searchQuery, String tsquery);


    @Query(value = """
             SELECT p.profile_id,
              ts_rank(p.profile_search_vector,
                              (SELECT search_query_ts FROM profile WHERE profile_id = :profileId)) AS profileRank
               FROM profile p
               ORDER BY profileRank desc LIMIT 20
            """, nativeQuery = true)
    List<ProfileRankDTO> findProfilesWithRank(@Param("profileId") String profileId);


    @Query(value = """
             SELECT p.profile_id,
                    ts_rank(p.profile_search_vector,
                            (SELECT regexp_replace(search_query_ts::text, '&', '|', 'g')::tsquery FROM profile WHERE profile_id = :profileId)) AS profileRank
             FROM profile p
             ORDER BY profileRank desc LIMIT 20
            """, nativeQuery = true)
    List<ProfileRankDTO> findProfilesWithRankByOr(@Param("profileId") String profileId);


    @Query(value = """
             SELECT *
               FROM profile p LIMIT 20
            """, nativeQuery = true)
    List<Profile> findProfilesLimit();


    @Query(value = """
             SELECT *
               FROM profile p where p.profile_id in (:profileIds)
            """, nativeQuery = true)
    List<Profile> findProfilesBy(List<String> profileIds);

}
