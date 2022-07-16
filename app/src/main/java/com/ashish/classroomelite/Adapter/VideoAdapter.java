package com.ashish.classroomelite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashish.classroomelite.databinding.VideoItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    VideoItemBinding bind;
    VideoAdapter.CallBack callBack;
    List<String> mdata;
    int day = 1;
    Context context;

    public VideoAdapter(Context context,VideoAdapter.CallBack callBack, List<String> data) {
        this.context = context;
        this.mdata = data;
        this.callBack=callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VideoItemBinding binding = VideoItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VideoAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        bind.playButton.setOnClickListener(v -> callBack.onItemClicked(mdata.get(holder.getLayoutPosition())));
        String tv = "Day" + day;
        ++day;
        bind.videoTv.setText(tv);
        long thumb = holder.getLayoutPosition() * 1000L;
        RequestOptions options = new RequestOptions().frame(thumb);
        Glide.with(context).load(mdata.get(position)).apply(options).into(bind.shapeableImageView);

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public interface CallBack {
        void onItemClicked(String url);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull VideoItemBinding itemView) {
            super(itemView.getRoot());
            bind = itemView;
        }
    }
}
