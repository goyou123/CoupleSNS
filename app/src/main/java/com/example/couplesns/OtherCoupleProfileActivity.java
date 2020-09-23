package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.couplesns.Adapter.StoryAdapter;
import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.DataClass.StoryData;
import com.example.couplesns.DataClass.ThreeStringData;
import com.example.couplesns.RetrofitJava.RetroCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OtherCoupleProfileActivity extends AppCompatActivity {
    ApplicationClass applicationClass;
    final static String TAG = "상대커플 프로필 액티비티";
    ImageView Imageview_Otherprofile_back,Imageview_Otherprofile_Myprofile,Imageview_Otherprofile_Anotherprofile;
    TextView Textview_Otherprofile_Date,Textview_Otherprofile_Myname,Textview_Otherprofile_Anothername;
    Button Button_Otherprofile_Follow,Button_Otherprofile_FollowCancel,Button_Otherprofile_Gallery,Button_Otherprofile_Doubledate;

    String couplekey,MyEmail;
    String intentName,intentCouplekey,intentProfile1,intentProfile2;

    //리사이클러뷰
    ArrayList<StoryData> storyDataArrayList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    StoryAdapter storyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_couple_profile);

        applicationClass = (ApplicationClass) getApplicationContext();

        //로그인한 유저의 이메일과 커플키
        couplekey = applicationClass.getShared_Couplekey();
        MyEmail = applicationClass.getShared_Email();

        //xml
        Imageview_Otherprofile_back = (ImageView) findViewById(R.id.Imageview_Otherprofile_back); //뒤로가기
        Imageview_Otherprofile_Myprofile = (ImageView) findViewById(R.id.Imageview_Otherprofile_Myprofile); // 남자 프로필
        Imageview_Otherprofile_Anotherprofile = (ImageView) findViewById(R.id.Imageview_Otherprofile_Anotherprofile); // 여자 프로필
        Textview_Otherprofile_Date = (TextView) findViewById(R.id.Textview_Otherprofile_Date); //사귄날
        Textview_Otherprofile_Myname = (TextView) findViewById(R.id.Textview_Otherprofile_Myname); // 남자 이름
        Textview_Otherprofile_Anothername = (TextView) findViewById(R.id.Textview_Otherprofile_Anothername); //여자 이름
        Button_Otherprofile_Follow = (Button) findViewById(R.id.Button_Otherprofile_Follow); // 팔로우 버튼
        Button_Otherprofile_FollowCancel= (Button) findViewById(R.id.Button_Otherprofile_FollowCancel); // 팔로우 취소버튼
        Button_Otherprofile_Gallery = (Button) findViewById(R.id.Button_Otherprofile_Gallery); // 갤러리 버튼
        Button_Otherprofile_Doubledate = (Button) findViewById(R.id.Button_Otherprofile_Doubledate); //더블데이트 신청 버튼

        //스토리 어댑터에서 넘어온 상대 프로필 데이터들
        Intent intent = getIntent();
        intentCouplekey = intent.getStringExtra("writerCoupleKey");
        intentName = intent.getStringExtra("writerCoupleName");
        intentProfile1 = intent.getStringExtra("writerProfile1");
        intentProfile2 = intent.getStringExtra("writerProfile2");

        //은찬 ♥ 민선 이름 잘라서 붙히기
        String splits[];
        splits = intentName.split("♥");
        Log.d(TAG, "onCreate: splits : "+splits.toString());
        Log.d(TAG, "onCreate: intentCouplekey"+intentCouplekey);
        Log.d(TAG, "onCreate: intentName"+intentName);
        Log.d(TAG, "onCreate: intentProfile1"+intentProfile1);
        Log.d(TAG, "onCreate: intentProfile2"+intentProfile2);

        /*화면에 상대커플 정보들 배치*/
        Glide.with(OtherCoupleProfileActivity.this).load(applicationClass.serverImageRoot+intentProfile1).into(Imageview_Otherprofile_Myprofile);
        Glide.with(OtherCoupleProfileActivity.this).load(applicationClass.serverImageRoot+intentProfile2).into(Imageview_Otherprofile_Anotherprofile);
        Textview_Otherprofile_Myname.setText(splits[0]); // 자른 상대방 이름
        Textview_Otherprofile_Anothername.setText(splits[1]); // 자른 상대방 이름

        //상대커플의 사귄 일 수 받아오기
        applicationClass.retroClient.getcoupledate(intentCouplekey, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Result_login data = (Result_login)receivedData;
                String couplekeyResult = data.getCouplekey();
                Log.d(TAG, "onSuccess: "+couplekeyResult+"/"+code);

                //oncreate에서 사귄날 메인에 보여주기
                Log.d(TAG, "couplekeyResult응?: "+couplekeyResult);
                if(couplekeyResult==null){
                    Textview_Otherprofile_Date.setText("사귄날이 등록되지 \n 않은 커플입니다!");

                }else {
                    Textview_Otherprofile_Date.setText(couplekeyResult);
                }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });



        //팔로우 버튼 클릭 시
        Button_Otherprofile_Follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_follow();

            }
        });

        //팔로우 취소 버튼 클릭 시
        Button_Otherprofile_FollowCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(applicationClass, "ㅇㅇㅇㅇㅇ", Toast.LENGTH_SHORT).show();
                remove_follow();
            }
        });


    }//OnCreate

    @Override
    protected void onResume() {
        super.onResume();
        otherStory(); //상대 커플이 쓴 게시글만 불러오기
        checkfollow(); //팔로우 값 체크
    }



    /*상대방 커플이 쓴 게시글 모아보기*/
    public void otherStory(){

        storyDataArrayList = new ArrayList<>();

        /*서버에서 데이터 리스트 받아와서 리사이클러뷰에 보여주기*/
        applicationClass.retroClient.getprofilestory("normal",intentCouplekey,new RetroCallback() {
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
                recyclerView = findViewById(R.id.RCV_OtherProfile);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(OtherCoupleProfileActivity.this);
                recyclerView.setLayoutManager(layoutManager);

                storyAdapter = new StoryAdapter(storyDataArrayList,OtherCoupleProfileActivity.this); // 스토리어댑터
                storyAdapter.notifyDataSetChanged();
//                storyAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.ALLOW);

                recyclerView.setAdapter(storyAdapter); // 리사이클러뷰에 어댑터 연결
                Log.d(TAG, "onCreate: 리사이클러뷰리스트"+storyDataArrayList);
            }

            @Override
            public void onFailure(int code) {

            }
        });

    }//otherStory()


    /*해당 커플 팔로우 하기*/
    public void add_follow(){
        //현재시간
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String getTime = simpleDate.format(mDate);


        HashMap<String, Object> addFollow = new HashMap<>();
        addFollow.put("our_couple",couplekey);
        addFollow.put("target_couple",intentCouplekey);
        addFollow.put("date",getTime);

        //팔로우 테이블에 데이터 추가하기
        applicationClass.retroClient.follow_add(addFollow, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code);
                Result_login data = (Result_login)receivedData;
                String serverResult = data.getServerResult();
                if(serverResult.equals("true")){
                    Toast.makeText(applicationClass, "팔로우 하였습니다", Toast.LENGTH_SHORT).show();
                    Button_Otherprofile_Follow.setVisibility(View.GONE);
                    Button_Otherprofile_FollowCancel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });

    }



    /*해당커플 팔로우 취소*/
    public void remove_follow(){
        //현재시간
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String getTime = simpleDate.format(mDate);


        HashMap<String, Object> addFollow = new HashMap<>();
        addFollow.put("our_couple",couplekey);
        addFollow.put("target_couple",intentCouplekey);
        addFollow.put("date",getTime);

        //팔로우 테이블에서 팔로우 취소하기
        applicationClass.retroClient.follow_remove(addFollow, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code);
                Result_login data = (Result_login)receivedData;
                String serverResult = data.getServerResult();
                if(serverResult.equals("true")){
                    Toast.makeText(applicationClass, "팔로우를 취소 하였습니다", Toast.LENGTH_SHORT).show();
                    Button_Otherprofile_FollowCancel.setVisibility(View.GONE);
                    Button_Otherprofile_Follow.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });


    }



    /*상대커플프로필 들어올 때 팔로우가 있는지 없는지 검사해서 팔로우 / 팔로우 취소 버튼 띄우게*/
    public void checkfollow(){

        HashMap<String, Object> checkFollow = new HashMap<>();
        checkFollow.put("our_couple",couplekey);
        checkFollow.put("target_couple",intentCouplekey);
        applicationClass.retroClient.follow_check(checkFollow, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code);
                ThreeStringData data = (ThreeStringData)receivedData;
                String serverResult = data.getFirst();
                if (serverResult.equals("true")){
//                    Toast.makeText(applicationClass, "서버에서 트루"+serverResult, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onSuccess: 서버 결과: "+serverResult);
                    Button_Otherprofile_Follow.setVisibility(View.GONE);
                    Button_Otherprofile_FollowCancel.setVisibility(View.VISIBLE);

                }else if(serverResult.equals("false")){
//                    Toast.makeText(applicationClass, "서버에서 풜스"+serverResult, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onSuccess: 서버 결과2: "+serverResult);
                    Button_Otherprofile_FollowCancel.setVisibility(View.GONE);
                    Button_Otherprofile_Follow.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });
    }

}//END