package com.pi.gymapp.domain;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum RoutineDifficulties {
    ROOKIE("Rookie", "rookie"),

    BEGINNER("Beginner", "beginner"),

    INTERMEDIATE("Intermediate", "intermediate"),

    ADVANCED("Advanced", "advanced"),

    EXPERT("Expert", "expert");


    private String friendlyName, apiName;

    RoutineDifficulties(String name, String fieldName) {
        this.friendlyName = name;
        this.apiName = fieldName;
    }


    public String getFriendlyName() {
        return friendlyName;
    }

    public String getApiName() {
        return apiName;
    }
}
