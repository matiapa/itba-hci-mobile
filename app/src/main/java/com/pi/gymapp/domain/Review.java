package com.pi.gymapp.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.pi.gymapp.R;

public class Review implements Comparable<Review> {

    private int id;
    private long date;
    private int score;
    private String review;
    private int routineId;

    public Review(int id, long date, int score, String review, int routineId) {
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

    public int getRoutine() {
        return routineId;
    }


    @Override
    public int compareTo(Review o) {
        return Long.compare(o.date, date);
    }

}
