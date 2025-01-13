package com.northcoders.bandit.repository;

import com.northcoders.bandit.model.InstType;
import com.northcoders.bandit.model.Instrument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstrumentManagerRepository extends CrudRepository<Instrument, Long> {

    Optional<Instrument> findByInstType(String instType);
}
