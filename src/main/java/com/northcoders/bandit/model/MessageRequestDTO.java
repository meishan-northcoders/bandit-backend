package com.northcoders.bandit.model;

public class MessageRequestDTO {

    //Sender id taken from context i.e. is the current user
    private String receiverId;
    private String messageBody;

    public MessageRequestDTO() {
    }

    public MessageRequestDTO(String receiverId, String messageBody) {
        this.receiverId = receiverId;
        this.messageBody = messageBody;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
