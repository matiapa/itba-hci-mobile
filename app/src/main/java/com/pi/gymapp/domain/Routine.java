package com.pi.gymapp.domain;

public class Routine {

    private int id;

    private String name;
    private String detail;

    private double rate;
    private String difficulty;

    private int categoryId;
    private String categoryName;

    private long dateCreated;
    private Boolean isFav;


    public Routine(int id, String name, String detail, double rate, String difficulty,
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