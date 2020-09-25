package com.example.testproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter
{
    List<Bitmap> list=new ArrayList<>();
    public MyRecyclerViewAdapter(List<Bitmap> list)
    {
        this.list=list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        myHolder myholder= (myHolder) holder;
//        Bitmap bitmap = BitmapFactory.decodeFile(list.get(position));
        myholder.textView.setImageBitmap(list.get(position));

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }
    public class myHolder extends RecyclerView.ViewHolder
    {
        ImageView textView;
        public myHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.item_image);
        }
    }
}
