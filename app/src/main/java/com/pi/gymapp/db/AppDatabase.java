package com.pi.gymapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.pi.gymapp.db.dao.CategoryDao;
import com.pi.gymapp.db.dao.CycleDao;
import com.pi.gymapp.db.dao.ExerciseDao;
import com.pi.gymapp.db.dao.RoutineDao;
import com.pi.gymapp.db.dao.UserDao;
import com.pi.gymapp.db.entity.CategoryEntity;
import com.pi.gymapp.db.entity.CycleEntity;
import com.pi.gymapp.db.entity.ExerciseEntity;
import com.pi.gymapp.db.entity.RoutineEntity;
import com.pi.gymapp.db.entity.UserEntity;

@Database(entities = {UserEntity.class, RoutineEntity.class, CycleEntity.class,
        ExerciseEntity.class, CategoryEntity.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract RoutineDao routineDao();

    public abstract CycleDao cycleDao();

    public abstract ExerciseDao exerciseDao();

    public abstract CategoryDao categoryDao();

}