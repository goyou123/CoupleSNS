package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.couplesns.Adapter.StoryAdapter;
import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.DataClass.StoryData;
import com.example.couplesns.DataClass.ThreeStringData;
import com.example.couplesns.DataClass.UserData;
import com.example.couplesns.RetrofitJava.RetroCallback;
import com.example.couplesns.RetrofitJava.RetroClient;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import okhttp3.RequestBody;

public class RealMainActivity extends AppCompatActivity {
    final static String TAG = "메인액티비티-데이트피커";
    ImageView Imageview_Main_Sound,Imageview_Main_Setting,Imageview_Main_Myprofile
            ,Imageview_Main_Anotherprofile,Imageview_Main_WriteStory;
    TextView Textview_Main_Date,Textview_Main_Myname,Textview_Main_Anothername;
    Button Button_Main_Home,Button_Main_Chatting,Button_Main_OurProfile;
    EditText Edittext_Main_Search;

    //레트로핏 관련 변수
    ApplicationClass applicationClass;
    String getEmail;
    String autoLoginKey;
    String myEmail,coupleKey;

    //전체보기 리사이클러뷰 변수
    ArrayList<StoryData> storyDataArrayList,storyDataArrayList2,storyDataArrayList3,storyDataArrayList4;
    RecyclerView recyclerView,recyclerView2,recyclerView3,recyclerView4;
    RecyclerView.LayoutManager layoutManager,layoutManager2,layoutManager4,layoutManager3;
    StoryAdapter storyAdapter,storyAdapter2,storyAdapter3,storyAdapter4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_main);

        //applicationClass
        applicationClass = (ApplicationClass) getApplicationContext();


        //xml연결
        Imageview_Main_Sound = (ImageView) findViewById(R.id.Imageview_Main_Sound); // 음성지원아이콘
        Imageview_Main_Setting = (ImageView) findViewById(R.id.Imageview_Main_Setting); // 세팅아이콘
        Imageview_Main_Myprofile = (ImageView) findViewById(R.id.Imageview_Main_Myprofile); // 내 프로필
        Imageview_Main_Anotherprofile = (ImageView) findViewById(R.id.Imageview_Main_Anotherprofile); //상대방 프로필
        Imageview_Main_WriteStory = (ImageView) findViewById(R.id.Imageview_Main_WriteStory); //글쓰기 이미지버튼
        Textview_Main_Date = (TextView) findViewById(R.id.Textview_Main_Date); //사귄날 표시
        Textview_Main_Myname = (TextView) findViewById(R.id.Textview_Main_Myname); //내 이름
        Textview_Main_Anothername = (TextView) findViewById(R.id.Textview_Main_Anothername); // 상대방 이름
        Button_Main_Home = (Button) findViewById(R.id.Button_Main_Home); // 홈 버튼
        Button_Main_Chatting = (Button) findViewById(R.id.Button_Main_Chatting); // 채팅 버튼
        Button_Main_OurProfile = (Button) findViewById(R.id.Button_Main_OurProfile); //내 피드 버튼
