package com.example.schooladmin.Faculty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.squareup.picasso.Picasso;

import java.util.List;

public class teacherAdapter extends RecyclerView.Adapter<teacherAdapter.teacherViewAdapter> {

    private List<teacherData> list;
    private Context context;
    private String category;

    public teacherAdapter(List<teacherData> list, Context context,String category) {
        this.list = list;
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    public teacherViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.teacher_item, parent, false);

        return new teacherViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull teacherViewAdapter holder, int position) {
        teacherData item = list.get(position);
        holder.name.setText(item.getName());
        holder.email.setText(item.getEmail());
        holder.post.setText(item.getPost());
        try {
        Picasso.get().load(item.getImage()).into(holder.imageView);
        } catch (Exception e){
            e.printStackTrace();
        }

        holder.updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, updateTeacher.class);
//                Toast.makeText(context, "teacher Update", Toast.LENGTH_SHORT).show();
                intent.putExtra("name", item.getName());
                intent.putExtra("email", item.getEmail());
                intent.putExtra("post", item.getPost());
                intent.putExtra("image", item.getImage());
                intent.putExtra("key", item.getKey());
                intent.putExtra("category", category);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class teacherViewAdapter extends RecyclerView.ViewHolder {

        private TextView name, email, post;
        private Button updateInfo;
        private ImageView imageView;
        private Bitmap bitmap;
        public teacherViewAdapter(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.teacherName);
            email = itemView.findViewById(R.id.teacherEmail);
            post = itemView.findViewById(R.id.teacherPost);
            updateInfo = itemView.findViewById(R.id.updateInfo);
            imageView = itemView.findViewById(R.id.teacherImage);

        }
    }
}
