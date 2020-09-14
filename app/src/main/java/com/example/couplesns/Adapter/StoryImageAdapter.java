package com.example.couplesns.Adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.couplesns.DataClass.StoryImageData;
import com.example.couplesns.R;

import java.util.ArrayList;

public class StoryImageAdapter extends RecyclerView.Adapter<StoryImageAdapter.StoryImageViewHolder>{
    ArrayList<StoryImageData> storyImageDataArrayList;
    Context context;

    public StoryImageAdapter(ArrayList<StoryImageData> storyImageDataArrayList, Context context) {
        this.storyImageDataArrayList = storyImageDataArrayList;
        this.context = context;
    }



    static class StoryImageViewHolder extends RecyclerView.ViewHolder{
        ImageView storyimage;
        public StoryImageViewHolder(@NonNull View itemView) {
            super(itemView);
             storyimage = (ImageView)itemView.findViewById(R.id.storyimage);

        }
    }


    @NonNull
    @Override
    public StoryImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_storyimagedata,parent,false);
        StoryImageViewHolder storyImageViewHolder = new StoryImageViewHolder(view);
        return storyImageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoryImageViewHolder holder, int position) {
        Log.d("가로 이미지들 스토리 어댑터", "onBindViewHolder: "+storyImageDataArrayList);
        Glide.with(holder.itemView).load(storyImageDataArrayList.get(position).getStoryimage()).fitCenter().into(holder.storyimage);
    }

    @Override
    public int getItemCount() {
        return (storyImageDataArrayList!=null ? storyImageDataArrayList.size():0);
    }

    //아이템을 한개 추가해주고싶을때
    public  void addItem(StoryImageData item){
        storyImageDataArrayList.add(item);
    }

    //한꺼번에 추가해주고싶을때
    public void addItems(ArrayList<StoryImageData> items){
        this.storyImageDataArrayList = items;
    }
}
