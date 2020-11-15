package com.pi.gymapp.api.models;

import com.google.gson.annotations.Expose;

public class Creator {

    @Expose private int id;
    
    @Expose private String username;
    
    @Expose private String gender;
    
    @Expose private String avatarUrl;
    
    @Expose private int dateCreated;
    
    @Expose private int dateLastActive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(int dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getDateLastActive() {
        return dateLastActive;
    }

    public void setDateLastActive(int dateLastActive) {
        this.dateLastActive = dateLastActive;
    }

}