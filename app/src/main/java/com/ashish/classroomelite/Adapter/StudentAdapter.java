package com.ashish.classroomelite.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashish.classroomelite.Models.Student;
import com.ashish.classroomelite.databinding.StudentListItemBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class StudentAdapter extends FirestoreRecyclerAdapter<Student,StudentAdapter.viewHolder> {
    StudentListItemBinding bind;
    StudentAdapter.CallBack callBack;
    public interface CallBack{
        void onItemClicked(Student student);
    }

    public StudentAdapter(@NonNull FirestoreRecyclerOptions<Student> options,StudentAdapter.CallBack callBack) {
        super(options);
        this.callBack=callBack;
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull Student model) {
        bind.studentName.setText(model.getName());
        bind.studentRoll.setText(model.getRoll_no());
      bind.addResult.setOnClickListener(v -> {
         callBack.onItemClicked(model);
      });
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StudentListItemBinding binding = StudentListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new viewHolder(binding);
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        public viewHolder(@NonNull StudentListItemBinding itemView) {
            super(itemView.getRoot());
            bind=itemView;
        }
    }
}
