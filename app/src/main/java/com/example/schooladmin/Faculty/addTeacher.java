package com.example.schooladmin.Faculty;

import static android.provider.MediaStore.Images.Media.insertImage;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class addTeacher extends AppCompatActivity {

    private ProgressDialog pd;
    private ImageView addTeacherImage;
    private TextView teacherName, teacherEmail, teacherPost;
    private Spinner subject;
    private Button addTeacherBtn;
    private String category;
    private String name,email,post,downloadUrl = "";
    private final int REQ = 1;
    private Bitmap bitmap;
    private DatabaseReference databaseReference,dbref;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_teacher);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Teacher");
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);

        addTeacherImage = findViewById(R.id.addTeacherImage);
        teacherName = findViewById(R.id.teacherName);
        teacherEmail = findViewById(R.id.teacherEmail);
        teacherPost = findViewById(R.id.teacherPost);
        subject = findViewById(R.id.subject);
        addTeacherBtn = findViewById(R.id.addTeacherBtn);

        addTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        String[] items = new String[]{"Select subject","Hindi","English","Mathematics","Science","Social Studies","History","Geography",
                "Civics","Computer Science","Art and Craft","Moral Science/Value Education","Other Select"};
        subject.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));
        subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = subject.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void checkValidation() {
        name = teacherName.getText().toString();
        email = teacherEmail.getText().toString();
        post = teacherPost.getText().toString();
        
        if(name.isEmpty()){
            teacherName.setError("Empty");
            teacherName.requestFocus();
        } else if (email.isEmpty()) {
            teacherEmail.setError("Empty");
            teacherEmail.requestFocus();
        } else if (post.isEmpty()) {
            teacherPost.setError("Empty");
            teacherPost.requestFocus();
        } else if (category.equals("Select subject")) {
            Toast.makeText(this, "Please Select subject", Toast.LENGTH_SHORT).show();
        } else if (bitmap == null) {
            pd.setMessage("Uploading......");
            pd.show();
            insertData();
        }else {
            pd.setMessage("Uploading......");
            pd.show();
            insertImage();
        }
    }

    private void insertImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Teachers").child(finalimg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(addTeacher.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    insertData();
                                }
                            });
                        }
                    });
                }else {
                    pd.dismiss();
                    Toast.makeText(addTeacher.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void insertData() {
        dbref = databaseReference.child(category);
        final String uniqueKey = dbref.push().getKey();

//    Date
        Calendar Date = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(Date.getTime());

//     Time
        Calendar Time = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(Time.getTime());

        teacherData teacherData = new teacherData(name,email,post,date,time,downloadUrl,uniqueKey);

        dbref.child(uniqueKey).setValue(teacherData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(addTeacher.this,"Teacher added",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(addTeacher.this, uploadFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(addTeacher.this,"Something went wrong",Toast.LENGTH_SHORT).show();
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
            addTeacherImage.setImageBitmap(bitmap);
        }
    }
}