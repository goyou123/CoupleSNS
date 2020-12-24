package com.example.couplesns;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.telecom.Call;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.couplesns.Adapter.StoryAdapter;
import com.example.couplesns.DataClass.ImgData_ex;
import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.DataClass.StoryData;
import com.example.couplesns.DataClass.ThreeStringData;
import com.example.couplesns.DataClass.UserData;
import com.example.couplesns.RetrofitJava.RetroCallback;
import com.example.couplesns.RetrofitJava.RetroClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static okhttp3.MediaType.parse;

public class CoupleprofileActivity extends AppCompatActivity {
    final static String TAG = "커플프로필액티비티";
    final static int REQUEST_IMAGE_CODE = 1000;
    ImageView Imageview_Coupleprofile_back,Imageview_Coupleprofile_Setting,Imageview_Coupleprofile_Myprofile,
            Imageview_Coupleprofile_Anotherprofile,ImageView_Coupleprofile_Edit;
    TextView Textview_Coupleprofile_Date,Textview_Coupleprofile_Myname,Textview_Coupleprofile_Anothername;
    Button Button_Coupleprofile_Follow,Button_Coupleprofile_Star,Button_Coupleprofile_Gallery,Button_Coupleprofile_Doubledate;

    ApplicationClass applicationClass;
    String coupleKey,myEmail;

    //리사이클러뷰
    ArrayList<StoryData> storyDataArrayList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    StoryAdapter storyAdapter;

    int position;
    int imgs_lenght;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupleprofile);

        //applicationClass
        applicationClass = (ApplicationClass) getApplicationContext();

        //xml연결
        Imageview_Coupleprofile_back = (ImageView) findViewById(R.id.Imageview_Coupleprofile_back); // 음성
//        Imageview_Coupleprofile_Setting = (ImageView) findViewById(R.id.Imageview_Coupleprofile_Setting); // 세팅
        Imageview_Coupleprofile_Myprofile = (ImageView) findViewById(R.id.Imageview_Coupleprofile_Myprofile); // 내 이미지
        Imageview_Coupleprofile_Anotherprofile = (ImageView) findViewById(R.id.Imageview_Coupleprofile_Anotherprofile); // 상대이미지
        ImageView_Coupleprofile_Edit =(ImageView) findViewById(R.id.ImageView_Coupleprofile_Edit); // 프사 옆 프로필 수정버튼
        Textview_Coupleprofile_Date = (TextView) findViewById(R.id.Textview_Coupleprofile_Date); // 사귄날
        Textview_Coupleprofile_Myname = (TextView) findViewById(R.id.Textview_Coupleprofile_Myname); // 내 이름
        Textview_Coupleprofile_Anothername = (TextView) findViewById(R.id.Textview_Coupleprofile_Anothername); //상대이름
        Button_Coupleprofile_Follow = (Button) findViewById(R.id.Button_Coupleprofile_Follow); // 팔로우 버튼
//        Button_Coupleprofile_Star = (Button) findViewById(R.id.Button_Coupleprofile_Star); // 스크랩 버튼
        Button_Coupleprofile_Gallery = (Button) findViewById(R.id.Button_Coupleprofile_Gallery); // 갤러리 버튼
        Button_Coupleprofile_Doubledate = (Button) findViewById(R.id.Button_Coupleprofile_Doubledate); //받은 더블데이트 버튼



        SharedPreferences sharedPreferences1 = getSharedPreferences("autologin",MODE_PRIVATE);
        myEmail = sharedPreferences1.getString("auto_login","no_autologin_key");
        Log.d("애플리케이션클래스에서 가져온", "CoupleprofileActivity: autoLoginKey::"+myEmail);

        SharedPreferences sharedPreferences = getSharedPreferences("CoupleKey",MODE_PRIVATE);
        coupleKey = sharedPreferences.getString(myEmail,"no_key_login");
        Log.d("애플리케이션클래스에서 가져온", "CoupleprofileActivity: coupleKey::"+coupleKey);

        //내 이름과 상대이름 불러오기
        getuserinfo();




