package com.example.resumescreening;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Image extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PAGE_SIZE = 3; // Number of images per page
    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private List<Uri> uploadedImages;
    private int currentPage = 1;
    private Button btnPrevious;
    private Button btnNext;
    private Button btnUpload;
    private TextView tvPageNumber;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        recyclerView = findViewById(R.id.recyclerView);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnUpload = findViewById(R.id.btnUpload);
        tvPageNumber = findViewById(R.id.tvPageNumber);
        progressBar = findViewById(R.id.progressBar);

        uploadedImages = new ArrayList<>(); // List to store uploaded image URIs

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UploadedImages", MODE_PRIVATE);
        gson = new Gson();

        // Retrieve stored images from SharedPreferences
        String jsonImages = sharedPreferences.getString("images", null);
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> storedImages = gson.fromJson(jsonImages, type);
        if (storedImages != null) {
            for (String uriString : storedImages) {
                uploadedImages.add(Uri.parse(uriString));
            }
        }

        adapter = new ImageAdapter(this, uploadedImages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Set the image delete listener in the adapter
        adapter.setOnImageDeleteListener(new ImageAdapter.OnImageDeleteListener() {
            @Override
            public void onImageDelete(int position) {
                deleteImage(position);
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 1) {
                    currentPage--;
                    updatePage();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalPages = (int) Math.ceil((double) uploadedImages.size() / PAGE_SIZE);
                if (currentPage < totalPages) {
                    currentPage++;
                    updatePage();
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    // Method to delete an image
    private void deleteImage(int position) {
        // Remove the image from the list
        uploadedImages.remove(position);

        // Save the updated list to SharedPreferences
        saveImagesToSharedPreferences();

        // Update the displayed images
        updateDisplayedImages();
    }

    private void updatePage() {
        updateDisplayedImages();
        updatePageNumber();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadImage(imageUri);
        }
    }

    private void uploadImage(Uri imageUri) {
        // Generate unique ID for the image
        String imageId = UUID.randomUUID().toString();

        // Add uploaded image URI to the list
        uploadedImages.add(0, imageUri);

        // Save updated list to SharedPreferences
        saveImagesToSharedPreferences();

        // Update page number and display uploaded images
        updatePage();
    }

    private void updateDisplayedImages() {
        int start = (currentPage - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, uploadedImages.size());
        List<Uri> displayedImages = new ArrayList<>();
        for (int i = start; i < end; i++) {
            displayedImages.add(uploadedImages.get(i));
        }
        adapter.setImages(displayedImages);
        adapter.notifyDataSetChanged();
    }

    private void updatePageNumber() {
        int totalPages = (int) Math.ceil((double) uploadedImages.size() / PAGE_SIZE);
        tvPageNumber.setText(getString(R.string.page_number, currentPage, totalPages));
        btnPrevious.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
    }

    private void saveImagesToSharedPreferences() {
        // Convert list of URIs to list of strings
        List<String> uriStrings = new ArrayList<>();
        for (Uri uri : uploadedImages) {
            uriStrings.add(uri.toString());
        }

        // Convert list to JSON and save to SharedPreferences
        String jsonImages = gson.toJson(uriStrings);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("images", jsonImages);
        editor.apply();
    }

}
