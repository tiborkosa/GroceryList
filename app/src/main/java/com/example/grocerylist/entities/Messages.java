package com.example.grocerylist.entities;

import java.util.Date;

public final class Messages {

    private String id;
    private String ownerId;
    private String otherOwnerId;
    private Date sentDate;

    public Messages() {}

    public Messages(String id, String ownerId, String otherOwnerId, Date sentDate) {
        this.id = id;
        this.ownerId = ownerId;
        this.otherOwnerId = otherOwnerId;
        this.sentDate = sentDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOtherOwnerId() {
        return otherOwnerId;
    }

    public void setOtherOwnerId(String otherOwnerId) {
        this.otherOwnerId = otherOwnerId;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "id='" + id + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", otherOwnerId='" + otherOwnerId + '\'' +
                ", sentDate=" + sentDate +
                '}';
    }
}
