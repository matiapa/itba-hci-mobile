package com.pi.gymapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pi.gymapp.db.entity.RoutineEntity;

import java.util.List;

@Dao
public interface RoutineDao {
    // Getters

    @Query("SELECT * FROM RoutineEntity WHERE id = :id")
    LiveData<RoutineEntity> getById(int id);

    @Query("SELECT * FROM RoutineEntity LIMIT :limit OFFSET :offset")
    LiveData<List<RoutineEntity>> getPage(int limit, int offset);

    @Query("SELECT * FROM RoutineEntity WHERE isFav")
    LiveData<List<RoutineEntity>> getFavs();

    // Inserts

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RoutineEntity... routine);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<RoutineEntity> routines);

    // Deletes

    @Query("DELETE FROM RoutineEntity WHERE id = :id")
    void deleteById(int id);

    @Query("DELETE FROM RoutineEntity WHERE id IN (SELECT id FROM RoutineEntity LIMIT :limit OFFSET :offset)")
    void deleteSlice(int limit, int offset);

    @Query("DELETE FROM RoutineEntity WHERE isFav")
    void deleteFavs();
}