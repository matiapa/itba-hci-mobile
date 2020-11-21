package com.pi.gymapp.api.models;

import com.google.gson.annotations.Expose;

public class ReviewModel {

    @Expose
    private String review;

    @Expose
    private double score;

    public ReviewModel(String review, double score) {
        this.review = review;
        this.score = score;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
