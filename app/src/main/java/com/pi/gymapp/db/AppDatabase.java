package com.pi.gymapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.pi.gymapp.db.dao.ExerciseDao;
import com.pi.gymapp.db.dao.RoutineDao;
import com.pi.gymapp.db.dao.UserDao;
import com.pi.gymapp.db.entity.ExerciseEntity;
import com.pi.gymapp.db.entity.RoutineEntity;
import com.pi.gymapp.db.entity.UserEntity;

@Database(entities = {RoutineEntity.class, UserEntity.class, ExerciseEntity.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {

    public abstract RoutineDao routineDao();

    public abstract UserDao userDao();

    public abstract ExerciseDao exerciseDao();

}