package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.couplesns.Adapter.ChatAdapter;
import com.example.couplesns.Adapter.ChatListAdapter;
import com.example.couplesns.Adapter.CommentAdapter;
import com.example.couplesns.DataClass.ChatData;
import com.example.couplesns.DataClass.ChatListData;
import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.DataClass.StoryData;
import com.example.couplesns.RetrofitJava.RetroCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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
    String read;

    private Handler mHandler;
    InetAddress serverAddr;
    Socket socket;
    PrintWriter sendWriter;
    String addr;

    String roomID;


    private BroadcastReceiver chat_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {




            String get_update = intent.getStringExtra("UPDATE");
            roomID = intent.getStringExtra("roomID");
            if(get_update.equals("UPDATE")) {
                Log.d(TAG, "onReceive: (브로드)리사이클러뷰 업데이트 실행");

                //채팅방 리스트에 new 추가
                new_message_update(roomID);


                /*룸아이디를 받아와서, FCM이 오면 new 를 띄어준다. 해당 룸 번호에
                * chat_room의 해당 룸아이디 컬럼에 count값을 new로 바꾼다
                * 후에 채팅방에 들어가서 count로 바꾼다. */




            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        applicationClass = (ApplicationClass) getApplicationContext();
        //로그인한 유저의 이메일과 커플키
        couplekey = applicationClass.getShared_Couplekey();
        MyEmail = applicationClass.getShared_Email();
        Log.d(TAG, "로그인한유저의 커플키, 이메일: "+couplekey+" / "+MyEmail);

        addr = "13.125.182.117"; // 내 서버 IP





        Log.d(TAG, "onCreate: 브로드 전");
        /*서비스에서 브로드캐스트 리시버를 만들어서 수신한뒤 값이 있으면 리사이클러뷰 업데이트*/
        LocalBroadcastManager.getInstance(this).registerReceiver(
                chat_receiver, new IntentFilter("MoveServiceFilter")
        );

//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("MoveServiceFilter");
//        registerReceiver(chat_receiver,intentFilter);

        /*실시간 채팅방리스트 업데이트*/
        mHandler = new Handler();
        Log.d(TAG, "onCreate: 브로드 후");


//
//        ConnectThread thread = new ConnectThread(addr);
//        thread.start();







    }//oncreate()

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(applicationClass, "onResume", Toast.LENGTH_SHORT).show();
        getChatListRCV();
//           new Thread() {
//               @Override
//               public void run() {
//                   super.run();
//                   try {
//                       sendWriter = new PrintWriter(socket.getOutputStream()); //socket.getOutputStream() -> 데이터를 보냄
//                       sendWriter.println(couplekey); //이름
//                       sendWriter.flush();
//                       Log.d(TAG, "서버로 계속 커플키 전송......");
//
//                   } catch (Exception e) {
//                       e.printStackTrace();
//                   }
//               }
//           }.start();


    }

