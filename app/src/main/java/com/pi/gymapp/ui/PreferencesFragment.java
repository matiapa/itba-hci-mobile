//package com.pi.gymapp.ui;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatDelegate;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.pi.gymapp.MyApplication;
//import com.pi.gymapp.databinding.FragmentPreferencesBinding;
//import com.pi.gymapp.databinding.FragmentProfileBinding;
//import com.pi.gymapp.repo.UserRepository;
//import com.pi.gymapp.ui.profile.ProfileViewModel;
//import com.pi.gymapp.utils.RepositoryViewModel;
//
//public class PreferencesFragment extends Fragment {
//
//    private FragmentPreferencesBinding  binding;
//
//    private MainActivity activity;
//
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        binding = FragmentPreferencesBinding.inflate(getLayoutInflater());
//
//
//        activity = (MainActivity) getActivity();
//
//       if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
//            binding.switch1.setChecked(true);
//
//
//        binding.switch1.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//
//               if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_NO) {
//                   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                   binding.switch1.setChecked(true);
//               } else{
//                   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                   binding.switch1.setChecked(false);
//               }
//           }
//       }
//
//        );
//        return binding.getRoot();
//    }
//}
