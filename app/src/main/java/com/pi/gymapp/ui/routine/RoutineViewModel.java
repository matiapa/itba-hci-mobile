package com.pi.gymapp.ui.routine;

import androidx.lifecycle.LiveData;

import com.pi.gymapp.api.models.ReviewModel;
import com.pi.gymapp.api.utils.ApiResponse;
import com.pi.gymapp.domain.Review;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.repo.RoutineRepository;
import com.pi.gymapp.utils.Resource;
import com.pi.gymapp.utils.RepositoryViewModel;

import java.util.List;

public class RoutineViewModel extends RepositoryViewModel<RoutineRepository> {

    private final static int PAGE_SIZE = 10;

    public RoutineViewModel(RoutineRepository repository) {
        super(repository);

        favourites = repository.getFavourites();

    }


    // ----------------------------- List of routines -----------------------------

    public LiveData<Resource<List<Routine>>> getRoutines(String orderBy, String direction, String difficulty) {
        return repository.getAllRoutines(orderBy, direction, difficulty);
    }

    public LiveData<Resource<List<Routine>>> getRoutines(String orderBy, String direction) {
        return repository.getAllRoutines(orderBy, direction);
    }

    private final LiveData<Resource<List<Routine>>> favourites;

    public LiveData<Resource<List<Routine>>> getFavourites() {
        return favourites;
    }


    // ----------------------------- Selected routine -----------------------------

    public LiveData<Resource<Routine>> getRoutine(int routineId) {
        return repository.getRoutineById(routineId);
    }

    public LiveData<Boolean> fetchIsFav(int routineId){
        return repository.fetchIsFav(routineId);
    }

    // ----------------------------- Routine updaters -----------------------------

    public LiveData<ApiResponse<Void>> setFav(int routineId, boolean value){
        return repository.setFav(routineId, value);
    }
    //-------------------------------- Routine Reviews -----------------------------

    public LiveData<Resource<List<Review>>> getReviews(int routineId) {
        return repository.getReviews(routineId);
    }

    public LiveData<Resource<Review>> postReview(int routineId, ReviewModel review) {
        return repository.postReview(routineId, review);
    }

    // ----------------------------- List of paged routines -----------------------------

    /*private int routinesPage = 0;
    private boolean isLastRoutinesPage = false;

    private final List<Routine> loadedRoutines = new ArrayList<>();
    private final MediatorLiveData<Resource<List<Routine>>> routines = new MediatorLiveData<>();

    public LiveData<Resource<List<Routine>>> getRoutines() {
        getMoreRoutines();
        return routines;
    }

    public void getMoreRoutines() {
        if (isLastRoutinesPage)
            return;

        routines.addSource(repository.getRoutineSlice(routinesPage, PAGE_SIZE), resource -> {
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

    public void resetRoutinesList(){
        loadedRoutines.clear();
        routinesPage = 0;
    }*/
}
