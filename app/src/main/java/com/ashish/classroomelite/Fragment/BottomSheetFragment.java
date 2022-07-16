package com.ashish.classroomelite.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ashish.classroomelite.Models.Result;
import com.ashish.classroomelite.Models.Student;
import com.ashish.classroomelite.R;
import com.ashish.classroomelite.databinding.FragmentBottomSheetBinding;
import com.ashish.classroomelite.databinding.FragmentStudentListBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private FragmentBottomSheetBinding binding;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
   private DocumentReference resultRef;
   private String[] type = new String[] {"Test_1", "Test_2", "Final_Result"};
  private   String examType,hindi,english,physics,chemistry,math;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        Student student = (Student) getArguments().get("student");
        Log.d("rohit", "onViewCreated: "+student.getStudentClass());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_menu_popup_item, type);
        binding.examType.setAdapter(adapter);
        binding.examType.setOnItemClickListener((parent, view1, position, id) -> examType = type[position]);
        binding.saveUser.setOnClickListener(v -> saveResult(student));
    }

    private void saveResult(Student student) {
      hindi= Objects.requireNonNull(binding.hindiEt.getText()).toString();
      english=  Objects.requireNonNull(binding.englishEt.getText()).toString();
      physics=  Objects.requireNonNull(binding.physicsEt.getText()).toString();
      chemistry=  Objects.requireNonNull(binding.chemistryEt.getText()).toString();
      math=  Objects.requireNonNull(binding.mathEt.getText()).toString();
        Result result=new Result();
        Map<String,String> map =new HashMap<>();
        map.put("Hindi",hindi);
        map.put("English",english);
        map.put("Physics",physics);
        map.put("Chemistry",chemistry);
        map.put("Math",math);
        result.setStudentClass(student.getStudentClass());
        result.setUid(student.getUid());
        String collection="result"+student.getStudentClass();
        resultRef = firebaseFirestore.collection(collection).document(student.getUid());
        if (examType.equals(type[0])){
            result.setTest_1(map);
            resultRef.set(result, SetOptions.mergeFields("test_1","studentClass","uid")).addOnSuccessListener(unused -> {
                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show();
                dismiss();
            }).addOnFailureListener(e -> Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

        }else if (examType.equals(type[1])){
            result.setTest_2(map);
            resultRef.set(result, SetOptions.mergeFields("test_2","studentClass","uid")).addOnSuccessListener(unused -> {
                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show();
                dismiss();
            }).addOnFailureListener(e -> Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

        }else{
            result.setFinal_Result(map);
            resultRef.set(result, SetOptions.mergeFields("final_Result","studentClass","uid")).addOnSuccessListener(unused -> {
                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show();
                dismiss();
            }).addOnFailureListener(e -> Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding= FragmentBottomSheetBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
}