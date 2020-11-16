package com.pi.gymapp.data;

public class RoutineEntity {

    public String title;
    public double duration;
    public double rate;
    public boolean isFav;

    public RoutineEntity(String title, double duration, double rate, boolean isFav) {
        this.title = title;
        this.duration = duration;
        this.rate = rate;
        this.isFav = isFav;
    }
}
