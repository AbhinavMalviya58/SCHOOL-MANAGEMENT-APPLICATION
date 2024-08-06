package com.example.schooladmin.Faculty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schooladmin.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class uploadFaculty extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView hindi, English, Mathematics, Science, Social_Studies, History,
            Geography, Civics, computerScience, artAndCraft, moralScienceValueEducation, otherSelect;
    private LinearLayout hindiNoData, EnglishNoData, MathematicsNoData, ScienceNoData, socialStudiesNoData,
            HistoryNoData, GeographyNoData, CivicsNoData, computerScienceNoData, artAndCraftNoData, moralScienceValueEducationNoData, otherSelectNoData;
    private List<teacherData> hinsiList, EnglishList, mathematicsList, ScienceList, Social_StudiesList, HistoryList,
            GeographyList, CivicsList, computerScienceList, artAndCraftList, moralScienceValueEducationList, otherSelectList;
    private DatabaseReference databaseReference, dbRef;
    private StorageReference storageReference;
    private  teacherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_faculty);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Teacher");
        storageReference = FirebaseStorage.getInstance().getReference();

        fab = findViewById(R.id.fab);

        hindi = findViewById(R.id.hindi);
        English = findViewById(R.id.English);
        Mathematics = findViewById(R.id.Mathematics);
        Science = findViewById(R.id.Science);
        Social_Studies = findViewById(R.id.Social_Studies);
        History = findViewById(R.id.History);
        Geography = findViewById(R.id.Geography);
        Civics = findViewById(R.id.Civics);
        computerScience = findViewById(R.id.computerScience);
        artAndCraft = findViewById(R.id.artAndCraft);
        moralScienceValueEducation = findViewById(R.id.moralScienceValueEducation);
        otherSelect = findViewById(R.id.otherSelect);

        hindiNoData = findViewById(R.id.hindiNoData);
        EnglishNoData = findViewById(R.id.EnglishNoData);
        MathematicsNoData = findViewById(R.id.MathematicsNoData);
        ScienceNoData = findViewById(R.id.ScienceNoData);
        socialStudiesNoData = findViewById(R.id.socialStudiesNoData);
        HistoryNoData = findViewById(R.id.HistoryNoData);
        GeographyNoData = findViewById(R.id.GeographyNoData);
        CivicsNoData = findViewById(R.id.CivicsNoData);
        computerScienceNoData = findViewById(R.id.computerScienceNoData);
        artAndCraftNoData = findViewById(R.id.artAndCraftNoData);
        moralScienceValueEducationNoData = findViewById(R.id.moralScienceValueEducationNoData);
        otherSelectNoData = findViewById(R.id.otherSelectNoData);

        hindi();
        English();
        Mathematics();
        Science();
        Social_Studies();
        History();
        Geography();
        Civics();
        computerScience();
        artAndCraft();
        moralScienceValueEducation();
        otherSelect();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(uploadFaculty.this, addTeacher.class));
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    private void hindi() {
        dbRef = databaseReference.child("Hindi");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hinsiList = new ArrayList<>();
                if(!snapshot.exists()){
                    hindiNoData.setVisibility(View.VISIBLE);
                    hindi.setVisibility(View.GONE);
                }else {
                    hindiNoData.setVisibility(View.GONE);
                    hindi.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        teacherData data = snapshot1.getValue(teacherData.class);
                        hinsiList.add(data);
                    }
                    hindi.setHasFixedSize(true);
                    hindi.setLayoutManager(new LinearLayoutManager(uploadFaculty.this ));
                    adapter = new teacherAdapter(hinsiList,uploadFaculty.this, "Hindi");
                    hindi.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(uploadFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void English() {
        dbRef = databaseReference.child("English");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EnglishList = new ArrayList<>();
                if(!snapshot.exists()){
                    EnglishNoData.setVisibility(View.VISIBLE);
                    English.setVisibility(View.GONE);
                }else {
                    EnglishNoData.setVisibility(View.GONE);
                    English.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        teacherData data = snapshot1.getValue(teacherData.class);
                        EnglishList.add(data);
                    }
                    English.setHasFixedSize(true);
                    English.setLayoutManager(new LinearLayoutManager(uploadFaculty.this));
                    adapter = new teacherAdapter(EnglishList,uploadFaculty.this, "English");
                    English.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(uploadFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void Mathematics() {
        dbRef = databaseReference.child("Mathematics");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mathematicsList = new ArrayList<>();
                if(!snapshot.exists()){
                    MathematicsNoData.setVisibility(View.VISIBLE);
                    Mathematics.setVisibility(View.GONE);
                }else {
                    MathematicsNoData.setVisibility(View.GONE);
                    Mathematics.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        teacherData data = snapshot1.getValue(teacherData.class);
                        mathematicsList.add(data);
                    }
                    Mathematics.setHasFixedSize(true);
                    Mathematics.setLayoutManager(new LinearLayoutManager(uploadFaculty.this));
                    adapter = new teacherAdapter(mathematicsList,uploadFaculty.this, "Mathematics");
                    Mathematics.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(uploadFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void Science() {
        dbRef = databaseReference.child("Science");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ScienceList = new ArrayList<>();
                if(!snapshot.exists()){
                    ScienceNoData.setVisibility(View.VISIBLE);
                    Science.setVisibility(View.GONE);
                }else {
                    ScienceNoData.setVisibility(View.GONE);
                    Science.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        teacherData data = snapshot1.getValue(teacherData.class);
                        ScienceList.add(data);
                    }
                    Science.setHasFixedSize(true);
                    Science.setLayoutManager(new LinearLayoutManager(uploadFaculty.this));
                    adapter = new teacherAdapter(ScienceList,uploadFaculty.this, "Science");
                    Science.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(uploadFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void Social_Studies() {
        dbRef = databaseReference.child("Social Studies");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Social_StudiesList = new ArrayList<>();
                if(!snapshot.exists()){
                    socialStudiesNoData.setVisibility(View.VISIBLE);
                    Social_Studies.setVisibility(View.GONE);
                }else {
                    socialStudiesNoData.setVisibility(View.GONE);
                    Social_Studies.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        teacherData data = snapshot1.getValue(teacherData.class);
                        Social_StudiesList.add(data);
                    }
                    Social_Studies.setHasFixedSize(true);
                    Social_Studies.setLayoutManager(new LinearLayoutManager(uploadFaculty.this));
                    adapter = new teacherAdapter(Social_StudiesList,uploadFaculty.this, "Social Studies");
                    Social_Studies.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(uploadFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void History() {
        dbRef = databaseReference.child("History");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HistoryList = new ArrayList<>();
                if(!snapshot.exists()){
                    HistoryNoData.setVisibility(View.VISIBLE);
                    History.setVisibility(View.GONE);
                }else {
                    HistoryNoData.setVisibility(View.GONE);
                    History.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        teacherData data = snapshot1.getValue(teacherData.class);
                        HistoryList.add(data);
                    }
                    History.setHasFixedSize(true);
                    History.setLayoutManager(new LinearLayoutManager(uploadFaculty.this));
                    adapter = new teacherAdapter(HistoryList,uploadFaculty.this, "History");
                    History.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(uploadFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void Geography() {
        dbRef = databaseReference.child("Geography");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GeographyList = new ArrayList<>();
                if(!snapshot.exists()){
                    GeographyNoData.setVisibility(View.VISIBLE);
                    Geography.setVisibility(View.GONE);
                }else {
                    GeographyNoData.setVisibility(View.GONE);
                    Geography.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        teacherData data = snapshot1.getValue(teacherData.class);
                        GeographyList.add(data);
                    }
                    Geography.setHasFixedSize(true);
                    Geography.setLayoutManager(new LinearLayoutManager(uploadFaculty.this));
                    adapter = new teacherAdapter(GeographyList,uploadFaculty.this, "Geography");
                    Geography.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(uploadFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void Civics() {
        dbRef = databaseReference.child("Civics");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CivicsList = new ArrayList<>();
                if(!snapshot.exists()){
                    CivicsNoData.setVisibility(View.VISIBLE);
                    Civics.setVisibility(View.GONE);
                }else {
                    CivicsNoData.setVisibility(View.GONE);
                    Civics.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        teacherData data = snapshot1.getValue(teacherData.class);
                        CivicsList.add(data);
                    }
                    Civics.setHasFixedSize(true);
                    Civics.setLayoutManager(new LinearLayoutManager(uploadFaculty.this));
                    adapter = new teacherAdapter(CivicsList,uploadFaculty.this, "Civics");
                    Civics.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(uploadFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void computerScience() {
        dbRef = databaseReference.child("Computer Science");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                computerScienceList = new ArrayList<>();
                if(!snapshot.exists()){
                    computerScienceNoData.setVisibility(View.VISIBLE);
                    computerScience.setVisibility(View.GONE);
                }else {
                    computerScienceNoData.setVisibility(View.GONE);
                    computerScience.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        teacherData data = snapshot1.getValue(teacherData.class);
                        computerScienceList.add(data);
                    }
                    computerScience.setHasFixedSize(true);
                    computerScience.setLayoutManager(new LinearLayoutManager(uploadFaculty.this));
                    adapter = new teacherAdapter(computerScienceList,uploadFaculty.this, "Computer Science");
                    computerScience.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(uploadFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void artAndCraft() {
        dbRef = databaseReference.child("Art and Craft");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                artAndCraftList = new ArrayList<>();
                if(!snapshot.exists()){
                    artAndCraftNoData.setVisibility(View.VISIBLE);
                    artAndCraft.setVisibility(View.GONE);
                }else {
                    artAndCraftNoData.setVisibility(View.GONE);
                    artAndCraft.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        teacherData data = snapshot1.getValue(teacherData.class);
                        artAndCraftList.add(data);
                    }
                    artAndCraft.setHasFixedSize(true);
                    artAndCraft.setLayoutManager(new LinearLayoutManager(uploadFaculty.this));
                    adapter = new teacherAdapter(artAndCraftList,uploadFaculty.this,"Art and Craft");
                    artAndCraft.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(uploadFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void moralScienceValueEducation() {
        dbRef = databaseReference.child("Moral Science/Value Education");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                moralScienceValueEducationList = new ArrayList<>();
                if(!snapshot.exists()){
                    moralScienceValueEducationNoData.setVisibility(View.VISIBLE);
                    moralScienceValueEducation.setVisibility(View.GONE);
                }else {
                    moralScienceValueEducationNoData.setVisibility(View.GONE);
                    moralScienceValueEducation.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        teacherData data = snapshot1.getValue(teacherData.class);
                        moralScienceValueEducationList.add(data);
                    }
                    moralScienceValueEducation.setHasFixedSize(true);
                    moralScienceValueEducation.setLayoutManager(new LinearLayoutManager(uploadFaculty.this));
                    adapter = new teacherAdapter(moralScienceValueEducationList,uploadFaculty.this, "Moral Science/Value Education");
                    moralScienceValueEducation.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(uploadFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void otherSelect() {
        dbRef = databaseReference.child("Other Select");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                otherSelectList = new ArrayList<>();
                if(!snapshot.exists()){
                    otherSelectNoData.setVisibility(View.VISIBLE);
                    otherSelect.setVisibility(View.GONE);
                }else {
                    otherSelectNoData.setVisibility(View.GONE);
                    otherSelect.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        teacherData data = snapshot1.getValue(teacherData.class);
                        otherSelectList.add(data);
                    }
                    otherSelect.setHasFixedSize(true);
                    otherSelect.setLayoutManager(new LinearLayoutManager(uploadFaculty.this));
                    adapter = new teacherAdapter(otherSelectList,uploadFaculty.this, "Other Select");
                    otherSelect.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(uploadFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}