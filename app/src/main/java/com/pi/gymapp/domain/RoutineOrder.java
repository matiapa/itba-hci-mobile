package com.pi.gymapp.domain;

import java.util.Comparator;

public enum RoutineOrder {

    RATE("Rating", "averageRating",
            (o1, o2) -> Double.compare(o2.getRate(), o1.getRate())),

    DATE_CREATED("Date created","dateCreated",
            (o1, o2) -> Long.compare(o2.getDateCreated(), o1.getDateCreated()));


    private String friendlyName, apiName;
    private Comparator<Routine> comparator;

    RoutineOrder(String friendlyName, String fieldName, Comparator<Routine> comparator){
        this.friendlyName = friendlyName;
        this.apiName = fieldName;
        this.comparator = comparator;
    }


    public String getFriendlyName() {
        return friendlyName;
    }

    public String getApiName() {
        return apiName;
    }

    public Comparator<Routine> getComparator() {
        return comparator;
    }
}
