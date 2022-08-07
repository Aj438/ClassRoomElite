package com.ashish.classroomelite.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ashish.classroomelite.Adapter.ResultAdapter;
import com.ashish.classroomelite.Models.Faculty;
import com.ashish.classroomelite.Models.Result;
import com.ashish.classroomelite.databinding.FragmentFacultyActivityBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class FacultyActivityFragment extends Fragment implements ResultAdapter.CallBack {
    private FragmentFacultyActivityBinding binding;
    NavDirections action;
    Faculty amount;
    private ResultAdapter adapter;
    FirebaseFirestore firebaseFirestore;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CollectionReference resultRef=firebaseFirestore.collection("resultClass_A");
        Query query = resultRef.whereEqualTo("studentClass",amount.getClassName());

        FirestoreRecyclerOptions<Result> list = new FirestoreRecyclerOptions.Builder<Result>()
                .setQuery(query, Result.class).build();
        adapter = new ResultAdapter(list , this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.studentResultRv.setLayoutManager(linearLayoutManager);
        binding.studentResultRv.setAdapter(adapter);
        binding.cardView3.setOnClickListener(v -> {
            action = FacultyActivityFragmentDirections.actionFacultyActivityFragmentToResourcesFragment(true,amount.getClassName());
            Navigation.findNavController(requireView()).navigate(action);
        });
        binding.cardView.setOnClickListener(v -> {
            action = FacultyActivityFragmentDirections.actionFacultyActivityFragmentToStudentListFragment(amount.getClassName());
            Navigation.findNavController(requireView()).navigate(action);
        });
        binding.cardView2.setOnClickListener(v -> {
            action = FacultyActivityFragmentDirections.actionFacultyActivityFragmentToAttendanceFragment(amount.getClassName());
            Navigation.findNavController(requireView()).navigate(action);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         firebaseFirestore = FirebaseFirestore.getInstance();
        assert getArguments() != null;
        amount = (Faculty)getArguments().get("faculty");
       binding=FragmentFacultyActivityBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();
        binding=null;
    }

    @Override
    public void onItemClicked(Result result) {

    }
}