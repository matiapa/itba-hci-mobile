package com.pi.gymapp.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserEntity {
    @PrimaryKey public int id;
    @ColumnInfo(name = "username") public String username;
    @ColumnInfo(name = "fullName") public String fullName;
    @ColumnInfo(name = "gender") public String gender;
    @ColumnInfo(name = "birthdate") public long birthdate;
    @ColumnInfo(name = "email") public String email;
    @ColumnInfo(name = "phone") public long phone;
    @ColumnInfo(name = "avatarUrl") public String avatarUrl;
    @ColumnInfo(name = "dateCreated") public long dateCreated;
    @ColumnInfo(name = "dateLastActive") public long dateLastActive;
    @ColumnInfo(name = "deleted") public boolean deleted;
    @ColumnInfo(name = "verified") public boolean verified;


    public UserEntity(int id, String username, String fullName, String gender, long birthdate, String email, long phone, String avatarUrl, long dateCreated, long dateLastActive, boolean deleted, boolean verified) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.gender = gender;
        this.birthdate = birthdate;
        this.email = email;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.dateCreated = dateCreated;
        this.dateLastActive = dateLastActive;
        this.deleted = deleted;
        this.verified = verified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutineEntity that = (RoutineEntity) o;
        return id == that.getId();
    }
}
