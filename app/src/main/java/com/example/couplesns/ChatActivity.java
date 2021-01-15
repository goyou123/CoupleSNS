package com.example.couplesns;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.couplesns.Adapter.ChatAdapter;
import com.example.couplesns.Adapter.ChatListAdapter;
import com.example.couplesns.DataClass.ChatData;
import com.example.couplesns.DataClass.ChatListData;
import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.DataClass.StoryImageData;
import com.example.couplesns.DataClass.ThreeStringData;
import com.example.couplesns.DataClass.UserData;
import com.example.couplesns.RetrofitJava.RetroCallback;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.couplesns.ApplicationClass.serverImageRoot;

public class ChatActivity extends AppCompatActivity {

    String TAG = "채팅액티비티";
    TextView Textview_Chat_Users;
    ImageView Imageview_Chat_Add;
    EditText Edittext_Chat_Write;
    Button Button_Chatting;
    Context context;

    ApplicationClass applicationClass;
    String couplekey,MyEmail, myname,read_result,myprofileimg,msg_type,getTime,msg_info;

    String chat[];

    //채팅관련
    private Handler mHandler;
    InetAddress serverAddr;
    Socket socket;
    PrintWriter sendWriter;
    private String ip = "192.168.30.1";
    //192.168.123.103
    //116.37.162.156
    private int port = 9348;
    String read;
    String sendmsg;
    String UserID;


    //리사이클러뷰
    ArrayList<ChatData> chatDataArrayList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ChatAdapter chatAdapter;

    String other_idx1;
    String other_idx2;
    String other_name1;
    String other_name2;
    String our_idx1;
    String our_idx2;
    String our_name1;
    String our_name2;
    String other_couplekey;
    String addr;
    String intent_my_couplekey,intent_other_couplekey; // 채팅리스트에서 넘어온 경우 인텐트
    HashMap tokens;

    String roomID;
    TextView testview;
    String roomCheck;
    String send;
    String first_send;
    String if_coupleAct_roomid;
    String first_roomid;

    String fcm_roomid;

    final static int REQUEST_IMAGE_CODE = 105;
    ArrayList<String> imageList;
    ArrayList<MultipartBody.Part> multiList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        applicationClass = (ApplicationClass) getApplicationContext();
        addr = "13.125.182.117"; // 내 서버 IP
        //로그인한 유저의 이메일과 커플키
        couplekey = applicationClass.getShared_Couplekey();
        MyEmail = applicationClass.getShared_Email();
        Log.d(TAG, "로그인한 유저의 커플키와 이메일: "+couplekey +" / "+ MyEmail);






        //XML 연결
        Textview_Chat_Users = (TextView) findViewById(R.id.Textview_Chat_Users);
        Imageview_Chat_Add = (ImageView) findViewById(R.id.Imageview_Chat_Add);
        Edittext_Chat_Write = (EditText) findViewById(R.id.Edittext_Chat_Write);
        Button_Chatting = (Button) findViewById(R.id.Button_Chatting);
        testview = (TextView) findViewById(R.id.testview);



        /**/
        chatDataArrayList = new ArrayList<>();


        mHandler = new Handler();


        /*위에 이름 처리 1. 상대커플프로필액티비티에서 넘어 왔을 때*/
        //상대방의 커플 프로필 액티비티에서 넘어 왔을 때
        Intent intent = getIntent();
        other_idx1 = intent.getStringExtra("other_idx1"); //상대방  1 idx
        other_idx2 = intent.getStringExtra("other_idx2"); //상대방 2 idx
        other_name1 = intent.getStringExtra("other_name1");// 상대방 1 이름
        other_name2 = intent.getStringExtra("other_name2"); // 상대방 2 이름
        our_idx1 = intent.getStringExtra("our_idx1");// 내 idx
        our_idx2 = intent.getStringExtra("our_idx2");// 내 커플 idx
        our_name1 = intent.getStringExtra("our_name1"); // 내 이름
        our_name2 = intent.getStringExtra("our_name2");// 내 커플 이름
        other_couplekey = intent.getStringExtra("other_couplekey"); //상대방의 커플 키

        Log.d(TAG, "상대커플프로필에서 넘어온 인텐트 상대: " + other_idx1 + " / " + other_name1 + " / " + other_idx2 + " / " + other_name2);
        Log.d(TAG, "상대커플프로필에서 넘어온 인텐트 우리: " + our_idx1 + " / " + our_name1 + " / " + our_idx2 + " / " + our_name2);
        Log.d(TAG, "상대커플프로필에서 넘어온 인텐트 커플키:  "+other_couplekey);


        /*채팅리스트에서 온 경우*/
        intent_my_couplekey = intent.getStringExtra("intent_my_couplekey");
        intent_other_couplekey = intent.getStringExtra("intent_other_couplekey");

        Log.d(TAG, "채팅리스트에서 넘어온 인텐트 내 커플키: " +intent_my_couplekey);
        Log.d(TAG, "채팅리스트에서 넘어온 인텐트 상대 커플키: "+intent_other_couplekey);


        /*FCM 노티피케이션을 통해 온경우*/
        fcm_roomid = intent.getStringExtra("fcm_roomidx");
        Log.d(TAG, "FCM에서 넘어온 룸 아이디 : "+fcm_roomid);

