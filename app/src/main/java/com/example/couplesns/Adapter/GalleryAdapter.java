package com.example.couplesns.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.couplesns.ApplicationClass;
import com.example.couplesns.CoupleprofileActivity;
import com.example.couplesns.DataClass.FollowData;
import com.example.couplesns.DataClass.GalleryData;
import com.example.couplesns.DataClass.StoryImageData;
import com.example.couplesns.R;
import com.example.couplesns.RetrofitJava.RetroClient;

import java.util.ArrayList;



public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>{

    ArrayList<GalleryData> GalleryDataList;
    Context context;
    ApplicationClass applicationClass;
    RetroClient retroClient;
    String serverImageRoot ="http://13.125.182.117/img/storyimages/";
    static String defaultProfile = "http://13.125.182.117/img/default_profile.png";
    String TAG = "갤러리어댑터";

    public GalleryAdapter(ArrayList<GalleryData> galleryDataList, Context context) {
        GalleryDataList = galleryDataList;
        this.context = context;
    }


    static class GalleryViewHolder extends RecyclerView.ViewHolder {
        ImageView Gallerydata_Image;
        TextView TEXT1;
        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            Gallerydata_Image = (ImageView)itemView.findViewById(R.id.Gallerydata_Image);
            TEXT1 = (TextView)itemView.findViewById(R.id.TEXT);
        }
    }




    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_gallerydata,parent,false);
        GalleryViewHolder galleryViewHolder = new GalleryViewHolder(view);
        return galleryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, final int position) {
        applicationClass = (ApplicationClass) context.getApplicationContext();

        /*사진1.jgp--사진2.jpg 이렇게 저장되있는 걸 어떻게 리스트로 쪼갤까? */
        String galleryImage = GalleryDataList.get(position).getImages();
        Log.d(TAG, "onBindViewHolder: "+galleryImage);

        final String imgs[] = galleryImage.split("--");
        Log.d(TAG, "onBindViewHolder 분할: "+imgs);
//        Glide.with(holder.itemView).load(serverImageRoot+galleryImage).into(holder.Gallerydata_Image);

        ArrayList gi = new ArrayList<>();

        final int img_pos = imgs.length - position; //이미지 위치
        for (int i=0; i<imgs.length; i++){
            Log.d("이미지들분할", "onBindViewHolder: "+serverImageRoot+imgs[i]);
//            gi.add(new StoryImageData(serverImageRoot+imgs[i]));

            Glide.with(holder.itemView).load(serverImageRoot+imgs[0]).override(600,600).into(holder.Gallerydata_Image);
            if(imgs.length==1){
                holder.TEXT1.setText("no more");
            }else{
                int j =imgs.length-1;
                holder.TEXT1.setText("+"+j);
            }
        }


        /*이미지 클릭시 원하는 리사이클러뷰 위치로 이동하기*/
        holder.Gallerydata_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CoupleprofileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("position",position);
                intent.putExtra("size",GalleryDataList.size());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return (GalleryDataList!=null ? GalleryDataList.size():0);
    }







}
