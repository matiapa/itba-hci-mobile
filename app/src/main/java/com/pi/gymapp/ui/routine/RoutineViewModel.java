package com.pi.gymapp.ui.routine;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.repo.RoutineRepository;
import com.pi.gymapp.utils.AbsentLiveData;
import com.pi.gymapp.utils.Resource;
import com.pi.gymapp.utils.Status;
import com.pi.gymapp.utils.RepositoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class RoutineViewModel extends RepositoryViewModel<RoutineRepository> {
    
    private final static int PAGE_SIZE = 10;

    private int routinesPage = 0;
    private boolean isLastRoutinesPage = false;
    
    private final List<Routine> loadedRoutines = new ArrayList<>();
    private final MediatorLiveData<Resource<List<Routine>>> routines = new MediatorLiveData<>();

    private final MutableLiveData<Integer> routineId = new MutableLiveData<>();
    private final LiveData<Resource<Routine>> routine;

    public RoutineViewModel(RoutineRepository repository) {
        super(repository);

        routine = Transformations.switchMap(routineId, routineId -> {
            if (routineId == null) {
                return AbsentLiveData.create();
            } else {
                return repository.getById(routineId);
            }
        });
    }

    public LiveData<Resource<List<Routine>>> getRoutines() {
        getMoreRoutines();
        return routines;
    }

    public void getMoreRoutines() {
        if (isLastRoutinesPage)
            return;

       routines.addSource(repository.getSlice(routinesPage, PAGE_SIZE), resource -> {
            if (resource.status == Status.SUCCESS) {
                if ((resource.data.size() == 0) || (resource.data.size() < PAGE_SIZE))
                    isLastRoutinesPage = true;

                routinesPage++;

                loadedRoutines.addAll(resource.data);
                routines.setValue(Resource.success(loadedRoutines));
            } else if (resource.status == Status.LOADING) {
                routines.setValue(resource);
            }
        });
    }

    public LiveData<Resource<Routine>> getRoutine() {
        return routine;
    }

    public void setRoutineId(int routineId) {
        if ((this.routineId.getValue() != null) &&
                (routineId == this.routineId.getValue())) {
            return;
        }

        this.routineId.setValue(routineId);
    }
}
