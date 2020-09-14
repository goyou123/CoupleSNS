package com.example.couplesns.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.couplesns.DataClass.Viewpage_Img;
import com.example.couplesns.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewHolderPage>{
    ArrayList<Viewpage_Img> listData;

    ViewPagerAdapter(ArrayList<Viewpage_Img> data) {
        this.listData = data;
    }



    @NonNull
    @Override
    public ViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_viewpager, parent, false);
        return new ViewHolderPage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPage holder, int position) {
        ViewHolderPage viewHolder = (ViewHolderPage) holder;

        //ViewHolderPage의 onBind에서 데이터 세팅
        viewHolder.onBind(listData.get(position));

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
