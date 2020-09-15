package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class WriteSecretStoryActivity extends AppCompatActivity {

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
}//END