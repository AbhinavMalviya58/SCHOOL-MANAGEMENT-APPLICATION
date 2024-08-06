package com.example.schooladmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.schooladmin.Faculty.uploadFaculty;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    CardView uploadNotice,addGalleryImage,addEbook,faculty,deleteNotice;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadNotice = findViewById(R.id.addNotice);
        addGalleryImage = findViewById(R.id.addGalleryImage);
        addEbook = findViewById(R.id.addEbook);
        faculty = findViewById(R.id.faculty);
        deleteNotice = findViewById(R.id.deleteNotice);

        uploadNotice.setOnClickListener(this);
        addGalleryImage.setOnClickListener(this);
        addEbook.setOnClickListener(this);
        faculty.setOnClickListener(this);
        deleteNotice.setOnClickListener(this);

//        toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onClick(View view) {

        Intent intent;

        switch (view.getId()){
            case R.id.addNotice:
                intent = new Intent(MainActivity.this, com.example.schooladmin.Notice.uploadNotice.class);
                startActivity(intent);
                break;
            case R.id.addGalleryImage:
                intent = new Intent(MainActivity.this,uploadImage.class);
                startActivity(intent);
                break;
            case R.id.addEbook:
                intent = new Intent(MainActivity.this,uploadE_Book.class);
                startActivity(intent);
                break;
            case R.id.faculty:
                intent = new Intent(MainActivity.this, uploadFaculty.class);
                startActivity(intent);
                break;
            case R.id.deleteNotice:
                intent = new Intent(MainActivity.this, com.example.schooladmin.Notice.deleteNotice.class);
                startActivity(intent);
                break;
        }
    }

}