package com.northcoders.bandit.repository;

import com.northcoders.bandit.model.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
    List<Message> findBySenderIdAndReceiverId(Long SenderId, Long ReceiverId);
}
