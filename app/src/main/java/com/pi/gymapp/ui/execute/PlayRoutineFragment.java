package com.pi.gymapp.ui.execute;

import android.app.Application;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pi.gymapp.MyApplication;
import com.pi.gymapp.R;
import com.pi.gymapp.databinding.PlayRoutineBinding;
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
import com.pi.gymapp.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayRoutineFragment extends Fragment {

    private PlayRoutineBinding binding;

    private RoutineViewModel routineViewModel;
    private CycleViewModel cycleViewModel;
    private ExerciseViewModel exerciseViewModel;

    private int routineId;
    private Routine routine;

    private List<Cycle> cycles = new ArrayList<>();
    private final Set<List<Exercise>> exercises = new TreeSet<>((o1, o2) -> o1.get(0).getCycleId()-o2.get(0).getCycleId());
    private List<List<Exercise>> exerciseslist = new ArrayList<>();

    private Cycle cycle;
    private Exercise exercise;
    public long timeremaining=0;
    public boolean flag= true;
    public boolean flag2=true;

    enum Viewmodes {SIMPLIFIED,COMPLEX}
    private Viewmodes viewmode=Viewmodes.COMPLEX;

    private int cycleIndex=0;
    private int exerciseIndex=0;

    private MainActivity activity;
    private MyApplication application;

    private int cyclesLoaded = 0;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = PlayRoutineBinding.inflate(getLayoutInflater());
        binding.playExerciseLeftTime.setText("-- : --");

        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        application = (MyApplication) getActivity().getApplication();
        activity = (MainActivity) getActivity();


        // Get routine
        routineId = PlayRoutineFragmentArgs.fromBundle(getArguments()).getRoutineId();

        ViewModelProvider.Factory viewModelFactory = new RepositoryViewModel.Factory<>(
                RoutineRepository.class, application.getRoutineRepository()
        );
        routineViewModel = new ViewModelProvider(this, viewModelFactory).get(RoutineViewModel.class);

        routineViewModel.setRoutineId(routineId);

        routineViewModel.getRoutine().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    activity.showProgressBar();
                    break;

                case SUCCESS:

                    Routine r = resource.data;

                    ((MainActivity) getActivity()).getSupportActionBar().setTitle(r.getName());

                    break;

                case ERROR:
                    activity.hideProgressBar();
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        });













        // CycleViewModel

        viewModelFactory = new RepositoryViewModel.Factory<>(
                CycleRepository.class, application.getCycleRepository());

        cycleViewModel = new ViewModelProvider(this, viewModelFactory).get(CycleViewModel.class);
        cycleViewModel.setRoutineId(routineId);

        // ExerciseViewModel

        viewModelFactory = new RepositoryViewModel.Factory<>(
                ExerciseRepository.class, application.getExerciseRepository());

        exerciseViewModel = new ViewModelProvider(this, viewModelFactory).get(ExerciseViewModel.class);
        exerciseViewModel.setRoutineId(routineId);

        binding.playButton.setEnabled(false);

        getCycles();


        //------------------------------------conectar los botones-------------------------------
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.getCountDownTimer().getStatus().observe(getViewLifecycleOwner(), countDownTimerStatus -> {
            if (countDownTimerStatus.isFinished()) {
                if (exerciseslist.size()-1==cycleIndex){
                    //TODO codigo para terminar bien
                    return;
                }else {
                    if (exerciseslist.get(cycleIndex).size()-1>exerciseIndex)
                        exerciseIndex++;
                    else{
                        cycleIndex++;
                        exerciseIndex=0;
                    }

                    ejercicios_left--;
                    setup();
                    viewModel.getCountDownTimer().stop();
                    viewModel.getCountDownTimer().start(exercise.getDuration()*1000,1000);

//                binding.countdown.setText(R.string.done);
//                binding.start.setEnabled(true);
//                binding.stop.setEnabled(false);
//                binding.pause.setEnabled(false);
//                binding.addTime.setEnabled(false);
                }
            }
            else {
                if (flag2){
                    int sec =(int) ((countDownTimerStatus.getRemainingTime())%60);
                    int min=(int) (countDownTimerStatus.getRemainingTime() / 60);
                    String secs= Integer.valueOf(sec).toString();
                    String minutes = Integer.valueOf(min).toString();
                    if(min < 10) {
                        minutes = "0" + minutes;
                    }
                    if(sec < 10) {
                        secs = "0" + secs;
                    }
                    String ans = minutes + " : " + secs ;
                    binding.playExerciseLeftTime.setText(ans);
                }
                flag2=true;
//                    binding.playExerciseLeftTime.setText(String.valueOf(countDownTimerStatus.getRemainingTime()));
            }


        });

        binding.playButton.setOnClickListener(v -> {
            if (!viewModel.getCountDownTimer().isPaused()){
                if (viewModel.getCountDownTimer().isStarted())
                {
                    viewModel.getCountDownTimer().pause();
                    binding.playButton.setImageResource(R.drawable.play_button);

                }
                else {
                    viewModel.getCountDownTimer().start(exercise.getDuration()*1000,1000);
                    binding.playButton.setImageResource(R.drawable.pause_button);
                }
            }else {
                if (viewModel.getCountDownTimer().isStarted()){
                    viewModel.getCountDownTimer().resume();
                    binding.playButton.setImageResource(R.drawable.pause_button);
                }
                else{

                }

            }
        });
        binding.prevButton.setOnClickListener(v -> {

            if (exerciseIndex==0){
                if (cycleIndex!=0){
                    cycleIndex--;
                    exerciseIndex=exerciseslist.get(cycleIndex).size()-1;
                    ejercicios_left++;
                }
                else
                    ;
            }
            else{
                exerciseIndex--;
                ejercicios_left++;
            }


            setup();
            if (viewModel.getCountDownTimer().countDownTimer!=null) {
                viewModel.getCountDownTimer().stop();
                viewModel.getCountDownTimer().start(exercise.getDuration() * 1000, 1000);
                viewModel.getCountDownTimer().pause();
            }
            binding.playButton.setImageResource(R.drawable.play_button);
            int sec = exercise.getDuration()%60;
            int min=exercise.getDuration() / 60;
            String secs= Integer.valueOf(sec).toString();
            String minutes = Integer.valueOf(min).toString();
            if(min < 10) {
                minutes = "0" + minutes;
            }
            if(sec < 10) {
                secs = "0" + secs;
            }
            String ans = minutes + " : " + secs ;
            binding.playExerciseLeftTime.setText(ans);
            flag2=false;


        });
        binding.nextButton.setOnClickListener(v -> {
            if (exerciseIndex==exerciseslist.get(cycleIndex).size()-1){
                if (cycleIndex<cycles.size()-1){
                    cycleIndex++;
                    exerciseIndex=0;
                    ejercicios_left--;
                }
                else
                    return;
            }
            else{
                exerciseIndex++;
                ejercicios_left--;
            }


            setup();
            if (viewModel.getCountDownTimer().countDownTimer!=null){
                viewModel.getCountDownTimer().stop();
                viewModel.getCountDownTimer().start(exercise.getDuration()*1000,1000);
                viewModel.getCountDownTimer().pause();
            }

            binding.playButton.setImageResource(R.drawable.play_button);
            int sec = exercise.getDuration()%60;
            int min=exercise.getDuration() / 60;
            String secs= Integer.valueOf(sec).toString();
            String minutes = Integer.valueOf(min).toString();
            if(min < 10) {
                minutes = "0" + minutes;
            }
            if(sec < 10) {
                secs = "0" + secs;
            }
            String ans = minutes + " : " + secs ;
            binding.playExerciseLeftTime.setText(ans);
            flag2=false;
        });
        binding.changeviewmode.setOnClickListener(v -> {
            if (viewmode==Viewmodes.COMPLEX){
                binding.simpleImageView.setVisibility(View.GONE);
                binding.expandable.setVisibility(View.GONE);
                binding.playExerciseLeftTime.setPadding(binding.playExerciseLeftTime.getPaddingLeft(),binding.playExerciseLeftTime.getPaddingTop()+20,binding.playExerciseLeftTime.getPaddingRight(),binding.playExerciseLeftTime.getPaddingBottom()+150);
//                binding.ExecuteRoutineExerciseTitle.setVisibility(View.VISIBLE);
                viewmode= Viewmodes.SIMPLIFIED;
            }
            else {
                binding.simpleImageView.setVisibility(View.VISIBLE);
                binding.expandable.setVisibility(View.VISIBLE);
                binding.playExerciseLeftTime.setPadding(binding.playExerciseLeftTime.getPaddingLeft(),binding.playExerciseLeftTime.getPaddingTop()-20,binding.playExerciseLeftTime.getPaddingRight(),binding.playExerciseLeftTime.getPaddingBottom()-150);
//                binding.ExecuteRoutineExerciseTitle.setVisibility(View.GONE);
                viewmode= Viewmodes.COMPLEX;
            }
        });

    }


    private void getCycles(){
        AtomicBoolean done = new AtomicBoolean(false);
        cycleViewModel.getCycles().observe(getViewLifecycleOwner(), r -> {
            if(done.compareAndSet(false, true))
                return;

            switch (r.status) {
                case LOADING:
                    activity.showProgressBar();
                    break;

                case SUCCESS:
                    cycles.clear();
                    cycles.addAll(r.data);
//                    exercises.clear();
                    for(Cycle cycle : cycles){
                        getExercises(cycle.getId(), cycles.size());

                    }


                    break;

                case ERROR:
                    activity.hideProgressBar();
                    Toast.makeText(activity, r.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }


    private void getExercises(int cycleId, int cyclesLength){
        AtomicBoolean done = new AtomicBoolean(false);
        exerciseViewModel.getExercises(cycleId).observe(getViewLifecycleOwner(), r -> {
            if(done.compareAndSet(false, true))
                return;

            switch (r.status) {
                case LOADING:
                    activity.showProgressBar();
                    break;

                case SUCCESS:


                    exercises.add(r.data);
                   // cyclesLoaded += 1;

                    if(cycles.size() == exercises.size()) {
                        activity.hideProgressBar();
                        binding.playButton.setEnabled(true);
                        ejercicios_left=0;
                        for (List<Exercise> e:exercises) {
                            ejercicios_left+=e.size();
                        }
                        exerciseslist.clear();
                        exerciseslist.addAll(exercises);
                        setup();
                        return;
                    }

                    break;

                case ERROR:
                    activity.hideProgressBar();
                    Toast.makeText(activity, r.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    int ejercicios_left=0;
    public void setup(){

        exerciseslist.sort((o1, o2) -> o1.get(0).getCycleId()-o2.get(0).getCycleId());
        for (List<Exercise>l:exerciseslist) {
            l.sort((o1, o2) -> o1.getOrder()-o2.getOrder());
        }


        if (cycles.size()>cycleIndex && exerciseslist.get(cycleIndex).size()>exerciseIndex){
            cycle = cycles.get(cycleIndex);
            exercise = exerciseslist.get(cycleIndex).get(exerciseIndex);


        }

        binding.playExerciseLeftRoutines.setText(String.format(getContext().getString(R.string.ejs_reamining),ejercicios_left));
        binding.content.setText(exercise.getDetail());
        String auxi=cycle.getName()+" - "+exercise.getName();
        binding.ExecuteRoutineExerciseTitle.setText(auxi);

    }

}
