package com.example.schooladmin.Notice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schooladmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class noticeAdapter extends RecyclerView.Adapter<noticeAdapter.noticeViewAdapter> {

    private Context context;
    private ArrayList<noticeData> list;

    public noticeAdapter(Context context, ArrayList<noticeData> list) {
        this.context = context;
        list = list;
    }

    @NonNull
    @Override
    public noticeViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.newsfeed_item_layout, parent, false);
        return new noticeViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull noticeViewAdapter holder, int position) {
        noticeData currentItem = list.get(position);

        holder.newsfeedDeleteNoticeTitle.setText(currentItem.getTitle());

        try {
            if (currentItem.getImage() != null)
                Picasso.get().load(currentItem.getImage()).into(holder.newsfeedDeleteNoticeImage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        holder.newsfeedDeleteNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Notice");
                databaseReference.child(currentItem.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Delete notice", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class noticeViewAdapter extends RecyclerView.ViewHolder {

        private Button newsfeedDeleteNotice;
        private TextView newsfeedDeleteNoticeTitle;
        private ImageView newsfeedDeleteNoticeImage;
        
        public noticeViewAdapter(@NonNull View itemView) {
            super(itemView);
            newsfeedDeleteNotice = itemView.findViewById(R.id.newsfeedDeleteNotice);
            newsfeedDeleteNoticeTitle = itemView.findViewById(R.id.newsfeedDeleteNoticeTitle);
            newsfeedDeleteNoticeImage = itemView.findViewById(R.id.newsfeedDeleteNoticeImage);
        }
    }
}
