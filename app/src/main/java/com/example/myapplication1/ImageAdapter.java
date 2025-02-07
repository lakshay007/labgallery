package com.example.myapplication1;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.io.File;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private static final String TAG = "ImageAdapter";
    private List<String> imagePaths;

    public ImageAdapter(List<String> imagePaths) {
        this.imagePaths = imagePaths;
        Log.d(TAG, "ImageAdapter created with " + imagePaths.size() + " images");
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imagePath = imagePaths.get(position);
        Log.d(TAG, "Loading image at position " + position + ": " + imagePath);
        
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            Log.e(TAG, "Image file does not exist: " + imagePath);
            return;
        }

        Glide.with(holder.itemView.getContext())
                .load(imageFile)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .centerCrop()
                .into(holder.imageView);

        // Set click listener for the image
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), FullScreenImageActivity.class);
            intent.putExtra(FullScreenImageActivity.EXTRA_IMAGE_PATH, imagePath);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
} 