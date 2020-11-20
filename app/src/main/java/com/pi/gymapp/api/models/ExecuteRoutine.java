package com.pi.gymapp.api.models;

public class ExecuteRoutine {
     private int duration;
     private boolean wasModified;

    public ExecuteRoutine(int duration, boolean wasModified) {
        this.duration = duration;
        this.wasModified = wasModified;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isWasModified() {
        return wasModified;
    }

    public void setWasModified(boolean wasModified) {
        this.wasModified = wasModified;
    }
}

