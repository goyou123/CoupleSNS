package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.couplesns.Adapter.ChatAdapter;
import com.example.couplesns.Adapter.ChatListAdapter;
import com.example.couplesns.Adapter.CommentAdapter;
import com.example.couplesns.DataClass.ChatData;
import com.example.couplesns.DataClass.ChatListData;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatListActivity extends AppCompatActivity {

    String TAG = "채팅액티비티";
    TextView Textview_Chat_Users;

    //리사이클러뷰
    ArrayList<ChatListData> chatListDataArrayList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ChatListAdapter chatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        getChatListRCV();

    }//oncreate()

    public void getChatListRCV () {
        chatListDataArrayList = new ArrayList<>();
//        chatListAdapter.addItem(new ChatListData("1","Asd","asd",1,"Asd","asd"));
//        chatListAdapter.addItem(new ChatListData("1","Asd","asd",1,"Asd","asd"));
//        chatListAdapter.addItem(new ChatListData("1","Asd","asd",1,"Asd","asd"));


        chatListDataArrayList.add(0,new ChatListData("1","Asd","asd",1,"Asd","asd"));
        chatListDataArrayList.add(1,new ChatListData("1","Asd","asd",1,"Asd","asd"));
        chatListDataArrayList.add(2,new ChatListData("1","Asd","asd",1,"Asd","asd"));

        Log.d(TAG, "getChatListRCV: "+chatListDataArrayList.size());
        recyclerView = findViewById(R.id.RCV_ChatList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(ChatListActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        chatListAdapter = new ChatListAdapter(chatListDataArrayList,ChatListActivity.this);
        chatListAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(chatListAdapter);

    }//getChatListRCV()

}//END