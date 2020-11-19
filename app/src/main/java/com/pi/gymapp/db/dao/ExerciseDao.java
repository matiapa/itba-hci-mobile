package com.pi.gymapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pi.gymapp.db.entity.ExerciseEntity;


import java.util.List;

@Dao
public interface ExerciseDao {
    // Getters

    @Query("SELECT * FROM ExerciseEntity WHERE (id = :id AND routineId = :routineId AND cycleId = :cycleId)")
    LiveData<ExerciseEntity> getById(int id,int routineId,int cycleId);

    @Query("SELECT * FROM ExerciseEntity WHERE (routineId = :routineId AND cycleId = :cycleId) LIMIT :limit OFFSET :offset")
    LiveData<List<ExerciseEntity>> getPage(int routineId,int cycleId,int limit, int offset);

    @Query("SELECT * FROM ExerciseEntity WHERE (routineId = :routineId AND cycleId = :cycleId) ")
    LiveData<List<ExerciseEntity>> getAll(int routineId,int cycleId);


    // Inserts

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ExerciseEntity... exercise);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<ExerciseEntity> exercises);

    // Deletes

    @Query("DELETE FROM ExerciseEntity WHERE (id = :id AND routineId = :routineId AND cycleId = :cycleId)")
    void deleteById(int id,int routineId,int cycleId);

    @Query("DELETE FROM ExerciseEntity WHERE id IN (SELECT id FROM ExerciseEntity WHERE (routineId = :routineId AND cycleId = :cycleId) LIMIT :limit OFFSET :offset)")
    void deleteSlice(int routineId,int cycleId,int limit, int offset);

    @Query("DELETE FROM ExerciseEntity WHERE (routineId = :routineId AND cycleId = :cycleId)")
    void deleteAll(int routineId,int cycleId);


}
