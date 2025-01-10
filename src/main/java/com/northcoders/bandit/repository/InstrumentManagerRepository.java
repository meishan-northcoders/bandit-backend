package com.northcoders.bandit.repository;

import com.northcoders.bandit.model.Instrument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentManagerRepository extends CrudRepository<Instrument, Long> {
}
