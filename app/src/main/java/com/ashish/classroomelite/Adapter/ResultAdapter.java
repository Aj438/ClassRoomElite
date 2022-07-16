package com.ashish.classroomelite.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashish.classroomelite.Helper;
import com.ashish.classroomelite.Models.Result;
import com.ashish.classroomelite.Models.Student;
import com.ashish.classroomelite.databinding.ResultItemBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResultAdapter extends FirestoreRecyclerAdapter<Result, ResultAdapter.viewHolder> {
    ResultItemBinding bind;
    FirebaseFirestore firestore;
    ResultAdapter.CallBack callBack;

    public ResultAdapter(@NonNull FirestoreRecyclerOptions<Result> options, ResultAdapter.CallBack callBack) {
        super(options);
        this.callBack = callBack;
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull Result model) {
        firestore = FirebaseFirestore.getInstance();

        if (model.getUid()!=null){
        DocumentReference documentReference = firestore.collection("student").document(model.getUid());
        documentReference.get().addOnCompleteListener(task -> {
            Student student = task.getResult().toObject(Student.class);
            if (student != null) {
                bind.studentName.setText(student.getName());
                bind.studentRoll.setText(student.getRoll_no());
            }
        });}
        setResult(model);
    }

    private void setResult(Result model) {
        if (model.getTest_1() != null) {
            bind.hindi1.setText(model.getTest_1().get(Helper.Hindi));
            bind.et1.setText(model.getTest_1().get(Helper.English));
            bind.pt1.setText(model.getTest_1().get(Helper.Physics));
            bind.ct1.setText(model.getTest_1().get(Helper.Chemistry));
            bind.mt1.setText(model.getTest_1().get(Helper.Math));
        }
        if (model.getTest_2() != null) {
            bind.hindi2.setText(model.getTest_2().get(Helper.Hindi));
            bind.et2.setText(model.getTest_2().get(Helper.English));
            bind.pt2.setText(model.getTest_2().get(Helper.Physics));
            bind.ct2.setText(model.getTest_2().get(Helper.Chemistry));
            bind.mt2.setText(model.getTest_2().get(Helper.Math));
        }
        if (model.getFinal_Result() != null) {
            bind.hindiFinall.setText(model.getFinal_Result().get(Helper.Hindi));
            bind.eth.setText(model.getFinal_Result().get(Helper.English));
            bind.pth.setText(model.getFinal_Result().get(Helper.Physics));
            bind.cth.setText(model.getFinal_Result().get(Helper.Chemistry));
            bind.mth.setText(model.getFinal_Result().get(Helper.Math));
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ResultItemBinding binding = ResultItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ResultAdapter.viewHolder(binding);
    }

    public interface CallBack {
        void onItemClicked(Result result);
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        public viewHolder(@NonNull ResultItemBinding itemView) {
            super(itemView.getRoot());
            bind = itemView;
        }
    }
}
