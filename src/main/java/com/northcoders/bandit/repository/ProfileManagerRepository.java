package com.northcoders.bandit.repository;

import com.northcoders.bandit.model.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProfileManagerRepository extends CrudRepository<Profile, String> {
    @Query(nativeQuery = true, value = "select * from profile p where p.profile_id in (?1)")
    List<Profile> findByProfileIdIn(Set<String> profileIds);
}
