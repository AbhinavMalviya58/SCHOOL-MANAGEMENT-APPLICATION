package com.example.schooladmin.Notice;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class deleteNotice extends AppCompatActivity {

    private RecyclerView RecyclerViewDeleteNotice;
    private ProgressBar progressBarDeleteNotice;
    private noticeAdapter adapter;
    private ArrayList<noticeData> dataList;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_notice);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        RecyclerViewDeleteNotice = findViewById(R.id.RecyclerViewDeleteNotice);
        progressBarDeleteNotice = findViewById(R.id.progressBarDeleteNotice);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notice");

        RecyclerViewDeleteNotice.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewDeleteNotice.setHasFixedSize(true);

        getNotice();
    }

    private void getNotice() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    noticeData data = dataSnapshot.getValue(noticeData.class);
                    dataList.add(data);
                }

                adapter = new noticeAdapter(deleteNotice.this, dataList);
                adapter.notifyDataSetChanged();
                progressBarDeleteNotice.setVisibility(View.GONE);
                RecyclerViewDeleteNotice.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBarDeleteNotice.setVisibility(View.GONE);
                Toast.makeText(deleteNotice.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}