//    class ConnectThread extends Thread {
//        String hostname;
//
//        public ConnectThread(String addr) {
//            hostname = addr;
//        }
//
//        @Override
//        public void run() {
//            try {
//                //클라이언트 소켓 생성
//                int port = 2074;
//                socket = new Socket(hostname, port);
//                Log.d(TAG, "Socket 생성, 연결.");
//                Log.d(TAG, "run: 런 실행");
//
//
//                /*방 구분 1. 서버로 roomID를 보낸다.*/
//                //outputstream을 할 수 있는 sendwritrer을 만들고 sendwritrer을 통해서 데이터를 보낸다. 보낼수 있게 생성해줌
//                sendWriter = new PrintWriter(socket.getOutputStream()); //socket.getOutputStream() -> 데이터를 보냄
//                sendWriter.println(couplekey); //이름
//                sendWriter.flush(); //전송하고 남아있는 스트림을 싹 비운다 > 확인, 데이터보낼때마다 해줘야함
//
//
//
//
//
//                /*서버에서 수신되는 데이터들을 받는 곳곳*/
//                //듣는거, Input트림은 계속 돌아가고 있어야 데이터를 받는다.
//                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8")); //한글 깨짐 처리
//
//                while(true) {
//                    read = input.readLine();
//                    Log.d(TAG, "run: 리드: " + read);
//                    System.out.println("서버에서 받음 메세지 : " + read);
//                    if (read != null) {
//                        mHandler.post(new msgUpdate(read));
//                        Log.d(TAG, "서버에서 받아서 "+read+" 를 핸들러로");
////                        testview.setText(testview.getText().toString() + read + "\n"); //여기에 바로 적용못시킴 (쓰레드 )
//
//                    }
//
//
//                }
//
//            } catch (UnknownHostException uhe) {
//                // 소켓 생성 시 전달되는 호스트(www.unknown-host.com)의 IP를 식별할 수 없음.
//
//                Log.e(TAG, " 생성 Error : 호스트의 IP 주소를 식별할 수 없음. (잘못된 주소 값 또는 호스트 이름 사용)");
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        Toast.makeText(getApplicationContext(), "Error : 호스트의 IP 주소를 식별할 수 없음. (잘못된 주소 값 또는 호스트 이름 사용)", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            } catch (IOException ioe) {
//                // 소켓 생성 과정에서 I/O 에러 발생.
//
//                Log.e(TAG, " 생성 Error : 네트워크 응답 없음" +ioe.toString());
//                runOnUiThread(new Runnable() {
//                    public void run() {
////                        Toast.makeText(getApplicationContext(), "Error : 네트워크 응답 없음",
////                                Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//
//            } catch (SecurityException se) {
//                // security manager에서 허용되지 않은 기능 수행.
//
//                Log.e(TAG, " 생성 Error : 보안(Security) 위반에 대해 보안 관리자(Security Manager)에 의해 발생. (프록시(proxy) 접속 거부, 허용되지 않은 함수 호출)");
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        Toast.makeText(getApplicationContext(), "Error : 보안(Security) 위반에 대해 보안 관리자(Security Manager)에 의해 발생. (프록시(proxy) 접속 거부, 허용되지 않은 함수 호출)", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//
//            } catch (IllegalArgumentException le) {
//                // 소켓 생성 시 전달되는 포트 번호(65536)이 허용 범위(0~65535)를 벗어남.
//
//                Log.e(TAG, " 생성 Error : 메서드에 잘못된 파라미터가 전달되는 경우 발생. (0~65535 범위 밖의 포트 번호 사용, null 프록시(proxy) 전달)");
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        Toast.makeText(getApplicationContext(), " Error : 메서드에 잘못된 파라미터가 전달되는 경우 발생. (0~65535 범위 밖의 포트 번호 사용, null 프록시(proxy) 전달)", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//            } catch (Exception e){
//                Log.e(TAG, " 마지막 에러 e)" +e);
//            }
//
//
//        }
//
//    }








//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        try {
//            if (socket != null) {
//                sendWriter.close();
//                socket.close();
//            }
////            sendWriter.close();
////            socket.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d(TAG, "onStop: 온스탑에러" + e.toString());
//        }
//    }


    /*채팅리스트에 FCM이 오면 해당 룸 ID에 new를 추가해줌 */
    public void new_message_update(String roomID) {
        applicationClass.retroClient.chat_list_new(roomID, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code);
                Result_login data = (Result_login)receivedData;
                String update_ok = data.getServerResult();
                if(update_ok.equals("true")){
                    Log.d(TAG, "onSuccess: 채팅리스트에 new 추가 성공 ");
                    getChatListRCV();
                }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });


    }



    /*채팅 리스트를 서버에서 불러와 리사이클러뷰에 적용시키는 메소드*/
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




    }//getChatListRCV()




//    /*서버로부터 데이터를 받아와 그 데이터를 화면에 뿌려주는 (리사이클러뷰에 추가하는) 부분*/
//    class msgUpdate implements Runnable {
//        private String msg;
//        public msgUpdate(String str)
//        {this.msg=str;}
//
//        @Override
//        public void run() {
//            /*여기서 채팅방 리스트를 리사이클러뷰에 업데이트*/
//        }
//    }




















}//END