package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
    ImageView Imageview_Setting_back;
    TextView TextView_Setting_Logout,TextView_Setting_Breack,TextView_Setting_Editprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Imageview_Setting_back = (ImageView) findViewById(R.id.Imageview_Setting_back);
        TextView_Setting_Logout = (TextView) findViewById(R.id.TextView_Setting_Logout);
        TextView_Setting_Breack = (TextView) findViewById(R.id.TextView_Setting_Breack);
        TextView_Setting_Editprofile = (TextView) findViewById(R.id.TextView_Setting_Editprofile);

        //로그아웃
        TextView_Setting_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //자동로그인을 데이터 삭제
                SharedPreferences sharedPreferences = getSharedPreferences("autologin",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("auto_login");
                editor.commit();

                //로그인페이지로 이동
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        //내 정보 변경
        TextView_Setting_Editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingActivity.this, "내 정보 변경 액티비티", Toast.LENGTH_SHORT).show();
            }
        });


        //뒤로가기
        Imageview_Setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }//OnCreate
}//END