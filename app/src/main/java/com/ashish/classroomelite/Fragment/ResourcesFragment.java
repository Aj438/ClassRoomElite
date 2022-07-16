package com.ashish.classroomelite.Fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ashish.classroomelite.Adapter.VideoAdapter;
import com.ashish.classroomelite.Models.VideoResources;
import com.ashish.classroomelite.databinding.FragmentResourcesBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResourcesFragment extends Fragment implements VideoAdapter.CallBack {
    NavDirections action;
    private FragmentResourcesBinding binding;
    private ActivityResultLauncher<String> mgetContent;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private DocumentReference videoRef;
    private CollectionReference videoColl;
    private StorageReference storageReference;
    private ExoPlayer player;
    private Uri videouri;
    private String downloadUri;
    private VideoResources videoResources;
    private ArrayList<String> arrayList;
    private ProgressDialog progressDialog;
    private VideoAdapter adapter;

    @Override
    public void onStop() {
        super.onStop();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setflotingButton();
        binding.floatingActionButton.setOnClickListener(v -> mgetContent.launch("video/*"));
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        player = new ExoPlayer.Builder(requireContext()).build();
        binding.exoplayer.setPlayer(player);
        videoRef = firestore.collection("video").document(getArguments().getString("className"));

        progressDialog = new ProgressDialog(requireContext());

        mgetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                videouri = result;
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                sendvideotoStorage();
            }
        });


        if (videoRef != null) {
            setRecyclerView();
            binding.exoplayer.setVisibility(View.VISIBLE);
            videoRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    VideoResources videoResources1 = task.getResult().toObject(VideoResources.class);
                    assert videoResources1 != null;
                    ArrayList<String> list = videoResources1.getVideoList();
                    for (String x : list) {
                        player.addMediaItem(MediaItem.fromUri(x));
                    }
                    player.prepare();
                    player.play();
                }
            });
        } else {
            player.clearMediaItems();
        }
    }

    private void setRecyclerView() {
        videoRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> list = Objects.requireNonNull(task.getResult().toObject(VideoResources.class)).getVideoList();
                adapter = new VideoAdapter(requireContext(),this,list);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
                binding.videoRv.setLayoutManager(linearLayoutManager);
                binding.videoRv.setAdapter(adapter);
            }
        });

    }

    private void setflotingButton() {
        assert getArguments() != null;
        Boolean addResources = (Boolean) getArguments().get("addResources");
        if (addResources) binding.floatingActionButton.setVisibility(View.VISIBLE);
        else binding.floatingActionButton.setVisibility(View.GONE);
    }

    private void sendvideotoStorage() {
        storageReference = storage.getReference("Files/" + System.currentTimeMillis());
        storageReference.putFile(videouri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            downloadUri = uriTask.getResult().toString();

            if (videoRef != null) {
                videoRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        videoResources = task.getResult().toObject(VideoResources.class);
                    assert videoResources != null;
                    arrayList = videoResources.getVideoList();
                    addOnFirestore(arrayList);
                });
            } else {
                videoResources = new VideoResources();
                arrayList = new ArrayList<>();
                addOnFirestore(arrayList);
            }

        }).addOnFailureListener(e -> {
            Log.d("rohit", "sendvideotoStorage: " + e.getMessage());
            progressDialog.dismiss();
        }).addOnProgressListener(taskSnapshot -> {
            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
            progressDialog.setMessage("Uploaded " + (int) progress + "%");
        });
    }

    private void addOnFirestore(ArrayList<String> arrayList) {
        arrayList.add(downloadUri);
        videoResources.setVideoList(arrayList);
        videoRef.set(videoResources).addOnCompleteListener(task -> progressDialog.dismiss());
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentResourcesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onItemClicked(String url) {
        playVideo(url);
    }

    private void playVideo(String url) {
        binding.exoplayer.setVisibility(View.VISIBLE);
        player.clearMediaItems();
        player.addMediaItem(MediaItem.fromUri(url));
        player.prepare();
        player.play();
    }
}