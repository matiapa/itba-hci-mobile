package com.pi.gymapp.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.pi.gymapp.api.models.RoutineModel;

@Entity
public class ReviewEntity {

    @PrimaryKey private int id;
    @ColumnInfo (name = "date") private long date;
    @ColumnInfo (name = "score") private int score;
    @ColumnInfo (name = "review") private String review;
    @ColumnInfo (name = "routine") private int routineId;

    public ReviewEntity(int id, long date, int score, String review, int routineId) {
        this.id = id;
        this.date = date;
        this.score = score;
        this.review = review;
        this.routineId = routineId;
    }

    public int getId() {
        return id;
    }

    public long getDate() {
        return date;
    }

    public int getScore() {
        return score;
    }

    public String getReview() {
        return review;
    }

    public int getRoutineId() {
        return routineId;
    }
}
