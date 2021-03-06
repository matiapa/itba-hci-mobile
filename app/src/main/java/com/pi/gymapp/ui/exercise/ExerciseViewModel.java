package com.pi.gymapp.ui.exercise;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.pi.gymapp.domain.Exercise;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.repo.ExerciseRepository;
import com.pi.gymapp.repo.RoutineRepository;
import com.pi.gymapp.utils.AbsentLiveData;
import com.pi.gymapp.utils.RepositoryViewModel;
import com.pi.gymapp.utils.Resource;
import com.pi.gymapp.utils.Status;

import java.util.ArrayList;
import java.util.List;

public class ExerciseViewModel extends RepositoryViewModel<ExerciseRepository> {

    private final static int PAGE_SIZE = 10;

    private Integer routineId =null;
    private Integer cycleId= null;

    private LiveData<Resource<List<Exercise>>> exercises;

    private final MutableLiveData<Integer> exerciseId = new MutableLiveData<>();
    private final LiveData<Resource<Exercise>> exercise;


    public ExerciseViewModel(ExerciseRepository repository) {
        super(repository);

        exercise = Transformations.switchMap(exerciseId,  (exerciseId) -> {
            if (routineId == null || cycleId == null || exerciseId==null) {
                return AbsentLiveData.create();
            } else {
                return repository.getExerciseById(routineId,cycleId,exerciseId);
            }
        });
    }

    // ----------------------------- List of all exercises -----------------------------

    public LiveData<Resource<List<Exercise>>> getExercises() {
        return exercises;
    }

    public LiveData<Resource<List<Exercise>>> getExercises(int cycleId) {
        return repository.getAll(routineId, cycleId);
    }

    public void setRoutineId(int routineId) {
        if (this.routineId != null && routineId == this.routineId)
            return;

        this.routineId = routineId;
    }

    public void setCycleId(int cycleId) {
        if ((this.cycleId != null && cycleId == this.cycleId) || routineId == null)
            return;

        this.cycleId = cycleId;

        exercises = repository.getAll(routineId, cycleId);
    }


    // ----------------------------- Selected routine -----------------------------

    public LiveData<Resource<Exercise>> getExercise() {
        return exercise;
    }

    public void setExerciseId(int exerciseId) {
        if ((this.exerciseId.getValue() != null) &&
                (exerciseId == this.exerciseId.getValue())) {
            return;
        }

        this.exerciseId.setValue(exerciseId);
    }


}
