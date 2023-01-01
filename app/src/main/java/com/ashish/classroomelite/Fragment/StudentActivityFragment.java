package com.ashish.classroomelite.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import java.util.Map;

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
                        SetText(binding.resultLay.hindi1, model.getTest_1(), binding.resultLay.et1, binding.resultLay.pt1, binding.resultLay.ct1, binding.resultLay.mt1,binding.resultLay.tt1);
                    }
                    if (model.getTest_2() != null) {
                        SetText(binding.resultLay.hindi2, model.getTest_2(), binding.resultLay.et2, binding.resultLay.pt2, binding.resultLay.ct2, binding.resultLay.mt2,binding.resultLay.tt2);
                    }
                    if (model.getFinal_Result() != null) {
                        SetText(binding.resultLay.hindiFinall, model.getFinal_Result(), binding.resultLay.eth, binding.resultLay.pth, binding.resultLay.cth, binding.resultLay.mth,binding.resultLay.tth);
                    }
                }
            }
        });
    }

    private void SetText(@NonNull TextView resultLay, @NonNull Map<String, String> model, @NonNull TextView resultLay1, @NonNull TextView resultLay2, @NonNull TextView resultLay3, @NonNull TextView resultLay4, @NonNull TextView total) {
        resultLay.setText(model.get(Helper.Hindi));
        resultLay1.setText(model.get(Helper.English));
        resultLay2.setText(model.get(Helper.Physics));
        resultLay3.setText(model.get(Helper.Chemistry));
        resultLay4.setText(model.get(Helper.Math));
        int count= 0;
        count +=Integer.parseInt(Helper.Hindi);
        count +=Integer.parseInt(Helper.English);
        count +=Integer.parseInt(Helper.Physics);
        count +=Integer.parseInt(Helper.Chemistry);
        count +=Integer.parseInt(Helper.Math);
        total.setText(Integer.toString(count));
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        student = (Student) getArguments().get("student");
        binding = FragmentStudentActivityBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}