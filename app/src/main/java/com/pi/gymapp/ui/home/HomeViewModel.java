
package com.pi.gymapp.ui.home;

import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
//
//    private final LiveData<List<RoutineEntity>> allRoutines;
//    private final LiveData<List<RoutineEntity>> favRoutines;
//
//    private RoutineRepo routineRepo;
//
//    public HomeViewModel(Application app, RoutineRepo routineRepo) {
//        this.routineRepo = routineRepo;
//
//        allRoutines = routineRepo.getAll();
//        favRoutines = routineRepo.getFav();
//    }
//
//    // Data getters
//
//    public LiveData<List<RoutineEntity>> getAllRoutines() {
//        return allRoutines;
//    }
//
//    public LiveData<List<RoutineEntity>> getFavRoutines() {
//        return favRoutines;
//    }
//
//    // Data fetchers
//
//    public void fetchAllRoutines(){
//        routineRepo.fetchAll();
//    }
//
//    public void fetchFavRoutines(){
//        routineRepo.fetchFavs();
//    }
//
//    // Interaction methods
//
//    public void favRoutine(RoutineEntity routine){
//        routineRepo.favRoutine(routine);
//    }
//
//    public void unfavRoutine(RoutineEntity routine){
//        routineRepo.unfavRoutine(routine);
//    }
//
//    // ViewModel factory
//
//    public static class Factory implements ViewModelProvider.Factory {
//        private Application app;
//        private static HomeViewModel viewModel;
//
//        Factory(Application app){
//            this.app = app;
//        }
//
//        @NonNull
//        @Override
//        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//            if(viewModel == null){
//                viewModel = new HomeViewModel(app, new RoutineRepo(app));
//            }
//            return (T) viewModel;
//        }
//    }
//

}