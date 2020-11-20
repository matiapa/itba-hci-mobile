package com.pi.gymapp.ui.routine;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pi.gymapp.R;
import com.pi.gymapp.databinding.RoutinesExploreBinding;


public class RoutinesExploreFragment extends Fragment {

    private RoutinesExploreBinding binding;

    private String filter;
    private int orderIndex;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = RoutinesExploreBinding.inflate(getLayoutInflater());

        filter = "all"; orderIndex = 0;
        changedListParams();


        // --------------------------------- Buttons setup ---------------------------------

        binding.allRoutinesChip.setOnClickListener(l -> {
            filter = "all";
            changedListParams();
        });

        binding.favRoutinesChip.setOnClickListener(l -> {
            filter = "favourites";
            changedListParams();
        });

        binding.orderBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orderIndex = position;
                changedListParams();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                orderIndex = 0;
                changedListParams();
            }
        });

        return binding.getRoot();
    }


    private void changedListParams(){
        RoutineListFragment fragment = newFragment(RoutineListFragment.class,
                new String[]{"filter", "orderBy"}, new String[]{filter, String.valueOf(orderIndex)});

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

}
