package com.example.couplesns.Adapter;

import android.content.Context;
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
import com.example.couplesns.DataClass.CommentData;
import com.example.couplesns.DataClass.StoryData;
import com.example.couplesns.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    ArrayList<CommentData> CommentDataList;
    Context context;
    ApplicationClass applicationClass;
    static String serverImageRoot ="http://13.125.182.117/img/";

    public CommentAdapter(ArrayList<CommentData> CommentDataList,Context context) {
        this.CommentDataList = CommentDataList;
        this.context = context;
    }


    static class CommentViewHolder extends RecyclerView.ViewHolder{
        ImageView Commentdata_profileimg;
        TextView Commentdata_Name,Commentdata_Memo,Commentdata_Date,Commentdata_Reply;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            Commentdata_profileimg =(ImageView) itemView.findViewById(R.id.Commentdata_profileimg);
            Commentdata_Name =(TextView) itemView.findViewById(R.id.Commentdata_Name);
            Commentdata_Memo =(TextView) itemView.findViewById(R.id.Commentdata_Memo);
            Commentdata_Date =(TextView) itemView.findViewById(R.id.Commentdata_Date);
            Commentdata_Reply =(TextView) itemView.findViewById(R.id.Commentdata_Reply);

        }
    }


    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //XML과 뷰홀더 연결
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_commentdata,parent,false);
        CommentViewHolder commentViewHolder = new CommentViewHolder(view);
        return commentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        //애플리케이션 클래스 연결
        applicationClass = (ApplicationClass) context.getApplicationContext();

        //댓글 리사이클러뷰 데이터 불러와서 보여주기
        Glide.with(holder.itemView).load(serverImageRoot + CommentDataList.get(position).getWriterimg()).fitCenter()
                .into(holder.Commentdata_profileimg);
        holder.Commentdata_Name.setText(CommentDataList.get(position).getWriter());
        holder.Commentdata_Memo.setText(CommentDataList.get(position).getMemo());
//        holder.Commentdata_Reply.setText(CommentDataList.get(position).getWriter());

        //스트링으로 저장된 날짜 값 long으로 만들기
        String commentDate = CommentDataList.get(position).getCommentdate();
        long getdate = new Long(commentDate);
        Log.d("커맨트어댑터 온바인드", "onBindViewHolder:롱타입 데이트 "+getdate);

        //long타입 다시 String으로 변환 (방금전..1분전 )
        String setTime = applicationClass.formatTimeString(getdate);
        Log.d("커맨트어댑터 온바인드", "onBindViewHolder:스트링타입 데이트 "+setTime);
        holder.Commentdata_Date.setText(setTime);

    }

    @Override
    public int getItemCount() {
        return (CommentDataList!=null ? CommentDataList.size():0);
    }


}