//        //데이트피커
//        /*사귄날 설정 - Datepicker*/


        /*프로필 수정 액티비티로 이동*/
        ImageView_Coupleprofile_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),EditProfileActivity.class);
                startActivity(intent);
                //여기에 인텐트로 값을 넘긴다면?
            }
        });

        /*뒤로가기*/
        Imageview_Coupleprofile_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*팔로우 액티비티로 이동*/
        Button_Coupleprofile_Follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),FollowActivity.class);
                startActivity(intent);
            }
        });

        /*갤러리 액티비티로 이동*/
        Button_Coupleprofile_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),GalleryActivity.class);
                startActivity(intent);
            }
        });


    }//OnCreate



    /*이메일값을 서버로 보내고 그걸 이용해 그 사람의 데이터 가져오기*/
    public void getuserinfo(){
        applicationClass.retroClient.getUserData_main(myEmail, new RetroCallback() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                UserData data = (UserData) receivedData;
                Log.d("데이터 불러오기", "onSuccess: "+data.getName());

                if(data.getOther()==null){
                    //signup2에서 바로 메인으로 넘어가서 상대방이 없는 경우
                    Textview_Coupleprofile_Myname.setText(data.getName());
                    Textview_Coupleprofile_Anothername.setText("상대방을 등록해주세요!");
                }else{
                    //커플일 경우에 나와 상대방 이름 등록
                    Textview_Coupleprofile_Myname.setText(data.getName());
                    Textview_Coupleprofile_Anothername.setText(data.getOther());
                }


            }

            @Override
            public void onFailure(int code) {

            }
        });

    }//getuserinfo()


    /*리줌일때 사귄날 불러옴*/
    @Override
    protected void onResume() {
        super.onResume();

        //나와 상대방 이름 불러오기
        getuserinfo();

        //우리가 쓴 게시글 리사이클러뷰 불러오기
        ourStory();


        /*applicationClass에서 커플키값, 이메일값 쉐어드로 불러오기*/
//        String coupleKey1 = applicationClass.sharedcouplekey; //커플키값
//        Log.d("애플리케이션클래스에서 가져온", "CoupleprofileActivity: sharedcouplekey ::"+coupleKey1);
        /*먼저 이메일값을 받아온 뒤 그값으로 커플키값을 찾는다.*/
        //이메일값 먼저 가져오기
        SharedPreferences sharedPreferences1 = getSharedPreferences("autologin",MODE_PRIVATE);
        String autoLoginKey = sharedPreferences1.getString("auto_login","no_autologin_key");
        Log.d(TAG, "onDateSet: 오토로그인키()이메일"+autoLoginKey);

        //이메일값으로 그 이메일에 해당하는 커플키 가져오기 - shared저장 signup2
        SharedPreferences sharedPreferences = getSharedPreferences("CoupleKey",MODE_PRIVATE);
        String sharedkey = sharedPreferences.getString(autoLoginKey,"no_key_login");
        Log.d(TAG, "onDateSet:이메일값에 해당하는 커플키"+sharedkey);

        /*사귄날 불러오기*/
        applicationClass.retroClient.getcoupledate(sharedkey, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Result_login data = (Result_login)receivedData;
//                String serverResult = data.getServerResult();
                String couplekeyResult = data.getCouplekey();
                Log.d(TAG, "onSuccess: "+couplekeyResult+"/"+code);

                //oncreate에서 사귄날 메인에 보여주기
                if(couplekeyResult==null){
                    Textview_Coupleprofile_Date.setText("설정에서 사귄날을\n등록해주세요\n→");
                }else {
                    Textview_Coupleprofile_Date.setText(couplekeyResult);
                }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });

        Log.d(TAG, "onResume: "+myEmail+"///"+coupleKey);





        /*나와 상대방 이미지 불러오기*/
        applicationClass.retroClient.getprofiles(myEmail, coupleKey, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code+receivedData);
                ThreeStringData data = (ThreeStringData)receivedData;
                String myProfile = data.getFirst();
                String otherProfile = data.getSecond();
                Log.d(TAG, "onSuccess: 내 프로필 사진"+myProfile);
                Log.d(TAG, "onSuccess: 상대 프로필 사진"+otherProfile);
                if (myProfile!=null){
                    Glide.with(getApplicationContext()).load(applicationClass.serverImageRoot+myProfile).into(Imageview_Coupleprofile_Myprofile); //글라이드 오류
                }else{
                    Glide.with(getApplicationContext()).load(applicationClass.defaultProfile).into(Imageview_Coupleprofile_Myprofile);
                }

                if (otherProfile!=null){
                    Glide.with(getApplicationContext()).load("http://13.125.182.117/img/"+otherProfile).into(Imageview_Coupleprofile_Anotherprofile);
//                    Glide.with(getApplicationContext()).load("http://3.34.137.189/img/"+otherProfile).into(Imageview_Coupleprofile_Anotherprofile);
                }else{
                    Glide.with(getApplicationContext()).load(applicationClass.defaultProfile).into(Imageview_Coupleprofile_Anotherprofile);
                }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });




    } //onResume


    /*우리커플이 쓴 게시물 리사이클러뷰 불러오기*/
    public void ourStory(){

        /*갤러리 액티비티에서 넘어왔을 때*/
        Intent intent = getIntent();
        position = intent.getIntExtra("position",999);
        imgs_lenght =  intent.getIntExtra("size",999);
        Log.d(TAG, "갤러리사진들사이즈: "+imgs_lenght);

        storyDataArrayList = new ArrayList<>();

        /*서버에서 데이터 리스트 받아와서 보여주기*/
        applicationClass.retroClient.getprofilestory("normal",coupleKey,new RetroCallback() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess:메인 리사이클러뷰 불러오기 "+code);
                List<StoryData> storyData = (List<StoryData>)receivedData;
                Button_Coupleprofile_Gallery.setText(" 갤러리 "+storyData.size());
                Log.d(TAG, "onSuccess: 리사이클러뷰 데이터"+storyData);
                for (int i = 0; i<((List<StoryData>) receivedData).size(); i++){

                    //댓글 리사이클러뷰에 보여줄 데이터 > 리스트에 추가
                    storyDataArrayList.add(storyData.get(i));
                    Log.d(TAG, "onCreate: 리사이클러뷰리스트"+storyDataArrayList);
                }

                Log.d(TAG, "onSuccess: 포지션 : "+position);
                //리사이클러뷰 연결
                recyclerView = findViewById(R.id.RCV_MyProfile);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(CoupleprofileActivity.this);

                /*갤러리액티비티에서 넘어왔을때 해당 포지션의 리사이클러뷰로 바로 이동*/
                if(position!=999){
                    //해당 글 이 있는 리사이클러뷰의 위치로 이동
                    int pos = imgs_lenght-position-1;
                    Log.d(TAG, "갤러리사진들글위치: "+pos);
                    layoutManager.scrollToPosition(pos);
                    recyclerView.setLayoutManager(layoutManager);

                    storyAdapter = new StoryAdapter(storyDataArrayList,CoupleprofileActivity.this); // 스토리어댑터
                    storyAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(storyAdapter); // 리사이클러뷰에 어댑터 연결
                    Log.d(TAG, "onSuccess: 갤러리-실행");
                }
                
                
                recyclerView.setLayoutManager(layoutManager);

                storyAdapter = new StoryAdapter(storyDataArrayList,CoupleprofileActivity.this); // 스토리어댑터
                storyAdapter.notifyDataSetChanged();
//                storyAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.ALLOW);

                recyclerView.setAdapter(storyAdapter); // 리사이클러뷰에 어댑터 연결
                Log.d(TAG, "onCreate: 리사이클러뷰리스트"+storyDataArrayList);
            }

            @Override
            public void onFailure(int code) {

            }
        });



    }//ourStory()


}//END