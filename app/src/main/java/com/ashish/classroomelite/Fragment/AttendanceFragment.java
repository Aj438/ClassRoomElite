package com.ashish.classroomelite.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ashish.classroomelite.Adapter.AttendanceAdapter;
import com.ashish.classroomelite.Models.Attendance;
import com.ashish.classroomelite.Models.Student;
import com.ashish.classroomelite.databinding.FragmentAttendanceBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AttendanceFragment extends Fragment implements AttendanceAdapter.CallBack{
    private FragmentAttendanceBinding binding;
    private AttendanceAdapter adapter;
    NavDirections action;
    private Attendance attendance=new Attendance("0",0);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CollectionReference collectionReference=firebaseFirestore.collection("student");
        assert getArguments() != null;
        Query query = collectionReference.whereEqualTo("studentClass",getArguments().getString("classname"));

        FirestoreRecyclerOptions<Student> list = new FirestoreRecyclerOptions.Builder<Student>()
                .setQuery(query, Student.class).build();
        adapter = new AttendanceAdapter(list , this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        binding.AttendanceStudentListRv.setLayoutManager(linearLayoutManager);
        binding.AttendanceStudentListRv.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding=FragmentAttendanceBinding.inflate(inflater,container,false);
       return binding.getRoot();
    }

    @Override
    public void onItemClicked(Student student) {
        DocumentReference studentRef=firebaseFirestore.collection(student.getStudentClass()).document(student.getUid());
        int att=0;
        studentRef.get().addOnCompleteListener(task -> {
            attendance=task.getResult().toObject(Attendance.class);
            if (attendance!=null){
                attendance.addPresent();
            studentRef.set(attendance);
            }
            else {
                attendance=new Attendance(student.getRoll_no(),0);
                attendance.addPresent();
                studentRef.set(attendance);
            }
        });
    }
}