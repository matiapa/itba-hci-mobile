package com.pi.gymapp.domain;

public class Routine {

    private int id;

    private String name;
    private String detail;

    private double rate;
    private String difficulty;

    private int categoryId;
    private String categoryName;

    private Boolean isFav;

    public Routine(int id, String name, String detail, double rate, String difficulty,
                   int categoryId, String categoryName, Boolean isFav) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.rate = rate;
        this.difficulty = difficulty;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
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

    public Boolean isFav() {
        return isFav;
    }

}