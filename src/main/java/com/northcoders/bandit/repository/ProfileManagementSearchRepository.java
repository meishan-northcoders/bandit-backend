package com.northcoders.bandit.repository;

import com.northcoders.bandit.model.Profile;

import java.util.List;

public interface ProfileManagementSearchRepository {
    List<Profile> findProfilesWithRank(String searchQuery);

    List<Profile> findProfilesWithRankByQuery(String searchQuery);

    List<Profile> findProfilesLimit();
}
