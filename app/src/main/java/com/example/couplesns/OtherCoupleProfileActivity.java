package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.couplesns.Adapter.StoryAdapter;
import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.DataClass.StoryData;
import com.example.couplesns.RetrofitJava.RetroCallback;

import java.util.ArrayList;
import java.util.List;

public class OtherCoupleProfileActivity extends AppCompatActivity {
    ApplicationClass applicationClass;
    final static String TAG = "상대커플 프로필 액티비티";
    ImageView Imageview_Otherprofile_back,Imageview_Otherprofile_Myprofile,Imageview_Otherprofile_Anotherprofile;
    TextView Textview_Otherprofile_Date,Textview_Otherprofile_Myname,Textview_Otherprofile_Anothername;
    Button Button_Otherprofile_Follow,Button_Otherprofile_Gallery,Button_Otherprofile_Doubledate;

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



    }//OnCreate

    @Override
    protected void onResume() {
        super.onResume();
        otherStory(); //상대 커플이 쓴 게시글만 불러오기
    }

    public void otherStory(){
        /*View2 - 일반글만 보기 탭 리사이클러뷰2*/
        storyDataArrayList = new ArrayList<>();

        /*서버에서 데이터 리스트 받아와서 보여주기*/
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






}//END