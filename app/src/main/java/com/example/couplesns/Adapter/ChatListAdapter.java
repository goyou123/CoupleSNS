package com.example.couplesns.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.example.couplesns.DataClass.ThreeStringData;
import com.example.couplesns.R;
import com.example.couplesns.RetrofitJava.RetroCallback;
import com.example.couplesns.RetrofitJava.RetroClient;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>  {
    ArrayList<ChatListData> ChatListDataArrayList;
    Context context;
    ApplicationClass applicationClass;
    String TAG = "채팅리스트어댑터";
    static String serverImageRoot ="http://13.125.182.117/img/";
    RetroClient retroClient;

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
    public void onBindViewHolder(@NonNull final ChatListViewHolder holder, final int position) {


        String my_couplekey = ChatListDataArrayList.get(position).getMy_couplekey();
        String other_couplekey = ChatListDataArrayList.get(position).getOther_couplekey();

        applicationClass = (ApplicationClass) context.getApplicationContext();
        retroClient = applicationClass.retroInAdapter();
        Log.d(TAG, "채팅리스트에 저장된 내 커플키: "+my_couplekey);
        Log.d(TAG, "채팅리스트에 저장된 상대 커플키: "+other_couplekey);
        Log.d(TAG, "채팅리스트, 원래 내 커플키"+applicationClass.getShared_Couplekey());

        //채팅리스트에서 채팅방에 우리커플이름이 안나오오도록 처리
       if(other_couplekey.equals(applicationClass.getShared_Couplekey())){
            Log.d(TAG, "저장된 내 커플키와 저장된 상대 커플키가 같은경우 : ");
            retroClient.chat_get_othername(my_couplekey,new RetroCallback() {
                @Override
                public void onError(Throwable t) {
                    Log.d(TAG, "onError: "+t.toString());
                }

                @Override
                public void onSuccess(int code, Object receivedData) {
//                ArrayList chat_idx_List;
                    List<ThreeStringData> data = (List<ThreeStringData>)receivedData;
                    String other_name1 = data.get(0).getFirst();
                    String other_name2 = data.get(1).getFirst();
                    holder.ChatListData_Members.setText(other_name1+", "+other_name2);

                }

                @Override
                public void onFailure(int code) {
                    Log.d(TAG, "onFailure: "+code);
                }
            });

        }  else {
            Log.d(TAG, "일반적인 경우 : ");
            retroClient.chat_get_othername(other_couplekey,new RetroCallback() {
                @Override
                public void onError(Throwable t) {
                    Log.d(TAG, "onError: "+t.toString());
                }

                @Override
                public void onSuccess(int code, Object receivedData) {
//                ArrayList chat_idx_List;
                    List<ThreeStringData> data = (List<ThreeStringData>)receivedData;
                    String other_name1 = data.get(0).getFirst();
                    String other_name2 = data.get(1).getFirst();
                    holder.ChatListData_Members.setText(other_name1+", "+other_name2);

                }

                @Override
                public void onFailure(int code) {
                    Log.d(TAG, "onFailure: "+code);
                }
            });

        }





        holder.ChatListData_LastMessage.setText(ChatListDataArrayList.get(position).getLast_message());
        holder.ChatListData_Count.setText(ChatListDataArrayList.get(position).getCount());
//        int int_count = ChatListDataArrayList.get(position).getCount();
//        String count = Integer.toString(int_count);
//        holder.ChatListData_Count.setText(count);


        /*채팅방으로 이동*/
        holder.const1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String my_couplekey = ChatListDataArrayList.get(position).getMy_couplekey();
                String other_couplekey = ChatListDataArrayList.get(position).getOther_couplekey();
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("intent_my_couplekey",my_couplekey);
                intent.putExtra("intent_other_couplekey",other_couplekey);
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
