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

import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.RetrofitJava.RetroCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class WriteSecretStoryActivity extends AppCompatActivity {
    final static String TAG = "익명게시글 액티비티";
    ImageView Imageview_SecretWriteStory_back;
    Button Button_SecretWritestory_add;
    EditText Edittext_SecretWritestory_Content;

    ApplicationClass applicationClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_secret_story);


        //applicationClass
        applicationClass = (ApplicationClass) getApplicationContext();

        //xml
        Imageview_SecretWriteStory_back = (ImageView)findViewById(R.id.Imageview_SecretWriteStory_back);
        Button_SecretWritestory_add = (Button)findViewById(R.id.Button_SecretWritestory_add);
        Edittext_SecretWritestory_Content = (EditText)findViewById(R.id.Edittext_SecretWritestory_Content);
        
        
        //작성완료 버튼 클릭
        Button_SecretWritestory_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(applicationClass, "클릭", Toast.LENGTH_SHORT).show();
                secretstoryupload();
            }
        });
        

        //뒤로가기
        Imageview_SecretWriteStory_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        
        
        
        
    }//OnCreate



    public void secretstoryupload(){

        String secretContent = Edittext_SecretWritestory_Content.getText().toString();
        String couplekey = applicationClass.getShared_Couplekey();

        //현재시간
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String getTime = simpleDate.format(mDate);


        HashMap<String, Object> secretstory = new HashMap<>();
        secretstory.put("writer", "익명");
        secretstory.put("couplekey", couplekey);
        secretstory.put("myimg", "default_profile.png");
        secretstory.put("content",secretContent);
        secretstory.put("date",getTime);
        secretstory.put("form","secret");

        Log.d("실행체크", "여기까지 실행"+secretstory); // 실행됨

        applicationClass.retroClient.secretstoryupload(secretstory, new RetroCallback() {
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
                    Toast.makeText(applicationClass, "익명 게시글이 작성되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),RealMainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });

    }//secretstoryupload()


}//END