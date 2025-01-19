package com.northcoders.bandit.repository;

import com.northcoders.bandit.model.Profile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfileManagementSearchRepositoryImpl implements ProfileManagementSearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Profile> findProfilesWithRank(String profileId) {
        String nativeQuery = """
            SELECT *,
              ts_rank(p.profile_search_vector,
                              (SELECT search_query_ts FROM profile WHERE profile_id = :profileId)) AS profileRank
               FROM profile p
               ORDER BY profileRank desc LIMIT 20
            """;

        Query query = entityManager.createNativeQuery(nativeQuery, Profile.class);
        query.setParameter("profileId", profileId);

        return query.getResultList();
    }

    @Override
    public List<Profile> findProfilesWithRankByQuery(String searchQuery) {
        String nativeQuery = """
            SELECT *,
                     ts_rank(profile_search_vector, to_tsquery('simple', :searchQuery)) AS profileRank
              FROM profile
              ORDER BY profileRank desc
              LIMIT 20
            """;

        Query query = entityManager.createNativeQuery(nativeQuery, Profile.class);
        query.setParameter("searchQuery", searchQuery);

        return query.getResultList();
    }

    @Override
    public List<Profile> findProfilesLimit() {
        String nativeQuery = """
            SELECT *
               FROM profile p LIMIT 20
            """;

        Query query = entityManager.createNativeQuery(nativeQuery, Profile.class);
        return query.getResultList();
    }
}
