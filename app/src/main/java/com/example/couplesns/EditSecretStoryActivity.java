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

public class EditSecretStoryActivity extends AppCompatActivity {
    final static String TAG = "익명글 수정 액티비티";

    ApplicationClass applicationClass;
    ImageView Imageview_EditSecretWriteStory_back;
    Button Button_EditSecretWritestory_add;
    EditText Edittext_EditSecretWritestory_Content;

    String editIdx,editWriterEmail,editContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_secret_story);

        applicationClass = (ApplicationClass) getApplicationContext();

        //xml
        Imageview_EditSecretWriteStory_back = (ImageView) findViewById(R.id.Imageview_EditSecretWriteStory_back);//뒤로가기
        Button_EditSecretWritestory_add = (Button) findViewById(R.id.Button_EditSecretWritestory_add); // 작성완료 버튼
        Edittext_EditSecretWritestory_Content = (EditText) findViewById(R.id.Edittext_EditSecretWritestory_Content); // 수정 글 작성

        //인텐트로 받은 게시글 내용들
        Intent intent = getIntent();
        editIdx = intent.getStringExtra("getidx");
        editWriterEmail = intent.getStringExtra("getWriterEmail");
        editContent = intent.getStringExtra("getContent");
        Log.d(TAG, "onCreate: 인텐트로 잘 받았나 "+editIdx+"/"+editWriterEmail+"/"+editContent);

        //글 내용 대입
        Edittext_EditSecretWritestory_Content.setText(editContent);

        //수정완료 버튼클릭
        Button_EditSecretWritestory_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSecretStoryUpload();
            }
        });




    }//OnCreate


    /*익명 게시글 수정*/
    public void editSecretStoryUpload (){

        //작성한 글
        String editSecretContent = Edittext_EditSecretWritestory_Content.getText().toString();

        //현재시간
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String getTime = simpleDate.format(mDate);

        HashMap<String, Object> editsecretstory = new HashMap<>();
        editsecretstory.put("editIdx",editIdx); //게시글 번호
        editsecretstory.put("content",editSecretContent);
        editsecretstory.put("date",getTime);

        //서버로 수정값 전송 후 응답 성공시 수정완료 토스트
        applicationClass.retroClient.edit_secretstoryupload(editsecretstory, new RetroCallback() {
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
                    Toast.makeText(applicationClass, "게시글이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),RealMainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });
    }//editSecretStoryUpload()


}//END