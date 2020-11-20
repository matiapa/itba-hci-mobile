package com.pi.gymapp.ui.routine;

import com.pi.gymapp.domain.Routine;

import java.util.Comparator;

public enum RoutineOrder {

    RATE("averageRating",
            (o1, o2) -> Double.compare(o2.getRate(), o1.getRate())),

    DATE_CREATED("dateCreated",
            (o1, o2) -> Long.compare(o2.getDateCreated(), o1.getDateCreated()));

    RoutineOrder(String fieldName, Comparator<Routine> comparator){
        this.fieldName = fieldName;
        this.comparator = comparator;
    }

    private String fieldName;
    private Comparator<Routine> comparator;

    public String getFieldName() {
        return fieldName;
    }

    public Comparator<Routine> getComparator() {
        return comparator;
    }
}
