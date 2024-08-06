package com.example.schooladmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class uploadImage extends AppCompatActivity {
    Toolbar toolbar;
    private CardView selectImage, addGalleryImage;
    Spinner imageCategory;
    private ImageView galleryImageView;
    private Button uploadImageBtn, selectImageBtn;
    private String category;
    private final int REQ = 1;
    private Bitmap bitmap;
    String downloadUrl = "";
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Gallery");
        storageReference = FirebaseStorage.getInstance().getReference().child("Gallery");

        pd = new ProgressDialog(this);

        selectImage = findViewById(R.id.addGalleryImage);
        imageCategory =findViewById(R.id.image_category);
        galleryImageView =findViewById(R.id.galleryImageView);
        uploadImageBtn =findViewById(R.id.uploadImageBtn);
        selectImageBtn = findViewById(R.id.selectImageBtn);

        //        Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        pd = new ProgressDialog(this);

        String[] items = new String[]{"Select Category","REPUBLIC DAY","INTERNATIONAL WOMEN’S DAY","INDEPENDENCE DAY","TEACHER’S DAY","Other Events"};
        imageCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));
        imageCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = imageCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap == null){
                    Toast.makeText(uploadImage.this, "please upload image", Toast.LENGTH_SHORT).show();
                }else if (category.equals("Select Category")){
                    Toast.makeText(uploadImage.this, "please select image category", Toast.LENGTH_SHORT).show();
                }else {
                    pd.setMessage("Uploading.......");
                    pd.show();
                    uploadimage();
                }
            }
        });


    }

    private void uploadimage() {
        pd.setMessage("Uploading......");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child(finalimg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(uploadImage.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    uploadData();
                                }
                            });
                        }
                    });
                }else {
                    pd.dismiss();
                    Toast.makeText(uploadImage.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadData() {
    databaseReference = databaseReference.child(category);
    final String uniqueKey = databaseReference.push().getKey();
//    String title = noticeTitle.getText().toString();
    databaseReference.child(uniqueKey).setValue(downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void unused) {
            pd.dismiss();
            Toast.makeText(uploadImage.this,"Image uploaded",Toast.LENGTH_SHORT).show();
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            pd.dismiss();
            Toast.makeText(uploadImage.this,"Something went wrong",Toast.LENGTH_SHORT).show();
        }
    });

    }

    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            galleryImageView.setImageBitmap(bitmap);
        }
    }
}