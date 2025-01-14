package com.northcoders.bandit.repository;

import com.northcoders.bandit.model.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileManagerRepository extends CrudRepository<Profile, String> {
}
