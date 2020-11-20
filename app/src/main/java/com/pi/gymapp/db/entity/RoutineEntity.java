package com.pi.gymapp.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RoutineEntity {

    @PrimaryKey private int id;
    @ColumnInfo(name = "name") private String name;
    @ColumnInfo(name = "detail") private String detail;

    @ColumnInfo(name = "rate") private double rate;
    @ColumnInfo(name = "difficulty")  private String difficulty;

    @ColumnInfo(name = "categoryId") private int categoryId;
    @ColumnInfo(name = "categoryName") private String categoryName;

    @ColumnInfo(name = "dateCreated") private long dateCreated;
    @ColumnInfo(name = "isFav") private Boolean isFav;

    public RoutineEntity(int id, String name, String detail, double rate, String difficulty,
                         int categoryId, String categoryName, long dateCreated, Boolean isFav) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.rate = rate;
        this.difficulty = difficulty;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.dateCreated = dateCreated;
        this.isFav = isFav;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public double getRate() {
        return rate;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public Boolean isFav() {
        return isFav;
    }

}