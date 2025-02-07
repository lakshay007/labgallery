package com.example.myapplication1;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.io.File;

public class FullScreenImageActivity extends AppCompatActivity {
    public static final String EXTRA_IMAGE_PATH = "image_path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        String imagePath = getIntent().getStringExtra(EXTRA_IMAGE_PATH);
        ImageView imageView = findViewById(R.id.fullScreenImageView);

        if (imagePath != null) {
            Glide.with(this)
                    .load(new File(imagePath))
                    .fitCenter()
                    .into(imageView);
        }
    }
} 