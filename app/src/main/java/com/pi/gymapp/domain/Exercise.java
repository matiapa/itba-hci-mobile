package com.pi.gymapp.domain;


public class Exercise implements Comparable<Exercise>{

    public int id;
    private int routineId;
    private int cycleId;
    private String name;
    private String detail;
    private int duration;
    private int repetitions;
    private int order;

    public Exercise(int id, int routineId, int cycleId, String name, String detail, int duration, int repetitions, int order) {
        this.id = id;
        this.routineId = routineId;
        this.cycleId = cycleId;
        this.name = name;
        this.detail = detail;
        this.duration = duration;
        this.repetitions = repetitions;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoutineId() {
        return routineId;
    }

    public void setRoutineId(int routineId) {
        this.routineId = routineId;
    }

    public int getCycleId() {
        return cycleId;
    }

    public void setCycleId(int cycleId) {
        this.cycleId = cycleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int compareTo(Exercise o) {
        return Integer.compare(getOrder(),o.getOrder());
    }
}
