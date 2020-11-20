package com.pi.gymapp.ui.routine;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.pi.gymapp.R;
import com.pi.gymapp.databinding.RoutinesExploreBinding;
import com.pi.gymapp.domain.Difficulties;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


public class RoutinesExploreFragment extends Fragment {

    private RoutinesExploreBinding binding;

    private boolean favouritesFilter = false;
    private String categoryFilter = "none";

    private int difficultyIndex = 0;
    private int orderIndex = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = RoutinesExploreBinding.inflate(getLayoutInflater());

        changedListParams();


        // --------------------------------- Buttons setup ---------------------------------

        binding.addFilterButton.setOnClickListener(v -> {
            showDialog();
        });

//        binding.allRoutinesChip.setOnClickListener(l -> {
//            filter = "all";
//            changedListParams();
//        });
//
//        binding.favRoutinesChip.setOnClickListener(l -> {
//            filter = "favourites";
//            changedListParams();
//        });
//
//        binding.orderBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                orderIndex = position;
//                changedListParams();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                orderIndex = 0;
//                changedListParams();
//            }
//        });

        return binding.getRoot();
    }


    private void changedListParams(){
        RoutineListFragment fragment = newFragment(RoutineListFragment.class,
                new String[]{"filter", "orderBy"}, new String[]{"all", String.valueOf(orderIndex)});

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.routineListFragment, fragment).commit();
    }


    private <T extends Fragment> T newFragment(Class<T> fragmentClass, String[] keys, String[] args){
        Bundle bundle = new Bundle();
        for(int i=0; i<keys.length; i++)
            bundle.putString(keys[i], args[i]);

        try{
            T fragment = fragmentClass.newInstance();
            fragment.setArguments(bundle);
            return fragment;
        }catch(Exception e){
            Log.e("Fragment", e.getMessage());
            return null;
        }
    }


    private void addChip(String text, Runnable onRemove){
        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip, binding.filters, false);
        chip.setText(text);

        chip.setOnClickListener(v -> {
            binding.filters.removeView(v);
            onRemove.run();
        });

        binding.filters.addView(chip);
    }


    private void showDialog(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.select_dialog_singlechoice);

        List<String> filters = Arrays.asList(getResources().getStringArray(R.array.routineFilters));
        filters.forEach(adapter::add);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.routine_filter_dialog_help));
        builder.setNegativeButton(getString(R.string.cancel_dialog_button), (dialog, which) -> dialog.dismiss());

        builder.setAdapter(adapter, (dialog, which) -> {
            switch(which){
                case 0:
                    favouritesFilter = true;
                    addChip(adapter.getItem(which), () -> {
                        favouritesFilter = false;
                    });
                    break;

                case 1:
                    showCategoriesDialog();

                case 2:
                    showDifficultiesDialog();
            }

            dialog.dismiss();
        });

        builder.show();
    }


    private void showCategoriesDialog(){
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                getContext(), android.R.layout.select_dialog_singlechoice);
//
//        List<String> difficulties = Arrays.asList(getResources().getStringArray(R.array.difficulties));
//        difficulties.forEach(adapter::add);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setNegativeButton(getString(R.string.cancel_dialog_button), (dialog1, which1) -> dialog1.dismiss());

//        builder.setAdapter(adapter, ((dialog, which) -> {
//            categoryFilter =
//            dialog.dismiss();
//        }));

//        builder.show();
    }

    private void showDifficultiesDialog(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.select_dialog_singlechoice);

        List<Difficulties> difficulties = Arrays.asList(Difficulties.values());
        difficulties.forEach(d -> adapter.add(d.getName()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setNegativeButton(getString(R.string.cancel_dialog_button), (dialog1, which1) -> dialog1.dismiss());

        builder.setAdapter(adapter, ((dialog, which) -> {
            difficultyIndex = which;
            addChip(adapter.getItem(which), () -> {
                difficultyIndex = -1;
            });

            dialog.dismiss();
        }));

        builder.show();
    }

}
