package com.northcoders.bandit.repository;

import com.northcoders.bandit.model.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, String> {
    List<Message> findBySenderIdAndReceiverId(String SenderId, String ReceiverId);
}
