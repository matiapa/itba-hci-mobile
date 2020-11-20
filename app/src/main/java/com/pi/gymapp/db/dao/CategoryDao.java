package com.pi.gymapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pi.gymapp.db.entity.CategoryEntity;
import com.pi.gymapp.db.entity.CycleEntity;
import com.pi.gymapp.db.entity.RoutineEntity;

import java.util.List;

@Dao
public interface CategoryDao {
    // Getters

    @Query("SELECT * FROM CategoryEntity")
    LiveData<List<CategoryEntity>> getAll();

    // Inserts

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<CategoryEntity> cycles);

    // Deletes

    @Query("DELETE FROM CategoryEntity")
    void deleteAll();

}