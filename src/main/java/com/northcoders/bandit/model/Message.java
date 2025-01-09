package com.northcoders.bandit.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Long id;

    //Do we need something to say that senderId and receiverId are foreign keys? They aren't really foreign keys in our PostgreSQL database, because they are the primary keys from the Firebase database of users
    @Column(name = "sender_id", updatable = false, nullable = false)
    private Long senderId;

    @Column(name = "receiver_id", updatable = false, nullable = false)
    private Long receiverId;

    @Column(name = "message_body")
    private String messageBody;

    //Is instant the best date time data type to use?
    @Column(name = "created_date")
    private Instant createdDate;

    public Message() {
    }

    public Message(Long id, Long senderId, Long receiverId, String messageBody, Instant createdDate) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageBody = messageBody;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
