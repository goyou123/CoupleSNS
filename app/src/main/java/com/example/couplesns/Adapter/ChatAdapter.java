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
import com.example.couplesns.DataClass.ChatData;
import com.example.couplesns.R;
import com.example.couplesns.RetrofitJava.RetroClient;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    ArrayList<ChatData> ChatDataList;
    Context context;
    ApplicationClass applicationClass;
    RetroClient retroClient;
    String TAG = "채팅어댑터";
    static String serverImageRootchat ="http://13.125.182.117/img/chat_images/";
    static String serverImageRoot ="http://13.125.182.117/img/";

    public ChatAdapter() {
    }

    public ChatAdapter(ArrayList<ChatData> chatDataList, Context context) {
        ChatDataList = chatDataList;
        this.context = context;
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView Mychatdata_message,Mychatdata_time,Mychatdata_time2;

        TextView Otherchat_user_name,Otherchat_message,Otherchat_time,Otherchat_read_result,Otherchat_time2,Otherchat_read_result2;
        ImageView Otherchat_profileimg,Otherchat_image,Mychatdata_image;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            //내가 보내는 채팅
            Mychatdata_message = (TextView)itemView.findViewById(R.id.Mychatdata_message);
            Mychatdata_time = (TextView)itemView.findViewById(R.id.Mychatdata_time);
            Mychatdata_time2 = (TextView)itemView.findViewById(R.id.Mychatdata_time2);
            Mychatdata_image = (ImageView)itemView.findViewById(R.id.Mychatdata_image);

            //상대방 채팅
            Otherchat_profileimg = (ImageView)itemView.findViewById(R.id.Otherchat_profileimg);
            Otherchat_user_name = (TextView)itemView.findViewById(R.id.Otherchat_user_name);
            Otherchat_message = (TextView)itemView.findViewById(R.id.Otherchat_message);
            Otherchat_image = (ImageView)itemView.findViewById(R.id.Otherchat_image);
            Otherchat_time = (TextView)itemView.findViewById(R.id.Otherchat_time);
            Otherchat_time2 = (TextView)itemView.findViewById(R.id.Otherchat_time2);
            Otherchat_read_result = (TextView)itemView.findViewById(R.id.Otherchat_read_result);
            Otherchat_read_result2 = (TextView)itemView.findViewById(R.id.Otherchat_read_result2);


        }
    }


    @Override
    public int getItemViewType(int position) {

        applicationClass = (ApplicationClass) context.getApplicationContext();
        retroClient = applicationClass.retroInAdapter();

        int i = 0;
        String get_msg_type = ChatDataList.get(position).getMsg_type();
        String email = ChatDataList.get(position).getEmail();

        if(email.equals(applicationClass.getShared_Email())){
            get_msg_type ="me";
            if (get_msg_type.equals("me")) {
                i = 1;
            }

        }else{
            get_msg_type ="other";
            if (get_msg_type.equals("other")) {
                i = 2;
            }

        }



        Log.d("스토리 리사이클러뷰 뷰타입 결정", "getItemViewType: " + get_msg_type);

//        if (get_msg_type.equals("me")) {
//            i = 1;
//
//        } else if (get_msg_type.equals("other")) {
//            i = 2;
//
//        } else {
//            //후에 빼면됨 이상하면
//            i = 1;
//        }

        Log.d("스토리 리사이클러뷰 뷰타입 결정", "getItemViewType: "+i);

        return i;
    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
//        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_stroydata,parent,false);
        if(viewType==1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_chatdata_me,parent,false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_chatdata_another,parent,false);
        }
        ChatViewHolder chatViewHolder = new ChatViewHolder(view);

        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        applicationClass = (ApplicationClass) context.getApplicationContext();
        retroClient = applicationClass.retroInAdapter();

        String msg_type = ChatDataList.get(position).getMsg_type();
        Log.d(TAG, "onBindViewHolder: msg_type"+msg_type);
        String msg_info =  ChatDataList.get(position).getMsg_info();

        String email = ChatDataList.get(position).getEmail();
        if(email.equals(applicationClass.getShared_Email())){
            /*내가 보낸 채팅일 경우*/
            holder.Mychatdata_time.setText(ChatDataList.get(position).getChat_time());
            //이미지 일때의 처리
            if(msg_info.equals("img")){
                holder.Mychatdata_image.setVisibility(View.VISIBLE);
                holder.Mychatdata_message.setVisibility(View.INVISIBLE);

                holder.Mychatdata_time2.setVisibility(View.VISIBLE);
                holder.Mychatdata_time.setVisibility(View.INVISIBLE);

                Glide.with(holder.itemView).load(serverImageRootchat + ChatDataList.get(position).getMessage()).fitCenter().into(holder.Mychatdata_image);
                holder.Mychatdata_time2.setText(ChatDataList.get(position).getChat_time());

            }else if ( msg_info.equals("text")){

                holder.Mychatdata_message.setText(ChatDataList.get(position).getMessage());
                holder.Mychatdata_time.setText(ChatDataList.get(position).getChat_time());
            }

        }else {
            /*상대가 보낸 채팅일 경우*/
            Glide.with(holder.itemView).load(serverImageRoot+ChatDataList.get(position).
                    getUser_profile()).fitCenter().into(holder.Otherchat_profileimg);

            holder.Otherchat_user_name.setText(ChatDataList.get(position).getUser_name());


            if(msg_info.equals("img")){
                holder.Otherchat_image.setVisibility(View.VISIBLE);
                holder.Otherchat_message.setVisibility(View.INVISIBLE);

                holder.Otherchat_time2.setVisibility(View.VISIBLE);
                holder.Otherchat_time.setVisibility(View.INVISIBLE);

                holder.Otherchat_read_result2.setVisibility(View.VISIBLE);
                holder.Otherchat_read_result.setVisibility(View.INVISIBLE);


                Glide.with(holder.itemView).load(serverImageRootchat + ChatDataList.get(position).getMessage()).fitCenter().into(holder.Otherchat_image);
                holder.Otherchat_time2.setText(ChatDataList.get(position).getChat_time());
//                holder.Otherchat_read_result2.setText(ChatDataList.get(position).getRead_result()); ㅇ
                /*시간 옆 읽음처리 배치고!!!!!!!!!!!!!*/
            }else if ( msg_info.equals("text")){
                holder.Otherchat_message.setText(ChatDataList.get(position).getMessage());
                holder.Otherchat_time.setText(ChatDataList.get(position).getChat_time());
//                holder.Otherchat_read_result.setText(ChatDataList.get(position).getRead_result()); ㅇ
            }

        }

//        if(msg_type.equals("me")){
//            /*내가 보낸 채팅일 경우*/
//            holder.Mychatdata_message.setText(ChatDataList.get(position).getMessage());
//            holder.Mychatdata_time.setText(ChatDataList.get(position).getChat_time());
//
//        }
//        else if ( msg_type.equals("other")){
//            /*상대가 보낸 채팅일 경우*/
//            Glide.with(holder.itemView).load(serverImageRoot+ChatDataList.get(position).
//                    getUser_profile()).fitCenter().into(holder.Otherchat_profileimg);
//            holder.Otherchat_message.setText(ChatDataList.get(position).getMessage());
//            holder.Otherchat_user_name.setText(ChatDataList.get(position).getUser_name());
//            holder.Otherchat_time.setText(ChatDataList.get(position).getChat_time());
////            int i_result = ChatDataList.get(position).getRead_result();
////            String result = Integer.toString(i_result);
//            holder.Otherchat_read_result.setText(ChatDataList.get(position).getRead_result());
//
//        }

    }

    @Override
    public int getItemCount() {
        return (ChatDataList!=null ? ChatDataList.size():0);
    }


}
