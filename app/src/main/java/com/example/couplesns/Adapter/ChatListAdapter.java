package com.example.couplesns.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.couplesns.ApplicationClass;
import com.example.couplesns.ChatActivity;
import com.example.couplesns.DataClass.ChatData;
import com.example.couplesns.DataClass.ChatListData;
import com.example.couplesns.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>  {
    ArrayList<ChatListData> ChatListDataArrayList;
    Context context;
    ApplicationClass applicationClass;
    String TAG = "채팅리스트어댑터";
    static String serverImageRoot ="http://13.125.182.117/img/";

    public ChatListAdapter(ArrayList<ChatListData> chatListDataArrayList, Context context) {
        this.ChatListDataArrayList = chatListDataArrayList;
        this.context = context;
    }

    static class ChatListViewHolder extends RecyclerView.ViewHolder {

        TextView ChatListData_Members,ChatListData_LastMessage,ChatListData_Count;
        ConstraintLayout const1;
        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            ChatListData_Members = (TextView)itemView.findViewById(R.id.ChatListData_Members);
            ChatListData_LastMessage = (TextView)itemView.findViewById(R.id.ChatListData_LastMessage);
            ChatListData_Count = (TextView)itemView.findViewById(R.id.ChatListData_Count);
            const1 = (ConstraintLayout)itemView.findViewById(R.id.const1);
        }
    }

    @NonNull
    @Override
    public ChatListAdapter.ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_chatlistdata,parent,false);
        ChatListViewHolder chatListViewHolder = new ChatListViewHolder(view);

        return chatListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListViewHolder holder, int position) {

        holder.ChatListData_Members.setText(ChatListDataArrayList.get(position).getOther_couple_name());
        holder.ChatListData_LastMessage.setText(ChatListDataArrayList.get(position).getLast_message());
        int int_count = ChatListDataArrayList.get(position).getCount();
        String count = Integer.toString(int_count);
        holder.ChatListData_Count.setText(count);


        /*채팅방으로 이동*/
        holder.const1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return (ChatListDataArrayList!=null ? ChatListDataArrayList.size():0);
    }


    public void addItem (ChatListData data){
        ChatListDataArrayList.add(data);
    }


}
