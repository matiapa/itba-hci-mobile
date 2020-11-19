package com.pi.gymapp.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity (primaryKeys = {"id","order"})
public class ExerciseEntity {


//    @PrimaryKey public int id;
    @ColumnInfo(name = "id") public int id;
    @ColumnInfo(name = "routineId") public int routineId;
    @ColumnInfo(name = "cycleId") public int cycleId;
    @ColumnInfo(name = "name") public String name;
    @ColumnInfo(name = "detail") public String detail;
    @ColumnInfo(name = "duration") public int duration;
    @ColumnInfo(name = "repetitions") public int repetitions;
    @ColumnInfo(name = "order") public int order;


    public ExerciseEntity(int id, int routineId, int cycleId, String name, String detail, int duration, int repetitions, int order) {
        this.id = id;
        this.routineId = routineId;
        this.cycleId = cycleId;
        this.name = name;
        this.detail = detail;
        this.duration = duration;
        this.repetitions = repetitions;
        this.order = order;
    }
}
