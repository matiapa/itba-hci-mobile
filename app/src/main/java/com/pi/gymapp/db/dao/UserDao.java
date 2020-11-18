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

    @Query("SELECT * FROM userentity WHERE id = :id")
    LiveData<UserEntity> getById(int id);

    @Query("SELECT * FROM userentity")
    LiveData<List<UserEntity>> getAll();

    @Query("SELECT * FROM userentity LIMIT :limit OFFSET :offset")
    LiveData<List<UserEntity>> getSlice(int limit, int offset);



    // Inserts

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserEntity... sport);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<UserEntity> sports);

    // Updaters

    @Update
    void update(UserEntity... user);

    // Deletes

    @Query("DELETE FROM userentity")
    void deleteAll();

    @Query("DELETE FROM userentity WHERE id IN (SELECT id FROM userentity LIMIT :limit OFFSET :offset)")
    void deleteSlice(int limit, int offset);
}