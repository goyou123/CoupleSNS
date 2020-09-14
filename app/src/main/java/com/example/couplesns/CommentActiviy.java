package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.couplesns.Adapter.CommentAdapter;
import com.example.couplesns.DataClass.CommentData;
import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.DataClass.StoryData;
import com.example.couplesns.DataClass.UserData;
import com.example.couplesns.RetrofitJava.RetroCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CommentActiviy extends AppCompatActivity {

    ApplicationClass applicationClass;

    ImageView Imageview_Comment_Back,Imageview_Comment_Coupleimg1,Imageview_Comment_Coupleimg2,Imageview_Comment_WriterProfile;
    TextView Textview_Comment_Storywriter,Textview_Comment_Storycontent,Textview_Comment_Storydate;
    EditText Edittext_Comment_Content;
    Button Button_Comment_Addcomment,Button_Comment_Editcomment;

    final static String TAG = "댓글액티비티";

    String myEmail;
    String myName;
    String myImg;
    String intentIdx,intentCouplekey,intentEmail;

    //리사이클러뷰
    ArrayList<CommentData> commentDataArrayList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_activiy);

        //applicationClass
        applicationClass = (ApplicationClass) getApplicationContext();
        myEmail = applicationClass.getShared_Email();

        //xml연결
//        Imageview_Comment_Back = (ImageView)findViewById(R.id.Imageview_Comment_Back); //뒤로가기
        Imageview_Comment_Coupleimg1 = (ImageView)findViewById(R.id.Imageview_Comment_Coupleimg1); // 내이미지
        Imageview_Comment_Coupleimg2 = (ImageView)findViewById(R.id.Imageview_Comment_Coupleimg2); // 상대 이미지
        Imageview_Comment_WriterProfile = (ImageView)findViewById(R.id.Imageview_Comment_WriterProfile); // 글쓰는 사람의 프로필사진
        Textview_Comment_Storywriter = (TextView)findViewById(R.id.Textview_Comment_Storywriter); // 게시글 작성자(커플이름)
        Textview_Comment_Storycontent = (TextView)findViewById(R.id.Textview_Comment_Storycontent); //게시글 내용
        Textview_Comment_Storydate = (TextView)findViewById(R.id.Textview_Comment_Storydate); //게시글 날짜
        Edittext_Comment_Content = (EditText)findViewById(R.id.Edittext_Comment_Content); // 댓글 쓰는 입력
        Button_Comment_Addcomment = (Button)findViewById(R.id.Button_Comment_Addcomment); // 게시 버튼
        Button_Comment_Editcomment = (Button)findViewById(R.id.Button_Comment_Editcomment); // 수정 버튼


        //리사이클러뷰 어댑터에서 넘어온 게시글 데이터
        Intent intent = getIntent();
        intentIdx = intent.getStringExtra("getidx");
        intentCouplekey = intent.getStringExtra("getcCouplekey");
        intentEmail = intent.getStringExtra("sharedEmail");
        String intentMyimg = intent.getStringExtra("getMyimg");
        String intentOtherimg = intent.getStringExtra("getOtherimg");
        String intentCouplename = intent.getStringExtra("getCouplename");
        String intentContent = intent.getStringExtra("getContent");
        String intentDate = intent.getStringExtra("getDate");

        //댓글 작성할 사람의 이름과 프로필 사진 가져오기
        getNameProfile();

        //넘어온 게시글 데이터 로그로 확인
        Log.d(TAG, "intentIdx: "+intentIdx);
        Log.d(TAG, "intentCouplekey: "+intentCouplekey);
        Log.d(TAG, "intentEmail: "+intentEmail);
        Log.d(TAG, "intentMyimg: "+intentMyimg);
        Log.d(TAG, "intentOtherimg: "+intentOtherimg);
        Log.d(TAG, "intentCouplename: "+intentCouplename);
        Log.d(TAG, "intentContent: "+intentContent);
        Log.d(TAG, "intentDate: "+intentDate);


        //상단에 위 게시글 내용 배치
        Glide.with(getApplicationContext()).load(applicationClass.serverImageRoot+intentMyimg).into(Imageview_Comment_Coupleimg1);
        Glide.with(getApplicationContext()).load(applicationClass.serverImageRoot+intentOtherimg).into(Imageview_Comment_Coupleimg2);
        Textview_Comment_Storywriter.setText(intentCouplename);
        Textview_Comment_Storydate.setText(intentDate);
        Textview_Comment_Storycontent.setText(intentContent);


        //댓글 달기 버튼 눌렀을때
        Button_Comment_Addcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentUpload();
            }
        });

    }//OnCreate

    @Override
    protected void onResume() {
        super.onResume();

        getCommentRecyclerView(); //댓글 리사이클러뷰 불러오기
    }//onResume()



    /*로그인한 유저의 이름과 프로필 이미지 가져오기*/
    public void getNameProfile() {
        applicationClass.retroClient.getnameprofile(myEmail, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code);
                UserData data = (UserData)receivedData;
                myName = data.getName();
                myImg = data.getProfileimg();
                Glide.with(getApplicationContext()).load(applicationClass.serverImageRoot+myImg).into(Imageview_Comment_WriterProfile);
                Log.d(TAG, "onSuccess: 댓글 작성자 이름 "+myName);

            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });

    }//getNameProfile()


    /*댓글 작성 - DB와 리사이클러뷰에 추가하기*/
    public void commentUpload(){
        //작성한 댓글
        String memo = Edittext_Comment_Content.getText().toString();

        //현재시간
        long mNow = System.currentTimeMillis(); // - long타입을 반환
//        Date mDate = new Date(mNow);
//        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//        String getTime = simpleDate.format(mDate);

        String getTime = Long.toString(mNow);
        Log.d(TAG, "commentUpload: 스트링"+getTime);

        //댓글 작성 정보
        HashMap<String, Object> commentsdata = new HashMap<>();
        commentsdata.put("storyidx",intentIdx); //게시글 인덱스
        commentsdata.put("couplekey",intentCouplekey); // 커플키
        commentsdata.put("writeremail",myEmail); // 작성자 이메일
        commentsdata.put("writer",myName); // 작성자 이름
        commentsdata.put("writerimg",myImg); // 작성자 이미지
        commentsdata.put("memo",memo); // 글 내용
        commentsdata.put("commentdate",getTime); //댓글 저장 시간

        applicationClass.retroClient.commentsupload(commentsdata, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code);
                Result_login data = (Result_login)receivedData;
                String serverResult = data.getServerResult();

                //DB에 댓글이 저장되면 토스트와 함께 댓글 등록 + 댓글창 비우기
                if(serverResult.equals("true")){
                    Toast.makeText(applicationClass, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),CommentActiviy.class);

                    //호출되는 Activity가 최상위에 존재할 경우에는 해당 Activity를 다시 생성하지 않고, 존재하던 Activity를 다시 사용하게 됩니다.
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    Edittext_Comment_Content.setText(null);

                }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });





    }// commentUpload()


    /*댓글 리사이클러뷰 불러오기*/
    public void getCommentRecyclerView(){
        commentDataArrayList = new ArrayList<>();
        int storyidx = Integer.parseInt(intentIdx);
        applicationClass.retroClient.getcomment(storyidx, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code);
                List<CommentData> commentData = (List<CommentData>)receivedData;

                for (int i = 0; i<commentData.size(); i++){
                    commentDataArrayList.add(commentData.get(i));
                    Log.d(TAG, "onCreate: 리사이클러뷰리스트"+commentDataArrayList);
                }
                //댓글 데이터를 받아온 뒤 리스트에 넣고 리사이클러뷰에 연결
                recyclerView = findViewById(R.id.RCV_Comment); // xml , 리사이클러뷰 연결
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(CommentActiviy.this);
                recyclerView.setLayoutManager(layoutManager);

                commentAdapter = new CommentAdapter(commentDataArrayList,CommentActiviy.this);
                commentAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(commentAdapter);

            }


            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });



    }//getCommentRecyclerView()


}//END