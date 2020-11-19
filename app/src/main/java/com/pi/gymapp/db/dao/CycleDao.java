package com.pi.gymapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pi.gymapp.db.entity.CycleEntity;
import com.pi.gymapp.db.entity.ExerciseEntity;

import java.util.List;

@Dao
public interface CycleDao {
    // Getters

    @Query("SELECT * FROM CycleEntity WHERE routineId = :routineId AND id = :cycleId")
    LiveData<CycleEntity> getCycle(int routineId, int cycleId);

    @Query("SELECT * FROM CycleEntity WHERE routineId = :routineId")
    LiveData<List<CycleEntity>> getCycles(int routineId);

    // Inserts

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CycleEntity... cycle);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<CycleEntity> cycles);

    // Deletes

    @Query("DELETE FROM CycleEntity WHERE routineId = :routineId")
    void deleteCycles(int routineId);

}