        if(fcm_roomid!=null){
            /*채팅내역 불러오기*/
            get_chattings(fcm_roomid);

            /*름 아이디로 커플키 2개 가져오기 FCM*/
            applicationClass.retroClient.chat_fcm_get_couplekeys(fcm_roomid, new RetroCallback() {
                @Override
                public void onError(Throwable t) {
                    Log.d(TAG, "onError: "+t.toString());
                }

                @Override
                public void onSuccess(int code, Object receivedData) {
                    Log.d(TAG, "onSuccess: "+code);
                    ThreeStringData data = (ThreeStringData)receivedData;
                    String fcm_couplekey = data.getFirst();
                    String fcm_other_couplekey = data.getSecond();
                    Log.d(TAG, "FCM 커플키: "+ fcm_couplekey + " / "+fcm_other_couplekey);

                    /*FCM 에서 받아온 룸아이디로 가져온 커플키들을 쉐어드에 저장했다가 후에 sendPOST에서 사용*/
                    SharedPreferences sharedPreferences = getSharedPreferences("CHAT_FCM_COUPLEKEYS",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("if_coupleAct_roomid",if_coupleAct_roomid);
                    editor.putString("fcm_couplekey",fcm_couplekey);
                    editor.putString("fcm_other_couplekey",fcm_other_couplekey);
                    editor.commit();


                    //정 안되면 이 부분 하드 코딩
                    /*가져온 커플키들을 활용해 이름 붙히기 */
                    if(fcm_other_couplekey.equals(applicationClass.getShared_Couplekey())){
                        applicationClass.retroClient.chat_getIdx(fcm_other_couplekey, fcm_couplekey,new RetroCallback() {
                            @Override
                            public void onError(Throwable t) {
                                Log.d(TAG, "onError: "+t.toString());
                            }

                            @Override
                            public void onSuccess(int code, Object receivedData) {

                                List<ThreeStringData> data = (List<ThreeStringData>)receivedData;
                                Log.d(TAG, "onSuccess: 채팅 IDX "+data);
                                String other_name1 = data.get(0).getSecond();
                                String other_name2 = data.get(1).getSecond();

                                String our_name1 = data.get(0).getFour();
                                String our_name2 = data.get(1).getFour();

                                Log.d(TAG, "상대 첫번째: "+ " / "+other_name1);
                                Log.d(TAG, "상대 두번째: "  + " / "+other_name2);


                                Log.d(TAG, "우리 첫번째: " + " / "+our_name1);
                                Log.d(TAG, "우리 두번째: " + " / "+our_name2);

                                Textview_Chat_Users.setText(our_name1 + ", " + our_name2 + ", " + other_name1 + ", " + other_name2);


                            }

                            @Override
                            public void onFailure(int code) {
                                Log.d(TAG, "onFailure: "+code);
                            }
                        });


                    }else {
                        applicationClass.retroClient.chat_getIdx(fcm_couplekey, fcm_other_couplekey,new RetroCallback() {
                            @Override
                            public void onError(Throwable t) {
                                Log.d(TAG, "onError: "+t.toString());
                            }

                            @Override
                            public void onSuccess(int code, Object receivedData) {

                                List<ThreeStringData> data = (List<ThreeStringData>)receivedData;
                                Log.d(TAG, "onSuccess: 채팅 IDX "+data);
                                String other_name1 = data.get(0).getSecond();
                                String other_name2 = data.get(1).getSecond();

                                String our_name1 = data.get(0).getFour();
                                String our_name2 = data.get(1).getFour();

                                Log.d(TAG, "상대 첫번째: "+ " / "+other_name1);
                                Log.d(TAG, "상대 두번째: "  + " / "+other_name2);


                                Log.d(TAG, "우리 첫번째: " + " / "+our_name1);
                                Log.d(TAG, "우리 두번째: " + " / "+our_name2);

                                Textview_Chat_Users.setText(our_name1 + ", " + our_name2 + ", " + other_name1 + ", " + other_name2);


                            }

                            @Override
                            public void onFailure(int code) {
                                Log.d(TAG, "onFailure: "+code);
                            }
                        });

                    }

                    // FCM - 룸아이디를 통해 불러온 커플키 2개가 있는 공간
                    /*불러온 후 소켓 연결 이부분 맞는지 잘 모르겠음 */
//                    ConnectThread thread = new ConnectThread(addr);
//                    thread.start();



                }

                @Override
                public void onFailure(int code) {
                    Log.d(TAG, "onFailure: "+code);
                }
            });


        }





        if(our_name1!= null && our_name2!=null  && other_name1!=null && other_name2!=null ) {
            //인텐트로 넘어온 데이터들이 있으면 상대커플프로필에서 들어온 경우 이름 설정
            Textview_Chat_Users.setText(our_name1 + ", " + our_name2 + ", " + other_name1 + ", " + other_name2);

        } else if(intent_my_couplekey!= null && intent_other_couplekey!=null){
            //채팅스트에서 커플키 2개를 인텐트로 받아온 경우

            if(intent_other_couplekey.equals(applicationClass.getShared_Couplekey())) {
                applicationClass.retroClient.chat_getIdx(intent_other_couplekey, intent_my_couplekey,new RetroCallback() {
                    @Override
                    public void onError(Throwable t) {
                        Log.d(TAG, "onError: "+t.toString());
                    }

                    @Override
                    public void onSuccess(int code, Object receivedData) {

                        List<ThreeStringData> data = (List<ThreeStringData>)receivedData;
                        Log.d(TAG, "onSuccess: 채팅 IDX "+data);
                        String other_name1 = data.get(0).getSecond();
                        String other_name2 = data.get(1).getSecond();

                        String our_name1 = data.get(0).getFour();
                        String our_name2 = data.get(1).getFour();

                        Log.d(TAG, "상대 첫번째: "+ " / "+other_name1);
                        Log.d(TAG, "상대 두번째: "  + " / "+other_name2);


                        Log.d(TAG, "우리 첫번째: " + " / "+our_name1);
                        Log.d(TAG, "우리 두번째: " + " / "+our_name2);

                        Textview_Chat_Users.setText(our_name1 + ", " + our_name2 + ", " + other_name1 + ", " + other_name2);
//                        final HashMap tokens2 = new HashMap();
//                        tokens2.put("our_name2",our_name2);
//                        tokens2.put("our_name1",our_name1);
//                        tokens2.put("other_name1",other_name1);
//                        tokens2.put("other_name2",other_name2);

                    }

                    @Override
                    public void onFailure(int code) {
                        Log.d(TAG, "onFailure: "+code);
                    }
                });

            }else {
                //채팅방 위 이름처리 - 이름의 순서를 맞추기 위해
                applicationClass.retroClient.chat_getIdx(intent_my_couplekey, intent_other_couplekey,new RetroCallback() {
                    @Override
                    public void onError(Throwable t) {
                        Log.d(TAG, "onError: "+t.toString());
                    }

                    @Override
                    public void onSuccess(int code, Object receivedData) {

                        List<ThreeStringData> data = (List<ThreeStringData>)receivedData;
                        Log.d(TAG, "onSuccess: 채팅 IDX "+data);
                        String other_name1 = data.get(0).getSecond();
                        String other_name2 = data.get(1).getSecond();

                        String our_name1 = data.get(0).getFour();
                        String our_name2 = data.get(1).getFour();

                        Log.d(TAG, "상대 첫번째: "+ " / "+other_name1);
                        Log.d(TAG, "상대 두번째: "  + " / "+other_name2);


                        Log.d(TAG, "우리 첫번째: " + " / "+our_name1);
                        Log.d(TAG, "우리 두번째: " + " / "+our_name2);

                        Textview_Chat_Users.setText(our_name1 + ", " + our_name2 + ", " + other_name1 + ", " + other_name2);

                    }

                    @Override
                    public void onFailure(int code) {
                        Log.d(TAG, "onFailure: "+code);
                    }
                });




            }
        }









//        String addr = "192.168.147.1"; //VMWARE 1 ->이거라고..? // 이클립스
//        String addr = "192.168.30.1"; //VMWARE 8
//        String addr = "192.168.123.103"; // 무선 LAN 어댑터 WI-FI -> 네트워크 연결 없음 뜸
//        String addr = "116.37.162.156"; //네이버 검색  -> 쓰레드가 실행이 안됨 아예 (이게 맞는거 같긴 한데..)
//        String addr = "115.115.33.333.156"; //아무거나 테스트 -> 에러메세지 잘 뜸

        /*이메일로 받아온 이름,프로필이미지  -> 후에 전송*/
        applicationClass.retroClient.chat_myname_myimg(MyEmail, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                ThreeStringData data = (ThreeStringData)receivedData;
                myname = data.getFirst();
                myprofileimg = data.getSecond();
                Log.d(TAG, "이메일통해불러온 내 이름, 프사: " + myname + " / "+myprofileimg);
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });

