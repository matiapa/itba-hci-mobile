package com.pi.gymapp.api.models;

import com.google.gson.annotations.Expose;

public class ReviewModel {

    @Expose
    private String review;

    @Expose
    private int score;

    public ReviewModel(String review, int score) {
        this.review = review;
        this.score = score;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
