package com.pi.gymapp.ui.routine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.pi.gymapp.R;
import com.pi.gymapp.databinding.RoutinesExploreBinding;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.domain.RoutineDifficulties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class RoutinesExploreFragment extends Fragment {

    private RoutinesExploreBinding binding;

    private List<RoutineFilter> routineFilters;

    private boolean filterFavourites;
    private String filterCategory;
    private int filterDifficulty;

    private int orderBy;
    private String orderDir;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        routineFilters = new ArrayList<>();

        filterFavourites = false;
        filterCategory = "none";
        filterDifficulty = -1;

        orderBy = 0;
        orderDir = "asc";
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = RoutinesExploreBinding.inflate(getLayoutInflater());


        // ------------------------------ Filter and sorting setup ------------------------------

        binding.addFilterButton.setOnClickListener(v -> {
            showFilterDialog();
        });

        binding.orderBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orderBy = position;
                changedListParams();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                orderBy = 0;
                changedListParams();
            }
        });

        applyFilters();

        return binding.getRoot();
    }


    private void applyFilters(){
        for(RoutineFilter filter : routineFilters){
            // Check that the filter is not repeated

            ArrayList<View> views = new ArrayList<>();
            binding.filters.findViewsWithText(views, filter.chipText, View.FIND_VIEWS_WITH_TEXT);
            if(! views.isEmpty())
                continue;

            // Put the chip and apply the filter

            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip, binding.filters, false);
            chip.setText(filter.chipText);

            chip.setOnClickListener(v -> {
                // Remove the chip and undo the filter

                routineFilters = routineFilters.stream().filter(
                        f -> ! f.chipText.equals(((Chip) v).getText().toString())
                ).collect(Collectors.toList());

                binding.filters.removeView(v);
                filter.onRemove.run();

                changedListParams();
            });

            binding.filters.addView(chip);

            filter.onApply.run();

        }

        changedListParams();
    }


    private void changedListParams(){
        RoutineListFragment fragment = new RoutineListFragment();

        Bundle bundle = new Bundle();
        bundle.putBoolean("filterFavourites", filterFavourites);
        bundle.putString("filterCategory", filterCategory);
        bundle.putInt("filterDifficulty", filterDifficulty);

        bundle.putInt("orderBy", orderBy);
        bundle.putString("orderDir", orderDir);

        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.routineListFragment, fragment).commit();
    }


    // ---------------------------------- FILTER CHOOSING ----------------------------------

    private void showFilterDialog(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.select_dialog_singlechoice);

        List<String> filters = Arrays.asList(getResources().getStringArray(R.array.routineFilters));
        filters.forEach(adapter::add);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.routine_filter_dialog_help));
        builder.setNegativeButton(getString(R.string.cancel_button), (dialog, which) -> dialog.dismiss());

        builder.setAdapter(adapter, (dialog, index) -> {
            switch(index){
                case 0:
                    routineFilters.add(
                        new RoutineFilter(
                            adapter.getItem(index),
                            () -> { filterFavourites = true; },
                            () -> { filterFavourites = false; }
                        )
                    );
                    applyFilters();
                    dialog.dismiss();

                    break;

                case 1:
                    showCategoriesDialog();

                case 2:
                    showDifficultiesDialog();
            }
        });

        builder.show();
    }


    private void showCategoriesDialog(){

    }


    private void showDifficultiesDialog(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.select_dialog_singlechoice);

        List<RoutineDifficulties> difficulties = Arrays.asList(RoutineDifficulties.values());
        difficulties.forEach(d -> adapter.add(d.getFriendlyName()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setNegativeButton(getString(R.string.cancel_button), (dialog1, which1) -> dialog1.dismiss());

        builder.setAdapter(adapter, ((dialog, index) -> {
            routineFilters.add(
                new RoutineFilter(
                    adapter.getItem(index),
                    () -> { filterDifficulty = index; },
                    () -> { filterDifficulty = -1; }
                )
            );

            applyFilters();
            dialog.dismiss();
        }));

        builder.show();
    }


    private class RoutineFilter{
        String chipText;
        Runnable onApply, onRemove;

        public RoutineFilter(String chipText, Runnable onApply, Runnable onRemove) {
            this.chipText = chipText;
            this.onApply = onApply;
            this.onRemove = onRemove;
        }
    }

}
