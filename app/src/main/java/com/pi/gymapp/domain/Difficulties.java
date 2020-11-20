package com.pi.gymapp.domain;

public enum Difficulties {
    BEGINNER("Beginner", "beginner"),
    INTERMEDIATE("Intermediate", "intermediate"),
    HARD("Hard", "hard"),
    EXPERT("Expert", "expert");

    private String name, fieldName;

    Difficulties(String name, String fieldName) {
        this.name = name;
        this.fieldName = fieldName;
    }

    public String getName() {
        return name;
    }

    public String getFieldName() {
        return fieldName;
    }
}
