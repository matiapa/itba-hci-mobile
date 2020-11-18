package com.pi.gymapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pi.gymapp.db.entity.RoutineEntity;

import java.util.List;

@Dao
public interface RoutineDao {
    // Getters

    @Query("SELECT * FROM routineentity WHERE id = :id")
    LiveData<RoutineEntity> getById(int id);

    @Query("SELECT * FROM routineentity")
    LiveData<List<RoutineEntity>> getAll();

    @Query("SELECT * FROM routineentity LIMIT :limit OFFSET :offset")
    LiveData<List<RoutineEntity>> getSlice(int limit, int offset);

    @Query("SELECT * FROM routineentity WHERE isFav")
    LiveData<List<RoutineEntity>> getFav();

    // Inserts

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RoutineEntity... sport);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<RoutineEntity> sports);

    // Updaters

    @Update
    void update(RoutineEntity... routines);

    // Deletes

    @Query("DELETE FROM routineentity")
    void deleteAll();

    @Query("DELETE FROM routineentity WHERE id IN (SELECT id FROM routineentity LIMIT :limit OFFSET :offset)")
    void deleteSlice(int limit, int offset);
}