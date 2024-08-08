package com.example.schooladmin.Faculty;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.schooladmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class updateTeacher extends AppCompatActivity {

    private ImageView updateTeacherImage;
    private TextView updateTeacherName, updateTeacherEmail, updateTeacherPost;
    private Spinner updateSubject;
    private Button deleteTeacherBtn, updateTeacherBtn;
    private String name,email,post,Image,downloadUrl, uniqueKey, category;
    private Bitmap bitmap = null;
    private final int REQ = 1;
    private DatabaseReference databaseReference,dbref;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_teacher);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Teacher");
        storageReference = FirebaseStorage.getInstance().getReference();

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        post = getIntent().getStringExtra("post");
        Image = getIntent().getStringExtra("image");
        uniqueKey = getIntent().getStringExtra("key");
        category = getIntent().getStringExtra("category");

        updateTeacherImage = findViewById(R.id.updateTeacherImage);
        updateTeacherName = findViewById(R.id.updateTeacherName);
        updateTeacherEmail = findViewById(R.id.updateTeacherEmail);
        updateTeacherPost = findViewById(R.id.updateTeacherPost);
        updateSubject = findViewById(R.id.updateSubject);
        deleteTeacherBtn = findViewById(R.id.deleteTeacherBtn);
        updateTeacherBtn = findViewById(R.id.updateTeacherBtn);

        updateTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        updateTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = updateTeacherName.getText().toString();
                email = updateTeacherEmail.getText().toString();
                post = updateTeacherPost.getText().toString();

                checkValidation();
            }
        });

        deleteTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(updateTeacher.this, "hai", Toast.LENGTH_SHORT).show();
                deleteData();
            }
        });

        try {
            Picasso.get().load(Image).into(updateTeacherImage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        updateTeacherName.setText(name);
        updateTeacherEmail.setText(email);
        updateTeacherPost.setText(post);

    }

    private void deleteData() {
//        databaseReference.child(category).child(uniqueKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(updateTeacher.this, "Teacher Deleted Successfully", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(updateTeacher.this, uploadFaculty.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(updateTeacher.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//                    }
//                });
        databaseReference.child(category).child(uniqueKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(updateTeacher.this, "Teacher Deleted Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(updateTeacher.this, uploadFaculty.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(updateTeacher.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkValidation() {

        if(name.isEmpty()){
            updateTeacherName.setError("Empty");
            updateTeacherName.requestFocus();
        } else if (email.isEmpty()) {
            updateTeacherEmail.setError("Empty");
            updateTeacherEmail.requestFocus();
        } else if (post.isEmpty()) {
            updateTeacherPost.setError("Empty");
            updateTeacherPost.requestFocus();
        }else if (bitmap == null) {
            updateData("");
        }else {
            updateImage();
        }

    }

    private void updateImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Teachers").child(finalimg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(updateTeacher.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    updateData(downloadUrl);
                                }
                            });
                        }
                    });
                }else {
                    Toast.makeText(updateTeacher.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateData(String s) {
        HashMap hp = new HashMap();
        hp.put("name", name);
        hp.put("email", email);
        hp.put("post", post);
        hp.put("image", s);

        Log.d("updateData", "HashMap contents: " + hp.toString());

        databaseReference.child(category).child(uniqueKey).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(updateTeacher.this, "Teacher Updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(updateTeacher.this, uploadFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(updateTeacher.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);
    }
}