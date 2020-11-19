package com.pi.gymapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pi.gymapp.db.entity.RoutineEntity;
import com.pi.gymapp.db.entity.UserEntity;

import java.util.List;
@Dao
public interface UserDao {
    // Getters

    @Query("SELECT * FROM userentity")
    LiveData<UserEntity> getCurrent();


    // Inserts

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserEntity... user);


    // Deletes

    @Query("DELETE FROM userentity")
    void delete();

}