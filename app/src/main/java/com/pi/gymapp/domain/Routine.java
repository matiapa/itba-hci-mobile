package com.pi.gymapp.domain;

public class Routine {

    private int id;
    private String title;
    private double rate;
    private boolean isFav;

    public Routine(int id, String title, double rate, boolean isFav) {
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

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

}