package com.pi.gymapp.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RoutineEntity {

    @PrimaryKey public int id;
    @ColumnInfo(name = "title") public String title;
    @ColumnInfo(name = "rate") public double rate;
    @ColumnInfo(name = "isFav") public boolean isFav;

    public RoutineEntity(int id, String title, double rate, boolean isFav) {
        this.id = id;
        this.title = title;
        this.rate = rate;
        this.isFav = isFav;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutineEntity that = (RoutineEntity) o;
        return id == that.id;
    }

}