        /*날짜 가져오기 */
        //현재시간
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        getTime = simpleDate.format(mDate);

//        /*클라이언트 소켓 생성하여 서버로 전송하는 쓰레드*/
//        ConnectThread thread = new ConnectThread(addr);
//        thread.start();



        /*방 체크 */

        HashMap<String, Object> chatroom = new HashMap<>();

        if(fcm_roomid==null) {
            if (other_couplekey != null) {
                /*상대커플액티비티에서 온경우*/
                Log.d(TAG, ":방체크, 상대커플액티비티에서 온경우 ");
                chatroom.put("other_couplekey", other_couplekey);
                chatroom.put("my_couplekey", couplekey);
            } else {
                /*채팅리스트에서 넘어온 경우*/
                if (intent_other_couplekey.equals(applicationClass.getShared_Couplekey())) {
                    Log.d(TAG, ":방체크, 채팅리스트에서 온경우 + 순서꼬임 ");
                    //순서가 꼬인경우
                    chatroom.put("other_couplekey", intent_my_couplekey);
                    chatroom.put("my_couplekey", couplekey);
                } else {
                    //일반적인 경우
                    Log.d(TAG, ":방체크, 채팅리스트에서 온경우 + 일반적  ");
                    chatroom.put("other_couplekey", intent_other_couplekey);
                    chatroom.put("my_couplekey", couplekey);
                }
            }

        }

        applicationClass.retroClient.chat_room_check(chatroom, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code);
                Result_login data = (Result_login) receivedData;
                roomCheck = data.getServerResult();

                Log.d(TAG, "방이 있는지 : "+roomCheck);
                roomID = data.getCouplekey();

                if(data.getServerResult().equals("roomX")){
                    Log.d(TAG, "방 생성 X 기존방 불러오기 +룸아이디 :"+roomID);

                    /*기존 방 데이터 유지*/
                    get_chattings(roomID);

                    /*방 데이터 불러오고 나서 룸 리스트의 count값을 0으로 처리해줌 ->roomID사용*/
                    //업데이트 .. + 만약 카운트값이 0이면 안보이게게 ( 처리)
                    applicationClass.retroClient.chat_list_read(roomID, new RetroCallback() {
                        @Override
                        public void onError(Throwable t) {
                            Log.d(TAG, "onError: "+t.toString());
                        }

                        @Override
                        public void onSuccess(int code, Object receivedData) {
                            Log.d(TAG, "onSuccess: "+code);
                            Result_login data = (Result_login)receivedData;
                            String read_ok = data.getServerResult();
                            Log.d(TAG, "리스트의 카운트값을 0 으로 바꿔줌: "+read_ok);

                        }

                        @Override
                        public void onFailure(int code) {
                            Log.d(TAG, "onFailure: "+code);
                        }
                    });



                    /*클라이언트 소켓 생성하여 서버로 전송하는 쓰레드*/
                   ConnectThread thread = new ConnectThread(addr);
                    thread.start();





                }else if(data.getServerResult().equals("roomO")){
                    Log.d(TAG, "방 생성 O 새로 방만들기");


                    /*새로운 방 만들기*/
                    //방을 만들면 새로운 룸 번호가 생성된다.
                    make_chat_room();



                    //그 룸번호를 가져와서 쉐어드에 저장한뒤, 사용한다.
                    applicationClass.retroClient.chat_get_roomid(couplekey, other_couplekey, new RetroCallback() {
                        @Override
                        public void onError(Throwable t) {
                            Log.d(TAG, "onError: "+t.toString());
                        }

                        @Override
                        public void onSuccess(int code, Object receivedData) {
                            Log.d(TAG, "onSuccess: "+code);
                            Result_login data = (Result_login) receivedData;
                            roomCheck = data.getServerResult();

                            Log.d(TAG, "방이 있는지cccccccccc : "+roomCheck);
                            if_coupleAct_roomid = data.getCouplekey();
                            Log.d(TAG, "상대커플액-방금생성된룸아이디: if_coupleAct_roomid"+if_coupleAct_roomid);
                            SharedPreferences sharedPreferences = getSharedPreferences("ROOMID",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("if_coupleAct_roomid",if_coupleAct_roomid);
                            editor.commit();
                            Log.d(TAG, "상대커플액-방금생성된룸아이디: 쉐어드에 저장완료"+if_coupleAct_roomid);


                        }

                        @Override
                        public void onFailure(int code) {
                            Log.d(TAG, "onFailure???: "+code);
                        }
                    });

                    //원래 547줄에 있었음
                    /*클라이언트 소켓 생성하여 서버로 전송하는 쓰레드*/
                    ConnectThread thread = new ConnectThread(addr);
                    thread.start();

                 }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });








        //허가 코드..? https://ddangeun.tistory.com/31
