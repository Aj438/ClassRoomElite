package com.ashish.classroomelite.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.ashish.classroomelite.Models.Faculty;
import com.ashish.classroomelite.Models.Student;
import com.ashish.classroomelite.databinding.FragmentFlashScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FlashScreenFragment extends Fragment {
    private FragmentFlashScreenBinding binding;
    private NavDirections action;
    private FirebaseFirestore firestore;
    private DocumentReference documentReference;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getUid();
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            String user = sharedPreferences.getString("user", null);
            if (user!=null) {
                if (user.equals("faculty") && uid != null) {
                    documentReference = firestore.collection("faculty").document(uid);
                    documentReference.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            action = FlashScreenFragmentDirections.actionFlashScreenFragmentToFacultyActivityFragment(task.getResult().toObject(Faculty.class));
                            Navigation.findNavController(requireView()).navigate(action);
                        }
                    });

                } else if (user.equals("student") && uid != null) {
                    documentReference = firestore.collection("student").document(uid);
                    documentReference.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            action = FlashScreenFragmentDirections.actionFlashScreenFragmentToStudentActivityFragment(task.getResult().toObject(Student.class));
                            Navigation.findNavController(requireView()).navigate(action);
                        }
                    });
                } else {
                    action = FlashScreenFragmentDirections.actionFlashScreenFragmentToLoginFragment();
                    Navigation.findNavController(requireView()).navigate(action);
                }
            }
            else { action = FlashScreenFragmentDirections.actionFlashScreenFragmentToLoginFragment();
                Navigation.findNavController(requireView()).navigate(action);}

        }, 1000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFlashScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}