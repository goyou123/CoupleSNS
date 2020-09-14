package com.example.couplesns.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.couplesns.DataClass.Viewpage_Img;
import com.example.couplesns.R;

public class ViewHolderPage extends RecyclerView.ViewHolder {

    ImageView Imageview_Viewpager1;
    Viewpage_Img data;
    TextView Textview_Viewpager1_Count;

    ViewHolderPage(View itemView) {
        super(itemView);
        Imageview_Viewpager1 = itemView.findViewById(R.id.Imageview_Viewpager1);
        Textview_Viewpager1_Count = itemView.findViewById(R.id.Textview_Viewpager1_Count);
    }

    public void onBind(Viewpage_Img data){
        //뷰 페이저의 이미지뷰에 이미지 데이터 넣기
        this.data = data;
        Glide.with(itemView.getContext()).load("http://13.125.182.117/img/storyimages/"+data.getImg()).fitCenter().into(Imageview_Viewpager1);

        String count = data.getCurrentPage()+"/"+data.getAppPage();
        Textview_Viewpager1_Count.setText(count);
    }
}
