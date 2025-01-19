package com.northcoders.bandit.model;

public class CorrespondentRequestDTO {
    private String correspondentId;

    public CorrespondentRequestDTO(String correspondentId) {
        this.correspondentId = correspondentId;
    }

    public CorrespondentRequestDTO() {
    }

    public String getCorrespondentId() {
        return correspondentId;
    }

    public void setCorrespondentId(String correspondentId) {
        this.correspondentId = correspondentId;
    }
}
