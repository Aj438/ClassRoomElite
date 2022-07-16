package com.ashish.classroomelite.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashish.classroomelite.Adapter.StudentAdapter;
import com.ashish.classroomelite.Models.Student;
import com.ashish.classroomelite.R;
import com.ashish.classroomelite.databinding.FragmentStudentListBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class StudentListFragment extends Fragment implements StudentAdapter.CallBack {
    private FragmentStudentListBinding binding;
    private StudentAdapter adapter;
    NavDirections action;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

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
    public void onItemClicked(Student student) {
     action=StudentListFragmentDirections.actionStudentListFragmentToBottomSheetFragment(student);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CollectionReference collectionReference=firebaseFirestore.collection("student");
        assert getArguments() != null;
        Query query = collectionReference.whereEqualTo("studentClass","Class_A");//getArguments().getString("classname")

        FirestoreRecyclerOptions<Student> list = new FirestoreRecyclerOptions.Builder<Student>()
                .setQuery(query, Student.class).build();
        adapter = new StudentAdapter(list , this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        binding.studentListRv.setLayoutManager(linearLayoutManager);
        binding.studentListRv.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding=FragmentStudentListBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }


}