package com.example.schooladmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;


public class uploadE_Book extends AppCompatActivity {

    CardView addImage;
    private TextView pdfTextView;
    private EditText PdfTitle;
    private Button selectPdfBtn,uploadPdfBtn;
    private final int REQ = 1;
    private Uri PdfData ;
    private String pdfName, title;
    String downloadUrl = "";
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload_ebook);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);

        pdfTextView = findViewById(R.id.pdfTextView);
        PdfTitle = findViewById(R.id.PdfTitle);
        selectPdfBtn = findViewById(R.id.selectPdfBtn);
        uploadPdfBtn = findViewById(R.id.uploadPdfBtn);

        selectPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFiles();
            }
        });
        uploadPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = PdfTitle.getText().toString();
                if(title.isEmpty()){
                    PdfTitle.setError("Empty");
                    PdfTitle.requestFocus();
                } else if (PdfData == null) {
                    Toast.makeText(uploadE_Book.this, "Please upload pdf", Toast.LENGTH_SHORT).show();
                }else {
                    uploadPdf();
                }
            }
        });

//        toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    private void uploadPdf() {
        pd.setTitle("please wait...");
        pd.setMessage("Uploading....");
        pd.show();
        StorageReference reference = storageReference.child("pdf/"+pdfName+" , "+System.currentTimeMillis()+".pdf");
        reference.putFile(PdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (uriTask.isComplete());
                Uri uri = uriTask.getResult();
                uploadData(String.valueOf(uri));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(uploadE_Book.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData(String downloadUrl) {
        String uniqueKey = databaseReference.child("pdf").push().getKey();

        HashMap data = new HashMap();
        data.put("pdfTitle",title);
        data.put("pdfUri",downloadUrl);

        databaseReference.child("pdf").child(uniqueKey).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        pd.dismiss();
                        Toast.makeText(uploadE_Book.this, "pdf uploaded successfully", Toast.LENGTH_SHORT).show();
                        PdfTitle.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(uploadE_Book.this, "Failed to upload pdf", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFiles() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select pdf file"), REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ && resultCode == RESULT_OK){
            PdfData = data.getData();
            Toast.makeText(this, "pdf selected", Toast.LENGTH_SHORT).show();
            if(PdfData.toString().startsWith("content://")){
                Cursor cursor = null;
                try {
                    cursor = uploadE_Book.this.getContentResolver().query(PdfData,null,null,null,null);
                    if(cursor != null && cursor.moveToFirst()){
                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if (PdfData.toString().startsWith("file://")) {
                pdfName = new File(PdfData.toString()).getName();
            }
            pdfTextView.setText(pdfName);
        }
    }
}