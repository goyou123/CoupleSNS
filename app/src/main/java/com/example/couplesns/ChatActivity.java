package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.couplesns.Adapter.ChatAdapter;
import com.example.couplesns.DataClass.ChatData;
import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.DataClass.StoryImageData;
import com.example.couplesns.DataClass.ThreeStringData;
import com.example.couplesns.RetrofitJava.RetroCallback;

import java.io.BufferedReader;
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

import static com.example.couplesns.ApplicationClass.serverImageRoot;

public class ChatActivity extends AppCompatActivity {

    String TAG = "채팅액티비티";
    TextView Textview_Chat_Users;
    ImageView Imageview_Chat_Add;
    EditText Edittext_Chat_Write;
    Button Button_Chatting;
    Context context;

    ApplicationClass applicationClass;
    String couplekey,MyEmail, myname,read_result,myprofileimg,msg_type,getTime;

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

    TextView testview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        applicationClass = (ApplicationClass) getApplicationContext();

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

//        getChatRecyclerView();
        /*일단 저장생각하지말고 소켓 통신 되는지 확인하기기*/

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

        Log.d(TAG, "상대커플프로필에서 넘어온 인텐트 상대: " + other_idx1 + " / " + other_name1 + " / " + other_idx2 + " / " + other_name2);
        Log.d(TAG, "상대커플프로필에서 넘어온 인텐트 우리: " + our_idx1 + " / " + our_name1 + " / " + our_idx2 + " / " + our_name2);

        Textview_Chat_Users.setText(our_name1 + ", " + our_name2 + ", " + other_name1 + ", " + other_name2);
//        String addr = "192.168.147.1"; //VMWARE 1 ->이거라고..? // 이클립스
//        String addr = "192.168.30.1"; //VMWARE 8
//        String addr = "192.168.123.103"; // 무선 LAN 어댑터 WI-FI -> 네트워크 연결 없음 뜸
//        String addr = "116.37.162.156"; //네이버 검색  -> 쓰레드가 실행이 안됨 아예 (이게 맞는거 같긴 한데..)
//        String addr = "115.115.33.333.156"; //아무거나 테스트 -> 에러메세지 잘 뜸
        String addr = "13.125.182.117"; //VMWARE 1 ->이거라고..?
        /*채팅방에서 들어왔을 때 추가해야함*/
        //추가하세염


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

        /*클라이언트 소켓 생성하여 서버로 전송하는 쓰레드*/
        ConnectThread thread = new ConnectThread(addr);
        thread.start();


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
                sendmsg = Edittext_Chat_Write.getText().toString();
                read_result ="0";
                msg_type = "me";
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

                            String send = myname +"--"+ MyEmail +"--"+ sendmsg + "--" + getTime +"--"+ read_result +"--"+ myprofileimg +"--"+ msg_type ;
//                            sendWriter.println(our_name1 +" : "+ sendmsg); //서버로 메세지 보낼때 println
                            sendWriter.println(send); //이름

                            Log.d(TAG, "전송버튼 클릭시 서버로 이 메세지들을 보냅니다 : "+send);
                            sendWriter.flush(); //전송하고 남아있는 스트림을 싹 비운다 > 확인, 데이터보낼때마다 해줘야함
                            Edittext_Chat_Write.setText("");


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });


    }//onCreate



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

            /*채팅방리스트에서 이방으로 들어와야겠지? */
            if(chat[1].equals(MyEmail)){
                chatDataArrayList.add(new ChatData(chat[0],chat[1],chat[2],chat[3],chat[4],chat[5],chat[6]));
            } else {
                chatDataArrayList.add(new ChatData(chat[0],chat[1],chat[2],chat[3],chat[4],chat[5],"other"));
            }
//            Log.d(TAG, "run: ");
            recyclerView = findViewById(R.id.RCV_Chat);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(ChatActivity.this);
            recyclerView.setLayoutManager(layoutManager);

            chatAdapter = new ChatAdapter(chatDataArrayList, ChatActivity.this);
            chatAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(chatAdapter);
        }
    }


    /*온스탑일때 소켓과 inputstream 닫기*/
    @Override
    protected void onStop() {
        super.onStop();
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

                //outputstream을 할 수 있는 sendwritrer을 만들고 sendwritrer을 통해서 데이터를 보낸다. 보낼수 있게 생성해줌
                sendWriter = new PrintWriter(socket.getOutputStream()); //socket.getOutputStream() -> 데이터를 보냄


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
                Log.e(TAG, " 마지막 에러 e)" +e);
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

}
