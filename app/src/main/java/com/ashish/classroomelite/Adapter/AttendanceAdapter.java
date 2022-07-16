package com.ashish.classroomelite.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashish.classroomelite.Models.Student;
import com.ashish.classroomelite.databinding.AttendanceItemBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AttendanceAdapter extends FirestoreRecyclerAdapter<Student,AttendanceAdapter.viewHolder> {
    AttendanceItemBinding bind;
    AttendanceAdapter.CallBack callBack;
    public interface CallBack{
        void onItemClicked(Student student);
    }

    public AttendanceAdapter(@NonNull FirestoreRecyclerOptions<Student> options, AttendanceAdapter.CallBack callBack) {
        super(options);
        this.callBack=callBack;
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull Student model) {
        bind.studentName.setText(model.getName());
        bind.studentRoll.setText(model.getRoll_no());
        bind.checkBoxPresent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                callBack.onItemClicked(model);
            }
        });
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AttendanceItemBinding binding = AttendanceItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new viewHolder(binding);
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        public viewHolder(@NonNull AttendanceItemBinding itemView) {
            super(itemView.getRoot());
            bind=itemView;
        }
    }
}
