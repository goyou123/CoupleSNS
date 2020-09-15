package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.couplesns.Adapter.CommentAdapter;
import com.example.couplesns.DataClass.CommentData;
import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.RetrofitJava.RetroCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SecretCommentActivity extends AppCompatActivity {
    final static String TAG = "익명 댓글 액티비티";
    ImageView Imageview_SecretComment_Coupleimg1,Imageview_Comment_WriterProfile;
    TextView Textview_SecretComment_Storycontent,Textview_SecretComment_Storydate;
    EditText Edittext_SecretComment_Content;
    Button Button_SecretComment_Addcomment;
    ApplicationClass applicationClass;

    String secretIdx,secretContent,secretDate;


    //리사이클러뷰
    ArrayList<CommentData> commentDataArrayList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret_comment);

        //applicationClass
        applicationClass = (ApplicationClass) getApplicationContext();

        //xml
        Imageview_SecretComment_Coupleimg1 = (ImageView)findViewById(R.id.Imageview_SecretComment_Coupleimg1);
        Imageview_Comment_WriterProfile = (ImageView)findViewById(R.id.Imageview_Comment_WriterProfile);
        Textview_SecretComment_Storycontent = (TextView)findViewById(R.id.Textview_SecretComment_Storycontent);
        Textview_SecretComment_Storydate = (TextView)findViewById(R.id.Textview_SecretComment_Storydate);
        Edittext_SecretComment_Content = (EditText)findViewById(R.id.Edittext_SecretComment_Content);
        Button_SecretComment_Addcomment =(Button)findViewById(R.id.Button_SecretComment_Addcomment);

        //인텐트로 익명 게시글 데이터 받기
        Intent intent = getIntent();
        secretIdx = intent.getStringExtra("secretIdx");
        secretContent = intent.getStringExtra("secretContent");
        secretDate = intent.getStringExtra("secretDate");

        //받은 게시글 데이터 보여주기
        Textview_SecretComment_Storycontent.setText(secretContent);
        Textview_SecretComment_Storydate.setText(secretDate);


        //댓글 작성버튼
        Button_SecretComment_Addcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secretCommentUpload();
            }
        });

    }//OnCreate


    @Override
    protected void onResume() {
        super.onResume();

        getSecretCommentRecyclerView(); //댓글 리사이클러뷰 불러오기
    }//onResume()


    //익명 댓글 업로드
    public void secretCommentUpload(){
        //댓글 내용
        String memo = Edittext_SecretComment_Content.getText().toString();

        //댓글 시간 - long
        long mNow = System.currentTimeMillis();
        String getTime = Long.toString(mNow);

        //댓글 작성자의 이메일
        String myEmail = applicationClass.getShared_Email();

        //댓글 작성하는 사람의 커플키
        String couplekey = applicationClass.getShared_Couplekey();

        //서버로 전송할 익명 댓글 데이터
        HashMap<String, Object> secretCommentsdata = new HashMap<>();
        secretCommentsdata.put("storyidx",secretIdx);
        secretCommentsdata.put("couplekey",couplekey);
        secretCommentsdata.put("writeremail",myEmail);
        secretCommentsdata.put("writer","익명");
        secretCommentsdata.put("memo",memo);
        secretCommentsdata.put("commentdate",getTime);

        //익명 댓글 데이터 서버로 저장
        applicationClass.retroClient.secretcommentsupload(secretCommentsdata, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code);
                //댓글 저장 성공
                Result_login data = (Result_login)receivedData;
                String serverResult = data.getServerResult();
                if(serverResult.equals("true")){
                    Toast.makeText(applicationClass, "댓글이 작성되었습니다.", Toast.LENGTH_SHORT).show();

                    //댓글을 실시간으로 적용하고 작성 내용 초기화
                    Intent intent = new Intent(getApplicationContext(),SecretCommentActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    Edittext_SecretComment_Content.setText(null);
                }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });




    }//secretCommentUpload()



    //댓글 리사이클러뷰 불러오기
    public void getSecretCommentRecyclerView(){
        commentDataArrayList = new ArrayList<>();
        int storyidx = Integer.parseInt(secretIdx);
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
                recyclerView = findViewById(R.id.RCV_SecretComment); // xml , 리사이클러뷰 연결
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(SecretCommentActivity.this);
                recyclerView.setLayoutManager(layoutManager);

                commentAdapter = new CommentAdapter(commentDataArrayList,SecretCommentActivity.this);
                commentAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(commentAdapter);

            }


            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });



    }//getSecretCommentRecyclerView()


}//END