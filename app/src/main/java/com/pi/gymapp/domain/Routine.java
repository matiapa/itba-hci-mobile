package com.pi.gymapp.domain;

public class Routine {

    private int id;
    private String title;
    private double rate;
    private Boolean isFav;

    public Routine(int id, String title, double rate, Boolean isFav) {
        this.id = id;
        this.title = title;
        this.rate = rate;
        this.isFav = isFav;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getRate() {
        return rate;
    }

    public Boolean isFav() {
        return isFav;
    }

}