package com.pi.gymapp.api.models;

import com.google.gson.annotations.Expose;

public class User {

    @Expose private int id;

    @Expose private String username;

    @Expose private String fullName;

    @Expose private String gender;

    @Expose private long birthdate;

    @Expose private String email;

    @Expose private String phone;

    @Expose private String avatarUrl;

    @Expose private long dateCreated;

    @Expose private long dateLastActive;

    @Expose private boolean deleted;

    @Expose private boolean verified;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(long birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getDateLastActive() {
        return dateLastActive;
    }

    public void setDateLastActive(long dateLastActive) {
        this.dateLastActive = dateLastActive;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

}