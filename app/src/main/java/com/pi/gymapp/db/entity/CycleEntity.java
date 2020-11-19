package com.pi.gymapp.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class CycleEntity {

    @PrimaryKey public int id;
    @ColumnInfo(name = "routineId") private int routineId;

    @ColumnInfo(name = "name") private String name;
    @ColumnInfo(name = "detail") private String detail;

    @ColumnInfo(name = "repetitions") private int repetitions;
    @ColumnInfo(name = "order") private int order;

    @ColumnInfo(name = "type") private String type;

    public CycleEntity(int id, int routineId, String name, String detail, int repetitions,
                       int order, String type) {
        this.id = id;
        this.routineId = routineId;
        this.name = name;
        this.detail = detail;
        this.repetitions = repetitions;
        this.order = order;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public int getRoutineId() {
        return routineId;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public int getOrder() {
        return order;
    }

    public String getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoutineId(int routineId) {
        this.routineId = routineId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setType(String type) {
        this.type = type;
    }

}