//        int SDK_INT = android.os.Build.VERSION.SDK_INT;
//
//        if (SDK_INT > 8){
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }

//        new Thread() {
//            public void run() {
//                try {
//                    InetAddress serverAddr = InetAddress.getByName(ip);
////                    socket = new Socket(serverAddr, port); // 클라에서 연결 요청을 보낸거고 이떄 서버에서 accept가 실행되는거임
////                    116.37.162.156 이게 맞는듯
//                    socket = new Socket("116.37.162.156",port);
//
//                    Log.d(TAG, "run: 런 실행");
//                    //outputstream을 할 수 있는 sendwritrer을 만들고 sendwritrer을 통해서 데이터를 보낸다. 보낼수 있게 생성해줌
//                    sendWriter = new PrintWriter(socket.getOutputStream()); //socket.getOutputStream() -> 데이터를 보냄
//
//
//                    //듣는거, Input스트림은 계속 돌아가고 있어야 데이터를 받는다.
//                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                    while(true){
//                        read = input.readLine();
//                        Log.d(TAG, "run: 리드: "+read);
//                        System.out.println("서버에서 받음 메세지 : "+read);
//                        if(read!=null){
//                            mHandler.post(new msgUpdate(read));
//                        }
//                    }
//                }catch (UnknownHostException uhe) {
//                    // 소켓 생성 시 전달되는 호스트(www.unknown-host.com)의 IP를 식별할 수 없음.
//                    Log.e(TAG," 생성 Error : 호스트의 IP 주소를 식별할 수 없음.(잘못된 주소 값 또는 호스트이름 사용)");
//
//                } catch (IOException ioe) {
//                    // 소켓 생성 과정에서 I/O 에러 발생. 주로 네트워크 응답 없음.
//                    Log.e(TAG," 생성 Error : 네트워크 응답 없음 : "+ioe);
//
//                } catch (SecurityException se) {
//                    // security manager에서 허용되지 않은 기능 수행.
//                    Log.e(TAG," 생성 Error : 보안(Security) 위반에 대해 보안 관리자(Security Manager)에 의해 발생. (프록시(proxy) 접속 거부, 허용되지 않은 함수 호출)");
//                } catch (IllegalArgumentException le) {
//                    // 소켓 생성 시 전달되는 포트 번호(65536)이 허용 범위(0~65535)를 벗어남.
//                    Log.e(TAG," 생성 Error : 메서드에 잘못된 파라미터가 전달되는 경우 발생. (0~65535 범위 밖의 포트 번호 사용, null 프록시(proxy) 전달)");
//
//                }
//
//            }}.start();


        //버튼을 누르면 sendwriter로 데이터를 보냄 (sendwriter)안에 outputstream 있음
        //주의 할 것은, 안드로이드에서는 소켓 연결을 처리할 때 스레드를 사용하여야 합니다. 최근 버전에서는 스레드를 사용하지 않으면 네트워킹 기능 자체가 동작하지 않습니다.
        Button_Chatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences1 = getSharedPreferences("ROOMID",MODE_PRIVATE);
                first_roomid = sharedPreferences1.getString("if_coupleAct_roomid","no_roomID");
                Log.d(TAG, "상대커플프로필에서 들어와 룸 id가 없어서 새로 받아온 룸ID : " +first_roomid);

                sendmsg = Edittext_Chat_Write.getText().toString();
                read_result ="0";
                msg_type = "me";
                if(sendmsg.contains(".jpg")||sendmsg.contains(".png")){
                    msg_info = "img";
                }else {
                    msg_info = "text";
                }

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Log.d(TAG, "run: 메세지 전송 버튼 클릭시 이 로그가 실행됩니다.");







                            /*1. 서버로 보낼 값들을 정리한다. (이름,메세지,프사,시간,메세지 타입 )
                            * 2. 하나의 String으로 묶는다 (구분자 추가)
                            * 3. 서버로 불러온 다음 구분자별로 나눈다
                            * 4. 나눈 값들을 리사이클러뷰 어레이리스트에 추가한다. */


//                            if(roomID ==null){
//
////                                send = first_roomid +"--"+ myname +"--"+ MyEmail +"--"+ sendmsg + "--" + getTime +"--"+ read_result +"--"+ myprofileimg +"--"+ msg_type + "--" +msg_info ;
//                                if(fcm_roomid!=null){
//                                    //FCM에서 왔을 때
//                                    send = fcm_roomid +"--"+ myname +"--"+ MyEmail +"--"+ sendmsg + "--" + getTime +"--"+ read_result +"--"+ myprofileimg +"--"+ msg_type + "--" +msg_info ;
//                                }else {
//                                    //커플액에서 왔을때
//                                    send = first_roomid +"--"+ myname +"--"+ MyEmail +"--"+ sendmsg + "--" + getTime +"--"+ read_result +"--"+ myprofileimg +"--"+ msg_type + "--" +msg_info ;
//                                }
//                            }else {
//                                //채팅리스트에서 왔을 때
//                                send = roomID +"--"+ myname +"--"+ MyEmail +"--"+ sendmsg + "--" + getTime +"--"+ read_result +"--"+ myprofileimg +"--"+ msg_type + "--" +msg_info ;
//                            }

                            if(roomID ==null){
                                //FCM에서 왔을 때
                                if(fcm_roomid!=null){
                                    roomID=fcm_roomid;
                                    Log.d(TAG, "전송버튼 (커플액) FCM: "+roomID);
                                }else{
                                    //커플액에서 왔을때
                                    roomID=first_roomid;
                                    Log.d(TAG, "전송버튼 (커플액) 룸아이디: "+roomID);
                                }

                            }else {
                                //채팅리스트에서 왔을 때
                                roomID=roomID;
                                Log.d(TAG, "전송버튼 (커플액) 채팅리스트: "+roomID);
                            }



                            send = roomID +"--"+ myname +"--"+ MyEmail +"--"+ sendmsg + "--" + getTime +"--"+ read_result +"--"+ myprofileimg +"--"+ msg_type + "--" +msg_info ;
