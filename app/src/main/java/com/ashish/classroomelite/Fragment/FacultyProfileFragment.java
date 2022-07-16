package com.ashish.classroomelite.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.ashish.classroomelite.Models.Faculty;
import com.ashish.classroomelite.R;
import com.ashish.classroomelite.databinding.FragmentFacultyProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Objects;


public class FacultyProfileFragment extends Fragment {
    private FragmentFacultyProfileBinding binding;
    String[] type = new String[] {"Class_A", "Class_B", "Class_C"};
    private String className;
    NavDirections action;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_menu_popup_item, type);

        binding.classnameEt.setAdapter(adapter);

        binding.classnameEt.setOnItemClickListener((parent, view1, position, id) -> className = type[position]);

        binding.saveUser.setOnClickListener(v -> saveUser());

    }

    private void saveUser() {
        String name = Objects.requireNonNull(binding.usernameEt.getText()).toString().trim();
        String uid = FirebaseAuth.getInstance().getUid();
        assert uid != null;
        DocumentReference facultyRef = firebaseFirestore.collection("faculty").document(uid);
        if(name.isEmpty() || className.isEmpty()) {
            Toast.makeText(requireContext(), "Enter Full Detail", Toast.LENGTH_SHORT).show();
        }
        else {
            Faculty faculty = new Faculty(uid , name , className);
            facultyRef.set(faculty, SetOptions.merge()).addOnSuccessListener(documentReference -> {
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("user", "faculty");
                myEdit.apply();
                Toast.makeText(requireContext(), "saved", Toast.LENGTH_SHORT).show();
                action = FacultyProfileFragmentDirections.actionFacultyProfileFragmentToFacultyActivityFragment(faculty);
                Navigation.findNavController(requireView()).navigate(action);
            });
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =FragmentFacultyProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}