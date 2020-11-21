package com.pi.gymapp.domain;

import java.util.Comparator;

public enum RoutineOrder {

    RATE("averageRating", (o1, o2) -> Double.compare(o2.getRate(), o1.getRate())),

    DATE_CREATED("dateCreated", (o1, o2) -> Long.compare(o2.getDateCreated(), o1.getDateCreated()));


    private String apiName;
    private Comparator<Routine> comparator;

    RoutineOrder(String fieldName, Comparator<Routine> comparator){
        this.apiName = fieldName;
        this.comparator = comparator;
    }


    public String getApiName() {
        return apiName;
    }

    public Comparator<Routine> getComparator() {
        return comparator;
    }
}
