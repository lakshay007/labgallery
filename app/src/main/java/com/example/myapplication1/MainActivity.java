package com.example.myapplication1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private List<String> imagePaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize the image paths list
        imagePaths = new ArrayList<>();
        
        // Setup RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new ImageAdapter(imagePaths);
        recyclerView.setAdapter(adapter);

        // Check both READ_EXTERNAL_STORAGE and READ_MEDIA_IMAGES permissions
        if (checkPermissions()) {
            loadImages();
        } else {
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) 
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) 
                    == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImages();
            } else {
                Toast.makeText(this, "Permission denied. Cannot load images.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadImages() {
        imagePaths.clear();
        File dcimDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        
        Log.d(TAG, "DCIM directory exists: " + dcimDirectory.exists());
        Log.d(TAG, "Pictures directory exists: " + picturesDirectory.exists());
        
        addImagesFromDirectory(dcimDirectory, imagePaths);
        addImagesFromDirectory(picturesDirectory, imagePaths);

        Log.d(TAG, "Number of images found: " + imagePaths.size());
        if (imagePaths.size() > 0) {
            Log.d(TAG, "First image path: " + imagePaths.get(0));
        }

        // Notify adapter that data has changed
        adapter.notifyDataSetChanged();
        
        if (imagePaths.isEmpty()) {
            Toast.makeText(this, "No images found in DCIM or Pictures folders", Toast.LENGTH_LONG).show();
        }
    }

    private void addImagesFromDirectory(File directory, List<String> imagePaths) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && isImageFile(file.getName())) {
                        imagePaths.add(file.getAbsolutePath());
                        Log.d(TAG, "Added image: " + file.getAbsolutePath());
                    }
                }
            } else {
                Log.d(TAG, "No files found in directory: " + directory.getAbsolutePath());
            }
        } else {
            Log.d(TAG, "Directory does not exist: " + directory.getAbsolutePath());
        }
    }

    private boolean isImageFile(String fileName) {
        String extension = fileName.toLowerCase();
        return extension.endsWith(".jpg") || extension.endsWith(".jpeg") ||
               extension.endsWith(".png") || extension.endsWith(".gif");
    }
}
