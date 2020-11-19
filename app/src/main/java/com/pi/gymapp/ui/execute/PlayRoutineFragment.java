package com.pi.gymapp.ui.execute;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pi.gymapp.MyApplication;
import com.pi.gymapp.R;
import com.pi.gymapp.databinding.PlayRoutineBinding;
import com.pi.gymapp.databinding.RoutinesListAllBinding;
import com.pi.gymapp.domain.Cycle;
import com.pi.gymapp.domain.Exercise;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.repo.CycleRepository;
import com.pi.gymapp.repo.ExerciseRepository;
import com.pi.gymapp.repo.RoutineRepository;
import com.pi.gymapp.ui.MainActivity;
import com.pi.gymapp.ui.account.SignUpFragment2Args;
import com.pi.gymapp.ui.cycle.CycleViewModel;
import com.pi.gymapp.ui.exercise.ExerciseViewModel;
import com.pi.gymapp.ui.routine.RoutineViewModel;
import com.pi.gymapp.ui.routine.RoutinesListAdapter;
import com.pi.gymapp.utils.MainViewModel;
import com.pi.gymapp.utils.RepositoryViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Semaphore;

public class PlayRoutineFragment extends Fragment {

    private PlayRoutineBinding binding;

    private RoutineViewModel routineViewModel;
    private CycleViewModel cycleViewModel;
    private ExerciseViewModel exerciseViewModel;



    private int routineId;
    private Routine routine;
    private List<Cycle> cycles;
    private List<List<Exercise>> exercises;
    private Cycle cycle;
    private Exercise exercise;
    public long timeremaining=0;
//    private final Semaphore semaphore = new Semaphore(0);
    public boolean flag= true;


    private int cycleIndex=0;
    private int exerciseIndex=0;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = PlayRoutineBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MyApplication application = (MyApplication) getActivity().getApplication();
        MainActivity activity = (MainActivity) getActivity();


        // --------------------------------- Fill Lists ---------------------------------
        //routines

        ViewModelProvider.Factory viewModelFactory = new RepositoryViewModel.Factory<>(
                RoutineRepository.class, application.getRoutineRepository()
        );
        routineViewModel = new ViewModelProvider(this, viewModelFactory).get(RoutineViewModel.class);

        routineId=PlayRoutineFragmentArgs.fromBundle(getArguments()).getRoutineId();



        routineViewModel.setRoutineId(routineId);

        MyApplication app = (MyApplication) getActivity().getApplication();

//        app.getAppExecutors().diskIO().execute(() -> {
//                app.getAppExecutors().mainThread().execute(() -> {
                    routineViewModel.getRoutine().observe(getViewLifecycleOwner(), resource -> {
                        switch (resource.status) {
                            case LOADING:
                                activity.showProgressBar();
                                break;

                            case SUCCESS:
                                activity.hideProgressBar();
                                routine=resource.data;
                                flag=false;


                                break;
                        }
                    });





        //cycles
        viewModelFactory = new RepositoryViewModel.Factory<>(CycleRepository.class, application.getCycleRepository());
        cycleViewModel = new ViewModelProvider(this, viewModelFactory).get(CycleViewModel.class);
        cycleViewModel.setRoutineId(routineId);
        cycles.addAll(cycleViewModel.getCycles().getValue().data);

        //Exercises

        viewModelFactory = new RepositoryViewModel.Factory<>(ExerciseRepository.class, application.getExerciseRepository());
        exerciseViewModel = new ViewModelProvider(this,viewModelFactory).get(ExerciseViewModel.class);
        exerciseViewModel.setRoutineId(routineId);
        for (Cycle cycle:cycles) {

            exerciseViewModel.setCycleId(cycle.getId());
            List<Exercise> auxlist = new ArrayList<>(exerciseViewModel.getExercises().getValue().data);

            exercises.add(auxlist);
        }



        // --------------------------------- inflar el fragment ---------------------------------

        setup();

        //------------------------------------conectar los botones-------------------------------
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.getCountDownTimer().getStatus().observe(getViewLifecycleOwner(), countDownTimerStatus -> {
            if (countDownTimerStatus.isFinished()) {
//
                if (exercises.get(cycleIndex).size()<exerciseIndex)
                    exerciseIndex++;
                else
                    cycleIndex++;
                setup();
                viewModel.getCountDownTimer().start(exercise.getDuration()*1000,1000);
//
//                binding.countdown.setText(R.string.done);
//                binding.start.setEnabled(true);
//                binding.stop.setEnabled(false);
//                binding.pause.setEnabled(false);
//                binding.addTime.setEnabled(false);
            } else {
//                String secs= Integer.valueOf((int) ((millisUntilFinished/1000)%60)).toString();
//                String minutes = Integer.valueOf((int) ((millisUntilFinished/1000)/60)).toString();
//                String ans = minutes + " : " + secs ;
//                binding.playExerciseLeftRoutines.setText(ans);

//                binding.countdown.setText(String.valueOf(countDownTimerStatus.getRemainingTime()));
                binding.playExerciseLeftRoutines.setText(String.valueOf(countDownTimerStatus.getRemainingTime()));
            }
        });

        binding.playButton.setOnClickListener(v -> {
            if (viewModel.getCountDownTimer().isPaused()){
                if (viewModel.getCountDownTimer().isStarted())
                {
                    viewModel.getCountDownTimer().resume();
                }
                else {
                    viewModel.getCountDownTimer().start(exercise.getDuration()*1000,1000);
                }
            }else {
                viewModel.getCountDownTimer().pause();
            }
        });



    }
    public void setup(){
        StringBuilder stringBuilder = new StringBuilder();
        if (cycles.size()<cycleIndex && exercises.get(cycleIndex).size()<exerciseIndex){
            cycle=cycles.get(cycleIndex);
            exercise=exercises.get(cycleIndex).get(exerciseIndex);
            stringBuilder.append(cycle.getName());
            stringBuilder.append("\n");
            stringBuilder.append(cycle.getDetail());
            stringBuilder.append("\n");
            stringBuilder.append(exercise.getName());
            stringBuilder.append("\n");
            stringBuilder.append(exercise.getDetail());
        }
        binding.content.setText(stringBuilder.toString());





    }
}
