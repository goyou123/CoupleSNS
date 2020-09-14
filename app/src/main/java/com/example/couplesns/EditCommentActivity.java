package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.RetrofitJava.RetroCallback;

public class EditCommentActivity extends AppCompatActivity {
    final static String TAG = "댓글수정 액티비티";
    ImageView Imageview_EditComment_Coupleimg1;
    EditText Edittext_EditComment_Write;
    Button Button_EditComment_Cancel,Button_EditComment_Update;
    ApplicationClass applicationClass;

    String editIdx,editWriter,editWriterimg,editMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);
        applicationClass = (ApplicationClass) getApplicationContext();
        //xml
        Imageview_EditComment_Coupleimg1 = (ImageView)findViewById(R.id.Imageview_EditComment_Coupleimg1);
        Edittext_EditComment_Write = (EditText) findViewById(R.id.Edittext_EditComment_Write);
        Button_EditComment_Cancel = (Button) findViewById(R.id.Button_EditComment_Cancel);
        Button_EditComment_Update = (Button) findViewById(R.id.Button_EditComment_Update);


        //인텐트로 받기
        //인텐트로 받은 게시글 내용들
        Intent intent = getIntent();
        editIdx = intent.getStringExtra("getidx");
        editWriter = intent.getStringExtra("writer");
        editWriterimg = intent.getStringExtra("writerimg");
        editMemo = intent.getStringExtra("getmemo");
        Log.d(TAG, "onCreate: 댓글 idx "+editIdx);

        //댓글 내용 대입
        Edittext_EditComment_Write.setText(editMemo);
        Glide.with(EditCommentActivity.this).load(applicationClass.serverImageRoot+editWriterimg).into(Imageview_EditComment_Coupleimg1);



        //취소
        Button_EditComment_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //댓글 수정
        Button_EditComment_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCommment();
            }
        });


    }//OnCreate


    //서버에서 댓글 수정
    public void editCommment(){
        int idx = Integer.parseInt(editIdx); //댓글의 위치
        String memo= Edittext_EditComment_Write.getText().toString(); //변경하는 댓글
        long mNow = System.currentTimeMillis(); // - long타입을 반환
        String getTime = Long.toString(mNow);
        applicationClass.retroClient.commentedit(idx,memo,getTime, new RetroCallback() {
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
                    Toast.makeText(applicationClass, "댓글이 수정되었습니다", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });



    }

}//END