//                            String send = roomID +"--"+ myname +"--"+ MyEmail +"--"+ sendmsg + "--" + getTime +"--"+ read_result +"--"+ myprofileimg +"--"+ msg_type + "--" +msg_info ;
//                            sendWriter.println(our_name1 +" : "+ sendmsg); //서버로 메세지 보낼때 println
                            sendWriter.println(send); //이름

                            Log.d(TAG, "전송버튼 클릭시 서버로 이 메세지들을 보냅니다 : "+send);
                            sendWriter.flush(); //전송하고 남아있는 스트림을 싹 비운다 > 확인, 데이터보낼때마다 해줘야함
                            Edittext_Chat_Write.setText(null);


//                            /*채팅 전송하구 리사이클러뷰의 맨아래 부분이 보이도록*/
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    recyclerView.scrollToPosition(chatAdapter.getItemCount()-1);
//                                }
//                            },10);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                /*아니면 여기서 각종 if문 roomID*/
                sendPostToFCM(first_roomid,sendmsg,myname);
//                sendPostToFCM(roomID,sendmsg,myname);

            }
        });




        /*이미지전송*/
        Imageview_Chat_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "이미지 추가 버튼 클릭");
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true); //여러장 선택
                startActivityForResult(intent,REQUEST_IMAGE_CODE);

            }
        });

        Log.d(TAG, "onClick: 갤러리에서 나오면 여기 실행?");



    }//onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK && data != null){
            if(data.getClipData()==null){
                Toast.makeText(this, "다중선택이 안되는 기기입니다.", Toast.LENGTH_SHORT).show();
            }else {
                ClipData clipData = data.getClipData();
                Log.d(TAG, "onActivityResult: clipData "+clipData);
                if(clipData.getItemCount()>9){
                    Toast.makeText(this, "사진은 9장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show();


                }else if(clipData.getItemCount() > 0 && clipData.getItemCount() < 9) {
                    /*실제 여러장 추가 되는 곳*/
                    imageList = new ArrayList<>(); //사진 url 리스트
                    multiList = new ArrayList<>(); //서버 전송을 위한 Multipart형식의 리스트

                    for (int i = 0; i < clipData.getItemCount(); i++){
                        Log.d(TAG, "여러장일때 (9장가능): "+getPath(clipData.getItemAt(i).getUri()));

                        //갤러리에서 선택한 uri 리스트
                        final String addimg = getPath(clipData.getItemAt(i).getUri());

                        imageList.add(addimg);



                        /*서버에 저장하기위해 보낼 리스트*/
                        final File file = new File(addimg);
                        // uri파일을 리퀘스트바디에 담을 수 있는 multipart로 파싱
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file);
                        /*name속성의 값 끝에 [] 를 붙혀 배열로 전송한다.*/
                        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file[]",file.getName(),requestFile);
                        multiList.add(body);
                        Log.d(TAG, "선택한 사진들 이름: "+file.getName());

                        applicationClass.retroClient.chat_images_upload(multiList, new RetroCallback() {
                            @Override
                            public void onError(Throwable t) {
                                Log.d(TAG, "onError: "+t.toString());
                            }

                            @Override
                            public void onSuccess(int code, Object receivedData) {
                                Log.d(TAG, "onSuccess: "+code);
                                Result_login data = (Result_login)receivedData;
                                Log.d(TAG, "onSuccess: "+data);
                                //수정이 성공해서 이쪽으로 오면
                                Toast.makeText(applicationClass, "이미지들 전송이 완료되었습니다.", Toast.LENGTH_SHORT).show();
//                                finish();
                            }

                            @Override
                            public void onFailure(int code) {
                                Log.d(TAG, "onFailure: "+code);
                            }
                        });


                        //////////////////////////////////////
                        read_result ="0";
                        msg_type = "me";
                        msg_info = "img";

                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    Log.d(TAG, "챗: 갤러리에서 이미지 선택시 이 로그 실행.");
                                    String send = roomID +"--"+ myname +"--"+ MyEmail +"--"+ file.getName() + "--" + getTime +"--"+ read_result +"--"+ myprofileimg +"--"+ msg_type + "--" +msg_info ;
                                    sendWriter.println(send); //이름

                                    Log.d(TAG, " 챗 ,서버로 이미지가 속한 이 메세지들을 보냅니다 : "+send);
                                    sendWriter.flush(); //전송하고 남아있는 스트림을 싹 비운다 > 확인, 데이터보낼때마다 해줘야함
                                    Edittext_Chat_Write.setText("");
                                    Log.d(TAG, "챗 : 서버로 이미지 전송 완료 ( 이미지 갯수만큼)");


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                        
                        
                        
                        
                        
                        
                        /////////////////////////////////////////
                        
                        
                        
                        
                        



                    }

                }
            }


        }


    }//onActivityResult() / 갤러리, 이미지

    /*서버로부터 데이터를 받아와 그 데이터를 화면에 뿌려주는 (리사이클러뷰에 추가하는) 부분*/
    class msgUpdate implements Runnable {
        private String msg;
        public msgUpdate(String str)
        {this.msg=str;}

        @Override
        public void run() {
//            텍스트뷰에 붙히기
//            TextView testview = (TextView) findViewById(R.id.testview);
//            testview.setText(testview.getText().toString() + msg + "\n");
            Log.d(TAG, "msgUpdate: 텍스트 업데이트 실행 Runnable");

            /*리사이클러뷰에 추가 */
            Log.d(TAG, "서버에서 받아서 Runnable에 들어온 내용: " +msg);
            chat = msg.split("--");
            Log.d(TAG, "run: "+chat);
            /*채팅방리스트에서 이방으로 들어와야겠지? */
            if(chat[2].equals(MyEmail)){

                chatDataArrayList.add(new ChatData(chat[0],chat[1],chat[2],chat[3],chat[4],chat[5],chat[6],chat[7],chat[8]));
            } else {
                chatDataArrayList.add(new ChatData(chat[0],chat[1],chat[2],chat[3],chat[4],chat[5],chat[6],"other",chat[8]));
            }
//            Log.d(TAG, "run: ");
            recyclerView = findViewById(R.id.RCV_Chat);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(ChatActivity.this);
            recyclerView.setLayoutManager(layoutManager);

            chatAdapter = new ChatAdapter(chatDataArrayList, ChatActivity.this);
            chatAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(chatAdapter);

            /*채팅 전송하구 리사이클러뷰의 맨아래 부분이 보이도록*/
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.scrollToPosition(chatAdapter.getItemCount()-1);
                }
            },10);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        /*방 데이터 불러오고 나서 룸 리스트의 count값을 0으로 처리해줌 ->roomID사용*/
        //업데이트 .. + 만약 카운트값이 0이면 안보이게게 ( 처리)
        Log.d(TAG, "온스탑: "+roomID);
        applicationClass.retroClient.chat_list_read(roomID, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code);
                Result_login data = (Result_login)receivedData;
                String read_ok = data.getServerResult();
                Log.d(TAG, "(온스탑)리스트의 카운트값을 0 으로 바꿔줌: "+read_ok);

            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }

        });


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
    }


    /*뒤로가기 버튼 클릭 시 해당 방 카운트 0으로 변경 */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: 뒤로가기 클릭");
        Log.d(TAG, "뒤로가기: "+roomID);
        if(roomID==null){
            if(fcm_roomid!=null){
                roomID = fcm_roomid;
            }else {
                roomID = first_roomid;
            }
        }
        applicationClass.retroClient.chat_list_read(roomID, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code);
                Result_login data = (Result_login)receivedData;
                String read_ok = data.getServerResult();
                Log.d(TAG, "(뒤로가기)리스트의 카운트값을 0 으로 바꿔줌: "+read_ok);

            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });

    }

    /*온스탑일때 소켓과 inputstream 닫기*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (socket != null) {
                sendWriter.close();
                socket.close();
            }
//            sendWriter.close();
//            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "onStop: 온스탑에러" + e.toString());
        }
    }




    /*채팅 액티비티에 들어오면 바로 실행되는 쓰레드
    * 파라미터로 받은 IP주소로 클라이언트 소켓을 생성하고
    * outputsstream할수있는 객체를 만들고
    * while문으로 서버에서 돌아오는 값을 inputstream할수 있도록 한다.*/
    class ConnectThread extends Thread {
        String hostname;

        public ConnectThread(String addr) {
            hostname = addr;
        }

        @Override
        public void run() {
            try {
                //클라이언트 소켓 생성
                int port = 9348;
                socket = new Socket(hostname, port);
                Log.d(TAG, "Socket 생성, 연결.");
                Log.d(TAG, "run: 런 실행");


                /*방 구분 1. 서버로 roomID를 보낸다.*/
                //outputstream을 할 수 있는 sendwritrer을 만들고 sendwritrer을 통해서 데이터를 보낸다. 보낼수 있게 생성해줌
                sendWriter = new PrintWriter(socket.getOutputStream()); //socket.getOutputStream() -> 데이터를 보냄
                if(roomID==null){ /*룸아이디가 있는 경우는 채팅리스트에서 온 경우*/
//                    SharedPreferences sharedPreferences1 = getSharedPreferences("ROOMID",MODE_PRIVATE);
//                    first_roomid = sharedPreferences1.getString("if_coupleAct_roomid","no_roomID");
//                    first_send = first_roomid;
//                    Log.d(TAG, "서버랑 연결되면 처음 보내는 룸 아이디 (커프액) first: "+first_roomid);
//                    Log.d(TAG, "서버랑 연결되면 처음 보내는 룸 아이디: (커프액) "+first_send);
                    if(fcm_roomid!=null){
                        /*FCM에서 온경우*/
                        first_send = fcm_roomid;
                        Log.d(TAG, "서버랑 연결되면 처음 보내는 룸 아이디 (FCM) first: "+fcm_roomid);
                        Log.d(TAG, "서버랑 연결되면 처음 보내는 룸 아이디 (FCM) : "+first_send);
                    }else {
                        SharedPreferences sharedPreferences4 = getSharedPreferences("ROOMID",MODE_PRIVATE);
                        first_roomid = sharedPreferences4.getString("if_coupleAct_roomid","no_roomID");
                        first_send = first_roomid;
                        Log.d(TAG, "서버랑 연결되면 처음 보내는 룸 아이디 (커프액) first: "+first_roomid);
                        Log.d(TAG, "서버랑 연결되면 처음 보내는 룸 아이디: (커프액) "+first_send);
                    }


                }else {
                    first_send = roomID;
                    Log.d(TAG, "서버랑 연결되면 처음 보내는 룸 아이디: (채팅리스트) "+first_send);
                }


                sendWriter.println(first_send); //이름
                sendWriter.flush(); //전송하고 남아있는 스트림을 싹 비운다 > 확인, 데이터보낼때마다 해줘야함
//                Log.d(TAG, "2. 서버로 커플키 2개를 전송완료! : "+send);

                /*서버에서 수신되는 데이터들을 받는 곳곳*/
                //듣는거, Input트림은 계속 돌아가고 있어야 데이터를 받는다.
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8")); //한글 깨짐 처리

                while(true) {
                    read = input.readLine();
                    Log.d(TAG, "run: 리드: " + read);
                    System.out.println("서버에서 받음 메세지 : " + read);
                    if (read != null) {
                        mHandler.post(new msgUpdate(read));
                        Log.d(TAG, "서버에서 받아서 "+read+" 를 핸들러로");
//                        testview.setText(testview.getText().toString() + read + "\n"); //여기에 바로 적용못시킴 (쓰레드 )


                    }
                }

            } catch (UnknownHostException uhe) {
                // 소켓 생성 시 전달되는 호스트(www.unknown-host.com)의 IP를 식별할 수 없음.

                Log.e(TAG, " 생성 Error : 호스트의 IP 주소를 식별할 수 없음. (잘못된 주소 값 또는 호스트 이름 사용)");
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error : 호스트의 IP 주소를 식별할 수 없음. (잘못된 주소 값 또는 호스트 이름 사용)", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException ioe) {
                // 소켓 생성 과정에서 I/O 에러 발생.

                Log.e(TAG, " 생성 Error : 네트워크 응답 없음" +ioe.toString());
                runOnUiThread(new Runnable() {
                    public void run() {
//                        Toast.makeText(getApplicationContext(), "Error : 네트워크 응답 없음",
//                                Toast.LENGTH_SHORT).show();

                    }
                });


            } catch (SecurityException se) {
                // security manager에서 허용되지 않은 기능 수행.

                Log.e(TAG, " 생성 Error : 보안(Security) 위반에 대해 보안 관리자(Security Manager)에 의해 발생. (프록시(proxy) 접속 거부, 허용되지 않은 함수 호출)");
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error : 보안(Security) 위반에 대해 보안 관리자(Security Manager)에 의해 발생. (프록시(proxy) 접속 거부, 허용되지 않은 함수 호출)", Toast.LENGTH_SHORT).show();

                    }
                });


            } catch (IllegalArgumentException le) {
                // 소켓 생성 시 전달되는 포트 번호(65536)이 허용 범위(0~65535)를 벗어남.

                Log.e(TAG, " 생성 Error : 메서드에 잘못된 파라미터가 전달되는 경우 발생. (0~65535 범위 밖의 포트 번호 사용, null 프록시(proxy) 전달)");
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), " Error : 메서드에 잘못된 파라미터가 전달되는 경우 발생. (0~65535 범위 밖의 포트 번호 사용, null 프록시(proxy) 전달)", Toast.LENGTH_SHORT).show();

                    }
                });

            } catch (Exception e){
                Log.e(TAG, " 마지막 에러 e)" +e); // 알수 없는 에러
             }


        }

    }

    public void getChatRecyclerView() {
//        chatDataArrayList = new ArrayList<>();
//        /*채팅방리스트에서 이방으로 들어와야겠지? */
//        chatDataArrayList.add(0, new ChatData("1", "고은찬", "안녕하세요", "2020.12.19 13:00", 1, null, "me"));
//        chatDataArrayList.add(1, new ChatData("1", "김민선", "안녕하세요", "2020.12.19 13:00", 1, null, "other"));
//        chatDataArrayList.add(2, new ChatData("1", "고은찬", "안녕하세요", "2020.12.19 13:00", 1, null, "other"));
//
//        recyclerView = findViewById(R.id.RCV_Chat);
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(ChatActivity.this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        chatAdapter = new ChatAdapter(chatDataArrayList, ChatActivity.this);
//        chatAdapter.notifyDataSetChanged();
//        recyclerView.setAdapter(chatAdapter);

    } //getChatRecyclerView()

    /*기존에 채팅방이 없을 시 채팅방 만들어서 DB에 저장하는 메소드*/
    public void make_chat_room(){
        //1. DB에 채팅방을 새로 생성해준다.
        HashMap<String, Object> chatroom = new HashMap<>();
        chatroom.put("my_couplekey",couplekey);
        chatroom.put("other_couplekey",other_couplekey);

        applicationClass.retroClient.chat_room_add(chatroom, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code);
                Result_login data = (Result_login) receivedData;
                Log.d(TAG, "채팅방 생성 여부 : "+data.getServerResult());
                Log.d(TAG, "채팅방이 생성되었습니다");
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });
    }//make_chat_room()



    /*기존 채팅 내역 불러와서 리사이클러뷰에 뿌려주는 메서드*/
    public void get_chattings(String room_idx){
        applicationClass.retroClient.chat_get_chattings(room_idx, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code);
                List<ChatData> chattings= (List<ChatData>)receivedData;
                Log.d(TAG, "서버에서 받아온 채팅리스트: "+chattings);
                for (int i = 0; i<((List<ChatData>) receivedData).size(); i++){
                    chatDataArrayList.add(chattings.get(i));
                    Log.d(TAG, "onCreate: 채팅리스트"+chatDataArrayList);

                    recyclerView = findViewById(R.id.RCV_Chat);
                    recyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(ChatActivity.this);
                    recyclerView.setLayoutManager(layoutManager);

                    chatAdapter = new ChatAdapter(chatDataArrayList, ChatActivity.this);
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(chatAdapter);
                }



            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });




    }//get_chattings();


    /*이미지 절대 경로*/
    public String getPath(Uri uri){

        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);

    }


    /*FCM 메세지 전송*/
    public void sendPostToFCM (final String fcm_roomId, final String fcm_message, final String fcm_myname){
        final String SERVER_KEY = "AAAAuqVcYdM:APA91bHBJH1oxQKLFr3a7jeocU0cZKDjPKSoIBUcTmxIRke2OGYnm53FYNCH499mo2tfWjVlrHmoSuHj2Ohf2k-vBPrmcrmVAFQRtskuh8HvgTdk9HVPPJpxznJ4_Fk2sCNybYHMipsw";

        //커플액티비티에서 넘어왔을 경우에는 인텐트커플키들이 존재하지 않기때문에 값을 넣어주어야 한다.
        // + FCM도 고려


        if (intent_my_couplekey == null && intent_other_couplekey == null) {
            if(other_couplekey !=null){
                //커플액티비티에서 온 경우
                intent_other_couplekey = other_couplekey;
                intent_my_couplekey = couplekey;
                Log.d(TAG, "sendPostToFCM: 커플액에서 온 경우");
            }else{
                //FCM
                SharedPreferences sharedPreferences1 = getSharedPreferences("CHAT_FCM_COUPLEKEYS",MODE_PRIVATE);
                intent_other_couplekey = sharedPreferences1.getString("fcm_other_couplekey","no_fcm_other_couplekey");
                intent_my_couplekey = sharedPreferences1.getString("fcm_couplekey","no_fcm_my_couplekey");
                Log.d(TAG, "sendPostToFCM: FCM에서 온 경우");
            }

        }else {
            //채팅리스트에서 온 경우
            Log.d(TAG, "sendPostToFCM: 채팅리스트에서온 경우");
        }


        applicationClass.retroClient.chat_getIdx(intent_other_couplekey, intent_my_couplekey,new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "FCM 이방에있는 커플아이디들(인텐트) 나 :  "+intent_my_couplekey +"/ 상대 : "+intent_other_couplekey); // 잘 감
                List<ThreeStringData> data = (List<ThreeStringData>)receivedData;
                Log.d(TAG, "onSuccess: 채팅 IDX "+data);
                String other_name1 = data.get(0).getSecond();
                String other_name2 = data.get(1).getSecond();

                String our_name1 = data.get(0).getFour();
                String our_name2 = data.get(1).getFour();

                Log.d(TAG, "상대 첫번째: "+ " / "+other_name1);
                Log.d(TAG, "상대 두번째: "  + " / "+other_name2);


                Log.d(TAG, "우리 첫번째: " + " / "+our_name1);
                Log.d(TAG, "우리 두번째: " + " / "+our_name2);

//                Textview_Chat_Users.setText(our_name1 + ", " + our_name2 + ", " + other_name1 + ", " + other_name2);
                final HashMap tokens2 = new HashMap();
                tokens2.put("our_name1",our_name1);
                tokens2.put("our_name2",our_name2);
                tokens2.put("other_name1",other_name1);
                tokens2.put("other_name2",other_name2);
                Log.d(TAG, "이름해쉬: "+tokens2);

                //
                applicationClass.retroClient.chat_get_tokens(tokens2, new RetroCallback() {
                    @Override
                    public void onError(Throwable t) {
                        Log.d(TAG, "onError: "+ t.toString());
                    }

                    @Override
                    public void onSuccess(int code, Object receivedData) {
                        Log.d(TAG, "onSuccess: "+ code);
                        ThreeStringData data1 = (ThreeStringData)receivedData;
                        Log.d(TAG, "토큰값들 어레이: "+data1);
                        final String token1 = data1.getFirst();
                        final String token2 = data1.getSecond();
                        final String token3 = data1.getThird();
                        final String token4 = data1.getFour();

                        final String my_token = applicationClass.getShared_Token();

                        Log.d(TAG, "나의 토큰 (쉐어드): "+my_token);
                        Log.d(TAG, "토큰값1: "+token1);
                        Log.d(TAG, "토큰값1: "+token2);
                        Log.d(TAG, "토큰값1: "+token3);
                        Log.d(TAG, "토큰값1: "+token4);




                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                /*FCM 여러명한테 데이터 보내기*/
                                JSONObject body = new JSONObject();


                                ArrayList<String> tokenlist = new ArrayList<>();
                                if(!my_token.equals(token1)) {
                                    tokenlist.add(token1);
                                }
                                if(!my_token.equals(token2)) {
                                    tokenlist.add(token2);
                                }
                                if(!my_token.equals(token3)) {
                                    tokenlist.add(token3);
                                }
                                if(!my_token.equals(token4)) {
                                    tokenlist.add(token4);
                                }
//                                tokenlist.add(token1);
//                                tokenlist.add(token2);
//                                tokenlist.add(token3);
//                                tokenlist.add(token4);

                                JSONArray array = new JSONArray();

                                for (int i = 0; i < tokenlist.size(); i++){
                                    array.put(tokenlist.get(i));
                                    Log.d(TAG, "노티피케이션 받아온 토큰값들: "+tokenlist.get(i));
                                }

                                try {
                                    body.put("registration_ids",array);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


//                        JsonObject notification = new JsonObject();
//                        notification.addProperty("room_idx",fcm_roomId);
//                        notification.addProperty("title",fcm_myname);
//                        notification.addProperty("body",fcm_message);
//
//                        body.put(notification);

                                JSONObject notification = new JSONObject();

                                try {
                                    if(fcm_roomId.equals("no_roomID")){
                                        notification.put("room_idx",roomID);
                                    }else {
                                        notification.put("room_idx",fcm_roomId);
                                    }
                                    notification.put("title",fcm_myname);
                                    notification.put("body",fcm_message);
                                    Log.d(TAG, "run: 노티피케이션에 들어갈 데이터들"+notification);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
//                                    body.put("notification",notification);
                                    //여기서 data라고 해야지 나중에 서비스에서 값이 전달 됨 .
                                    body.put("data",notification);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.d(TAG, "JSON노티피케이션: "+body.toString());

                                //FCM서버로 전송하기 - 기존 Http API 방식
                                final MediaType mediaType = MediaType.parse("application/json");
                                OkHttpClient httpClient = new OkHttpClient();
                                try {

                                    Request request = new Request.Builder()
                                            .url("https://fcm.googleapis.com/fcm/send")
                                            .addHeader("Content-Type", "application/json; UTF-8")
                                            .addHeader("Authorization", "key=" + SERVER_KEY)
                                            .post(RequestBody.create(mediaType, body.toString())).build();
                                    Response response = httpClient.newCall(request).execute(); // 발송

                                    String res = response.body().string(); //, 요걸 리턴함

                                    Log.d(TAG, "notification response okhttp:전송 리퀘스트 "+request);
                                    Log.d(TAG, "notification response okhttp:전송 리스폰스"+response);
                                    Log.d(TAG, "notification response okhttp:전송확인"+res);
                                    /*에러 코드별로 로그에 찍힘 - ex)CODE:400 */
                                } catch (IOException e) {
                                    Log.d(TAG, "Error in sending message to FCM server: "+e);
//                                logger.info("Error in sending message to FCM server " + e);
                                }



                            }
                        }).start();

                    }

                    @Override
                    public void onFailure(int code) {
                        Log.d(TAG, "onFailure: "+code);
                    }
                });




            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });

        // 1. 보낼 상대들의 나 빼고 3명의 토큰값 가져오기
//        our_idx1,our_idx2,other_idx1,other_idx2;





        // 2.JSON 만들기




    }//sendPostToFCM()

}
