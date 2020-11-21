package com.pi.gymapp.domain;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum RoutineDifficulties {
    ROOKIE( "rookie"),

    BEGINNER( "beginner"),

    INTERMEDIATE( "intermediate"),

    ADVANCED( "advanced"),

    EXPERT( "expert");


    private String apiName;

    RoutineDifficulties(String fieldName) {
        this.apiName = fieldName;
    }


    public String getApiName() {
        return apiName;
    }
}
