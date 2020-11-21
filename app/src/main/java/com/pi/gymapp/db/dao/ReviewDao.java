package com.pi.gymapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pi.gymapp.db.entity.ReviewEntity;
import com.pi.gymapp.db.entity.RoutineEntity;

import java.util.List;

@Dao
public interface ReviewDao {
    // Getters

    @Query("SELECT * FROM ReviewEntity WHERE id = :id")
    LiveData<ReviewEntity> getById(int id);

    @Query("SELECT * FROM ReviewEntity WHERE routine = :routineId")
    LiveData<List<ReviewEntity>> getByRoutineId(int routineId);

    // Inserts

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ReviewEntity... routine);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<ReviewEntity> routines);

    // Deletes

    @Query("DELETE FROM ReviewEntity WHERE routine = :routineId")
    void deleteByRoutineId(int routineId);

}