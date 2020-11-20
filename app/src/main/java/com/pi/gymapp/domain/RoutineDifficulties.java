package com.pi.gymapp.domain;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum RoutineDifficulties {
    ROOKIE("Rookie", "rookie", routines -> {
        routines.removeIf(r -> ! r.getDifficulty().equals("rookie"));
        return null;
    }),

    BEGINNER("Beginner", "beginner", routines -> {
        routines.removeIf(r -> ! r.getDifficulty().equals("beginner"));
        return null;
    }),

    INTERMEDIATE("Intermediate", "intermediate", routines -> {
        routines.removeIf(r -> ! r.getDifficulty().equals("intermediate"));
        return null;
    }),

    ADVANCED("Advanced", "advanced", routines -> {
        routines.removeIf(r -> ! r.getDifficulty().equals("advanced"));
        return null;
    }),

    EXPERT("Expert", "expert", routines -> {
        routines.removeIf(r -> ! r.getDifficulty().equals("expert"));
        return null;
    });


    private String friendlyName, apiName;
    private Function<List<Routine>, Void> filter;

    RoutineDifficulties(String name, String fieldName, Function<List<Routine>, Void> filter) {
        this.friendlyName = name;
        this.apiName = fieldName;
        this.filter = filter;
    }


    public String getFriendlyName() {
        return friendlyName;
    }

    public String getApiName() {
        return apiName;
    }

    public Function<List<Routine>, Void> getFilter() {
        return filter;
    }
}
