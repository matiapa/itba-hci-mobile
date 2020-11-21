package com.pi.gymapp.api.models;

import com.google.gson.annotations.Expose;

public class FullReviewModel {
    @Expose
    private  int id;

    @Expose
    private long date;

    @Expose
    private double score;

    @Expose
    private String review;

    @Expose
    private RoutineModel routine;

    public FullReviewModel(int id, long date, double score, String review, RoutineModel routine) {
        this.id = id;
        this.date = date;
        this.score = score;
        this.review = review;
        this.routine = routine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public RoutineModel getRoutine() {
        return routine;
    }

    public void setRoutine(RoutineModel routine) {
        this.routine = routine;
    }
}