//        Edittext_Main_Search = (EditText) findViewById(R.id.Edittext_Main_Search); // 검색 입력칸

        SharedPreferences sharedPreferences1 = getSharedPreferences("autologin",MODE_PRIVATE);
        myEmail = sharedPreferences1.getString("auto_login","no_autologin_key");
        Log.d("애플리케이션클래스에서 가져온", "CoupleprofileActivity: autoLoginKey::"+myEmail);

        SharedPreferences sharedPreferences = getSharedPreferences("CoupleKey",MODE_PRIVATE);
        coupleKey = sharedPreferences.getString(myEmail,"no_key_login");
        Log.d("애플리케이션클래스에서 가져온", "CoupleprofileActivity: coupleKey::"+coupleKey);



        /*탭 레이아웃 관련설정*/
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // TODO : process tab selection event.
                //탭 선택시 관련 레이아웃 보이기 / 안보이기
                int pos = tab.getPosition();
                changeView(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
        });











        //글쓰기 이미지 클릭 - 다이얼로그
        Imageview_Main_WriteStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Write_Dialog writeDialog = new Write_Dialog(RealMainActivity.this);
                writeDialog.callFunction();
            }
        });


        //음성지원
        Imageview_Main_Sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(applicationClass, "음성지원서비스 예정", Toast.LENGTH_SHORT).show();
            }
        });

        //세팅 화면으로 이동
        Imageview_Main_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
                startActivity(intent);
            }
        });


        //우리 피드로 이동1
        Imageview_Main_Myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CoupleprofileActivity.class);
                startActivity(intent);
            }
        });
        //우리 피드로 이동2
        Button_Main_OurProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CoupleprofileActivity.class);
                startActivity(intent);
            }
        });


    } //OnCreate

    /*리줌일때 */
    @Override
    protected void onResume() {
        super.onResume();
        getcoupledate(); //사귄 날 가져오기
        getprofile(); //프로필 사진들 가져오기
        getuserinfo(); // 이름 가져오기
        allStory(); // 메인 리사이클러뷰 1 가져오기 - 전체보기
        normalStory(); // 메인 리사이클러뷰 2 가져오기 - 일반글보기
        secretStory(); // 메인 리사이클러뷰 3 가져오기 - 익명글보기
        followStory();; //메인 리사이클러뷰 3 가져오기 - 팔로우글보기
    } //onResume



    /*탭 메뉴*/
    private void changeView(int index) {
        LinearLayout View1 = (LinearLayout) findViewById(R.id.View1);
        LinearLayout View2 = (LinearLayout) findViewById(R.id.View2);
        LinearLayout View3 = (LinearLayout) findViewById(R.id.View3);
        LinearLayout View4 = (LinearLayout) findViewById(R.id.View4);
        switch (index) {
            case 0:
                View1.setVisibility(View.VISIBLE);
                View2.setVisibility(View.INVISIBLE);
                View3.setVisibility(View.INVISIBLE);
                View4.setVisibility(View.INVISIBLE);
                break;
            case 1:
                View1.setVisibility(View.INVISIBLE);
                View2.setVisibility(View.VISIBLE);
                View3.setVisibility(View.INVISIBLE);
                View4.setVisibility(View.INVISIBLE);
                break;
            case 2:
                View1.setVisibility(View.INVISIBLE);
                View2.setVisibility(View.INVISIBLE);
                View3.setVisibility(View.VISIBLE);
                View4.setVisibility(View.INVISIBLE);
                break;
            case 3:
                View1.setVisibility(View.INVISIBLE);
                View2.setVisibility(View.INVISIBLE);
                View3.setVisibility(View.INVISIBLE);
                View4.setVisibility(View.VISIBLE);
                break;

        }
    }//changeView


    /*이메일값을 서버로 보내고 그걸 이용해 그 사람의 데이터 가져오기*/
    public void getuserinfo(){

        SharedPreferences sharedPreferences = getSharedPreferences("autologin",MODE_PRIVATE);
        getEmail = sharedPreferences.getString("auto_login","no_autologin_key");
        Log.d("메인shared", "getuserinfo: "+getEmail); // 확인
        applicationClass.retroClient.getUserData_main(getEmail, new RetroCallback() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                UserData data = (UserData) receivedData;
                Log.d("데이터 불러오기", "onSuccess: "+data.getName());

                if(data.getOther()==null){
                    //signup2에서 바로 메인으로 넘어가서 상대방이 없는 경우
                    Textview_Main_Myname.setText(data.getName());
                    Textview_Main_Anothername.setText("상대방을\n등록해주세요!");
                }else{
                    //커플일 경우에 나와 상대방 이름 등록
                    Textview_Main_Myname.setText(data.getName());
                    Textview_Main_Anothername.setText(data.getOther());
                }


            }

            @Override
            public void onFailure(int code) {

            }
        });

    }//getuserinfo()


    /*나와 상대방 이미지 불러오기*/
    public void getprofile(){
        /*나와 상대방 이미지 불러오기*/
        /*applicationClass에서 커플키값, 이메일값 쉐어드로 불러오기*/
        String myEmail = applicationClass.autoLoginKey;
        String coupleKey = applicationClass.sharedcouplekey;
//
//        SharedPreferences sharedPreferences1 = getSharedPreferences("autologin",MODE_PRIVATE);
//        String myEmail = sharedPreferences1.getString("auto_login","no_autologin_key");
//        Log.d("애플리케이션클래스에서 가져온", "CoupleprofileActivity: autoLoginKey::"+myEmail);
//
//        SharedPreferences sharedPreferences = getSharedPreferences("CoupleKey",MODE_PRIVATE);
//        String coupleKey = sharedPreferences.getString(myEmail,"no_key_login");
//        Log.d("애플리케이션클래스에서 가져온", "CoupleprofileActivity: coupleKey::"+coupleKey);

        applicationClass.retroClient.getprofiles(myEmail, coupleKey, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                //나와 상대 프로필 이미지 불러오기
                Log.d(TAG, "onSuccess: "+code+receivedData);
                ThreeStringData data = (ThreeStringData)receivedData;
                String myProfile = data.getFirst();
                String otherProfile = data.getSecond();

                Log.d(TAG, "onSuccess: 내 프로필 사진"+myProfile);
                Log.d(TAG, "onSuccess: 상대 프로필 사진"+otherProfile);

                //내 프로필 보여주기
                if (myProfile!=null){
                    Log.d(TAG, "onSuccess: 내프로필1 "+applicationClass.serverImageRoot+myProfile);
                    Glide.with(getApplicationContext()).load(applicationClass.serverImageRoot+myProfile).into(Imageview_Main_Myprofile); //글라이드 오류
                }else if (myProfile.equals("default_profile.png")){
                    Glide.with(getApplicationContext()).load(applicationClass.defaultProfile).into(Imageview_Main_Myprofile);
                    Log.d(TAG, "onSuccess: 내프로필2 "+applicationClass.serverImageRoot+myProfile);
                }else{
                    Glide.with(getApplicationContext()).load(applicationClass.defaultProfile).into(Imageview_Main_Myprofile);
                    Log.d(TAG, "onSuccess: 내프로필3 "+applicationClass.serverImageRoot+myProfile);
                }


                //상대 프로필 보여주기
                if (otherProfile!=null){
                    Glide.with(getApplicationContext()).load(applicationClass.serverImageRoot+otherProfile).into(Imageview_Main_Anotherprofile);
                }else if (otherProfile.equals("default_profile.png")){
                    Glide.with(getApplicationContext()).load(applicationClass.defaultProfile).into(Imageview_Main_Myprofile);
                }else{
                    Glide.with(getApplicationContext()).load(applicationClass.defaultProfile).into(Imageview_Main_Anotherprofile);
                }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });
    } //getprofile()


    /*사귄날 불러오기*/
    public void getcoupledate(){
        /*applicationClass에서 커플키값, 이메일값 쉐어드로 불러오기*/
//        String coupleKey1 = applicationClass.sharedcouplekey; //커플키값
//        Log.d("애플리케이션클래스에서 가져온", "CoupleprofileActivity: sharedcouplekey ::"+coupleKey1);
//        SharedPreferences sharedPreferences1 = getSharedPreferences("autologin",MODE_PRIVATE);
//        String myEmail = sharedPreferences1.getString("auto_login","no_autologin_key");
//        Log.d("애플리케이션클래스에서 가져온", "CoupleprofileActivity: autoLoginKey::"+myEmail);
//
//        SharedPreferences sharedPreferences = getSharedPreferences("CoupleKey",MODE_PRIVATE);
//        String coupleKey = sharedPreferences.getString(myEmail,"no_key_login");
//        Log.d("애플리케이션클래스에서 가져온", "CoupleprofileActivity: coupleKey::"+coupleKey);

        /*사귄날 불러오기*/
        applicationClass.retroClient.getcoupledate(coupleKey, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d("받는거실행?", "onSuccess: "+code);
                Result_login data = (Result_login)receivedData;
//                String serverResult = data.getServerResult();
                String couplekeyResult = data.getCouplekey();
                Log.d(TAG, "onSuccess: "+couplekeyResult+"/"+code);

                //oncreate에서 사귄날 메인에 보여주기
                Log.d(TAG, "couplekeyResult응?: "+couplekeyResult);
                if(couplekeyResult==null){
                    Textview_Main_Date.setText("설정에서 사귄날을 등록해주세요!");
//                    String no_date = Textview_Main_Date.getText().toString();
//                    if(no_date.equals("우리 사귄날을 등록해주세요!")){
//                        Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
//                        startActivity(intent);
//                    }
                }else {
                    Textview_Main_Date.setText(couplekeyResult);
                }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });


    }




    /*메인 리사이클러뷰1 - 전체보기*/
    public void allStory(){

        /*View1 - 게시글 전체보기 탭 리사이클러뷰*/
        storyDataArrayList = new ArrayList<>();
        String couplekey = applicationClass.getShared_Couplekey();

        /*서버에서 데이터 리스트 받아와서 보여주기*/
        applicationClass.retroClient.mainStory_All("normal",couplekey,new RetroCallback() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess:메인 리사이클러뷰 불러오기 "+code);
                List<StoryData> storyData = (List<StoryData>)receivedData;

                Log.d(TAG, "onSuccess: 리사이클러뷰 데이터"+storyData);
                for (int i = 0; i<((List<StoryData>) receivedData).size(); i++){

                    //댓글 리사이클러뷰에 보여줄 데이터 > 리스트에 추가
                    storyDataArrayList.add(storyData.get(i));
                    Log.d(TAG, "onCreate: 리사이클러뷰리스트"+storyDataArrayList);
                }

                //리사이클러뷰 연결
                recyclerView = findViewById(R.id.RCV_Main_View1);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(RealMainActivity.this);
                recyclerView.setLayoutManager(layoutManager);

//                StoryAdapter.StateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW;
                storyAdapter = new StoryAdapter(storyDataArrayList,RealMainActivity.this); // 스토리어댑터
                storyAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
                storyAdapter.notifyDataSetChanged();

                recyclerView.setAdapter(storyAdapter); // 리사이클러뷰에 어댑터 연결
                Log.d(TAG, "onCreate: 리사이클러뷰리스트"+storyDataArrayList);
            }

            @Override
            public void onFailure(int code) {

            }
        });

    }//allStory()


    public void normalStory(){
        /*View2 - 일반글만 보기 탭 리사이클러뷰2*/
        storyDataArrayList2 = new ArrayList<>();
        String couplekey = applicationClass.getShared_Couplekey();

        /*서버에서 데이터 리스트 받아와서 보여주기*/
        applicationClass.retroClient.mainStory_normal("normal",couplekey,new RetroCallback() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess:메인 리사이클러뷰 불러오기 "+code);
                List<StoryData> storyData = (List<StoryData>)receivedData;

                Log.d(TAG, "onSuccess: 리사이클러뷰 데이터"+storyData);
                for (int i = 0; i<((List<StoryData>) receivedData).size(); i++){

                    //댓글 리사이클러뷰에 보여줄 데이터 > 리스트에 추가
                    storyDataArrayList2.add(storyData.get(i));
                    Log.d(TAG, "onCreate: 리사이클러뷰리스트"+storyDataArrayList2);
                }

                //리사이클러뷰 연결
                recyclerView2 = findViewById(R.id.RCV_Main_View2);
                recyclerView2.setHasFixedSize(true);
                layoutManager2 = new LinearLayoutManager(RealMainActivity.this);
                recyclerView2.setLayoutManager(layoutManager2);

                storyAdapter2 = new StoryAdapter(storyDataArrayList2,RealMainActivity.this); // 스토리어댑터
                storyAdapter2.notifyDataSetChanged();
//                storyAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.ALLOW);

                recyclerView2.setAdapter(storyAdapter2); // 리사이클러뷰에 어댑터 연결
                Log.d(TAG, "onCreate: 리사이클러뷰리스트"+storyDataArrayList2);
            }

            @Override
            public void onFailure(int code) {

            }
        });

    }//mainStory_normal()



    public void secretStory(){
        /*View2 - 일반글만 보기 탭 리사이클러뷰2*/
        storyDataArrayList3 = new ArrayList<>();
        String couplekey = applicationClass.getShared_Couplekey();

        /*서버에서 데이터 리스트 받아와서 보여주기*/
        applicationClass.retroClient.mainStory_secret("secret",couplekey,new RetroCallback() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess:메인 리사이클러뷰 불러오기 "+code);
                List<StoryData> storyData = (List<StoryData>)receivedData;

                Log.d(TAG, "onSuccess: 리사이클러뷰 데이터"+storyData);
                for (int i = 0; i<((List<StoryData>) receivedData).size(); i++){

                    //댓글 리사이클러뷰에 보여줄 데이터 > 리스트에 추가
                    storyDataArrayList3.add(storyData.get(i));
                    Log.d(TAG, "onCreate: 리사이클러뷰리스트"+storyDataArrayList3);
                }

                //리사이클러뷰 연결
                recyclerView3 = findViewById(R.id.RCV_Main_View3);
                recyclerView3.setHasFixedSize(true);
                layoutManager3 = new LinearLayoutManager(RealMainActivity.this);
                recyclerView3.setLayoutManager(layoutManager3);

                storyAdapter3 = new StoryAdapter(storyDataArrayList3,RealMainActivity.this); // 스토리어댑터
                storyAdapter3.notifyDataSetChanged();
//                storyAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.ALLOW);

                recyclerView3.setAdapter(storyAdapter3); // 리사이클러뷰에 어댑터 연결
                Log.d(TAG, "onCreate: 리사이클러뷰리스트"+storyDataArrayList3);
            }

            @Override
            public void onFailure(int code) {

            }
        });

    }//mainStory_secret()


    /*팔로우꺼 모아보기*/
    public void followStory(){
        /*View2 - 일반글만 보기 탭 리사이클러뷰2*/
        storyDataArrayList4 = new ArrayList<>();
        String couplekey = applicationClass.getShared_Couplekey();

        /*서버에서 데이터 리스트 받아와서 보여주기*/
        applicationClass.retroClient.mainStory_follow("normal",couplekey,new RetroCallback() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess:메인 리사이클러뷰 불러오기 "+code);
                List<StoryData> storyData = (List<StoryData>)receivedData;

                Log.d(TAG, "onSuccess: 리사이클러뷰 데이터"+storyData);
                for (int i = 0; i<((List<StoryData>) receivedData).size(); i++){

                    //댓글 리사이클러뷰에 보여줄 데이터 > 리스트에 추가
                    storyDataArrayList4.add(storyData.get(i));
                    Log.d(TAG, "onCreate: 리사이클러뷰리스트"+storyDataArrayList4);
                }

                //리사이클러뷰 연결
                recyclerView4 = findViewById(R.id.RCV_Main_View4);
                recyclerView4.setHasFixedSize(true);
                layoutManager4 = new LinearLayoutManager(RealMainActivity.this);
                recyclerView4.setLayoutManager(layoutManager4);

                storyAdapter4 = new StoryAdapter(storyDataArrayList4,RealMainActivity.this); // 스토리어댑터
                storyAdapter4.notifyDataSetChanged();
                storyAdapter4.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.ALLOW);

                recyclerView4.setAdapter(storyAdapter4); // 리사이클러뷰에 어댑터 연결
                Log.d(TAG, "onCreate: 리사이클러뷰리스트"+storyDataArrayList4);
            }

            @Override
            public void onFailure(int code) {

            }
        });

    }//followStory()


}//END