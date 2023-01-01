package com.ashish.classroomelite.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

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

import java.util.Map;

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
                bind.studentName.setText(String.format("Name-%s", student.getName()));
                bind.studentRoll.setText(String.format("Roll No.-%s", student.getRoll_no()));
            }
        });}
        setResult(model);
    }

    private void setResult(Result model) {
        if (model.getTest_1() != null) {
            SetTextStudent(bind.hindi1, model.getTest_1(), bind.et1, bind.pt1, bind.ct1, bind.mt1, bind.tt1);
        }
        if (model.getTest_2() != null) {
            SetTextStudent(bind.hindi2, model.getTest_2(), bind.et2, bind.pt2, bind.ct2, bind.mt2, bind.tt2);
        }
        if (model.getFinal_Result() != null) {
            SetTextStudent(bind.hindiFinall, model.getFinal_Result(), bind.eth, bind.pth, bind.cth, bind.mth, bind.tth);
        }
    }

    private void SetTextStudent(TextView bind, Map<String, String> model, TextView bind1, TextView bind2, TextView bind3, TextView bind4, TextView bind5) {
        bind.setText(model.get(Helper.Hindi));
        bind1.setText(model.get(Helper.English));
        bind2.setText(model.get(Helper.Physics));
        bind3.setText(model.get(Helper.Chemistry));
        bind4.setText(model.get(Helper.Math));
        int count= 0;
        count +=Integer.parseInt(model.get(Helper.Hindi));
        count +=Integer.parseInt(model.get(Helper.English));
        count +=Integer.parseInt(model.get(Helper.Physics));
        count +=Integer.parseInt(model.get(Helper.Chemistry));
        count +=Integer.parseInt(model.get(Helper.Math));
        bind5.setText(Integer.toString(count));
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
