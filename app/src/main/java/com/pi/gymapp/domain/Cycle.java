package com.pi.gymapp.domain;

public class Cycle implements Comparable<Cycle>{

    private int id;
    private int routineId;

    private String name;
    private String detail;

    private int repetitions;
    private int order;

    private String type;

    public Cycle(int id, int routineId, String name, String detail, int repetitions,
                 int order, String categoryName) {
        this.id = id;
        this.routineId = routineId;
        this.name = name;
        this.detail = detail;
        this.repetitions = repetitions;
        this.order = order;
        this.type = categoryName;
    }

    public int getId() {
        return id;
    }

    public int getRoutineId() {
        return routineId;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public int getOrder() {
        return order;
    }

    public String getType() {
        return type;
    }

    @Override
    public int compareTo(Cycle o) {
        return Integer.compare(order, o.order);
    }
}