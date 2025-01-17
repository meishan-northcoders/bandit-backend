package com.northcoders.bandit.repository;

import com.northcoders.bandit.model.Profile;
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
}
