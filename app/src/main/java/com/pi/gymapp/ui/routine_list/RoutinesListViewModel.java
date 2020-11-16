package com.pi.gymapp.ui.routine_list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pi.gymapp.data.RoutineEntity;

import java.util.List;

public class RoutinesListViewModel extends ViewModel {

    private final MutableLiveData<List<RoutineEntity>> routines;

    public RoutinesListViewModel() {
        routines = new MutableLiveData<>();
    }

    public LiveData<List<RoutineEntity>> getRoutines() {
        return routines;
    }

}