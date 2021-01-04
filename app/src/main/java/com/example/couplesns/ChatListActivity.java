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
import com.example.couplesns.DataClass.StoryData;
import com.example.couplesns.RetrofitJava.RetroCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    String TAG = "채팅리스트 액티비티";
    TextView Textview_Chat_Users;
    String couplekey,MyEmail;

    ApplicationClass applicationClass;

    //리사이클러뷰
    ArrayList<ChatListData> chatListDataArrayList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ChatListAdapter chatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        applicationClass = (ApplicationClass) getApplicationContext();
        //로그인한 유저의 이메일과 커플키
        couplekey = applicationClass.getShared_Couplekey();
        MyEmail = applicationClass.getShared_Email();
        Log.d(TAG, "로그인한유저의 커플키, 이메일: "+couplekey+" / "+MyEmail);

        getChatListRCV();

    }//oncreate()

    public void getChatListRCV () {
        chatListDataArrayList = new ArrayList<>();
        applicationClass.retroClient.chat_get_rooms(couplekey, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code);
                List<ChatListData> chatList= (List<ChatListData>)receivedData;
                Log.d(TAG, "서버에서 받아온 리스트: "+chatList);
                for (int i = 0; i<((List<ChatListData>) receivedData).size(); i++){

                    //댓글 리사이클러뷰에 보여줄 데이터 > 리스트에 추가
                    chatListDataArrayList.add(chatList.get(i));
                    Log.d(TAG, "onCreate: 채팅리스트"+chatListDataArrayList);

                    Log.d(TAG, "getChatListRCV: "+chatListDataArrayList.size());
                    recyclerView = findViewById(R.id.RCV_ChatList);
                    recyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(ChatListActivity.this);
                    recyclerView.setLayoutManager(layoutManager);

                    chatListAdapter = new ChatListAdapter(chatListDataArrayList,ChatListActivity.this);
                    chatListAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(chatListAdapter);
                }

            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });




//        chatListAdapter.addItem(new ChatListData("1","Asd","asd",1,"Asd","asd"));
//        chatListAdapter.addItem(new ChatListData("1","Asd","asd",1,"Asd","asd"));
//        chatListAdapter.addItem(new ChatListData("1","Asd","asd",1,"Asd","asd"));


//        chatListDataArrayList.add(0,new ChatListData("1","이충일,최민선","안녕",1,"Asd","asd"));
//        chatListDataArrayList.add(1,new ChatListData("1","유저3,유저4","asd",2,"Asd","asd"));
//        chatListDataArrayList.add(2,new ChatListData("1","유저5,유저6","asd",1,"Asd","asd"));
//
//        Log.d(TAG, "getChatListRCV: "+chatListDataArrayList.size());
//        recyclerView = findViewById(R.id.RCV_ChatList);
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(ChatListActivity.this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        chatListAdapter = new ChatListAdapter(chatListDataArrayList,ChatListActivity.this);
//        chatListAdapter.notifyDataSetChanged();
//        recyclerView.setAdapter(chatListAdapter);

    }//getChatListRCV()

}//END