package com.pi.gymapp.ui.cycle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.pi.gymapp.domain.Cycle;
import com.pi.gymapp.domain.Cycle;
import com.pi.gymapp.repo.CycleRepository;
import com.pi.gymapp.utils.AbsentLiveData;
import com.pi.gymapp.utils.RepositoryViewModel;
import com.pi.gymapp.utils.Resource;

import java.util.List;

public class CycleViewModel extends RepositoryViewModel<CycleRepository> {

    private final static int PAGE_SIZE = 10;
    
    private int routineId;

    public CycleViewModel(CycleRepository repository) {
        super(repository);

        cycle = Transformations.switchMap(cycleId, cycleId -> {
            if (cycleId == null) {
                return AbsentLiveData.create();
            } else {
                return repository.getCycleById(routineId, cycleId);
            }
        });

    }

    public void setRoutineId(int routineId){
        this.routineId = routineId;
        cycles = repository.getCycles(routineId);
    }


    // ----------------------------- List of cycles -----------------------------

    private LiveData<Resource<List<Cycle>>> cycles;

    public LiveData<Resource<List<Cycle>>> getCycles() {
        return cycles;
    }


    // ----------------------------- Selected cycle -----------------------------

    private final MutableLiveData<Integer> cycleId = new MutableLiveData<>();
    private final LiveData<Resource<Cycle>> cycle;

    public LiveData<Resource<Cycle>> getCycle() {
        return cycle;
    }

    public void setCycleId(int cycleId) {
        if ((this.cycleId.getValue() != null) &&
                (cycleId == this.cycleId.getValue())) {
            return;
        }

        this.cycleId.setValue(cycleId);
    }

}