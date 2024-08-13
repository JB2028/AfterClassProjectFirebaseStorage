package com.example.afterclassprojectfirebasestorage;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView imageView;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSelectImage = findViewById(R.id.btnSelectImage);
        Button btnUploadImage = findViewById(R.id.btnUploadImage);
        imageView = findViewById(R.id.imageView);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        btnSelectImage.setOnClickListener(v -> openFileChooser());

        btnUploadImage.setOnClickListener(v -> uploadFile());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void uploadFile() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(MainActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(MainActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
}
