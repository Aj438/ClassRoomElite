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

import com.ashish.classroomelite.Helper;
import com.ashish.classroomelite.Models.Attendance;
import com.ashish.classroomelite.Models.Result;
import com.ashish.classroomelite.Models.Student;
import com.ashish.classroomelite.databinding.FragmentStudentActivityBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentActivityFragment extends Fragment {
    FragmentStudentActivityBinding binding;
    FirebaseFirestore firebaseFirestore;
    DocumentReference studentRef, resultRef;
    Student student;
    NavDirections action;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        present();
        binding.cardView3.setOnClickListener(v -> {
            action = StudentActivityFragmentDirections.actionStudentActivityFragmentToResourcesFragment(false, student.getStudentClass());
            Navigation.findNavController(requireView()).navigate(action);
        });
        showResult();

    }

    private void showResult() {
        resultRef = firebaseFirestore.collection("result" + student.getStudentClass()).document(student.getUid());
        resultRef.addSnapshotListener((value, error) -> {
            if (value != null && value.exists()) {
                Result model = value.toObject(Result.class);
                if (model != null) {
                    if (model.getTest_1() != null) {
                        binding.resultLay.hindi1.setText(model.getTest_1().get(Helper.Hindi));
                        binding.resultLay.et1.setText(model.getTest_1().get(Helper.English));
                        binding.resultLay.pt1.setText(model.getTest_1().get(Helper.Physics));
                        binding.resultLay.ct1.setText(model.getTest_1().get(Helper.Chemistry));
                        binding.resultLay.mt1.setText(model.getTest_1().get(Helper.Math));
                    }
                    if (model.getTest_2() != null) {
                        binding.resultLay.hindi2.setText(model.getTest_2().get(Helper.Hindi));
                        binding.resultLay.et2.setText(model.getTest_2().get(Helper.English));
                        binding.resultLay.pt2.setText(model.getTest_2().get(Helper.Physics));
                        binding.resultLay.ct2.setText(model.getTest_2().get(Helper.Chemistry));
                        binding.resultLay.mt2.setText(model.getTest_2().get(Helper.Math));
                    }
                    if (model.getFinal_Result() != null) {
                        binding.resultLay.hindiFinall.setText(model.getFinal_Result().get(Helper.Hindi));
                        binding.resultLay.eth.setText(model.getFinal_Result().get(Helper.English));
                        binding.resultLay.pth.setText(model.getFinal_Result().get(Helper.Physics));
                        binding.resultLay.cth.setText(model.getFinal_Result().get(Helper.Chemistry));
                        binding.resultLay.mth.setText(model.getFinal_Result().get(Helper.Math));
                    }
                }
            }
        });
    }

    private void present() {
        studentRef = firebaseFirestore.collection(student.getStudentClass()).document(student.getUid());
        studentRef.addSnapshotListener((value, error) -> {
            if (value != null && value.exists()) {
                Attendance attendance = value.toObject(Attendance.class);
                if (attendance != null) {
                    binding.studentPresent.setText(String.valueOf(attendance.getPresent()));
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        student = (Student) getArguments().get("student");
        binding = FragmentStudentActivityBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}