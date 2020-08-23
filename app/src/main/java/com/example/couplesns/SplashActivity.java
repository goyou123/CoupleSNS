package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.couplesns.DataClass.UserData;
import com.example.couplesns.RetrofitJava.RetroCallback;
import com.example.couplesns.RetrofitJava.RetroClient;

public class SplashActivity extends AppCompatActivity {
//   String autoLoginKey;
   RetroClient retroClient;
   ApplicationClass applicationClass;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startLoading();

        //레트로핏 사용을 위해 레트로핏 인스턴스 생성
        retroClient = RetroClient.getInstance(this).createBaseApi();
        //applicationClass
        applicationClass = (ApplicationClass) getApplicationContext();

    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

//                /*자동로그인*/
//                if (shared_Email!=null) {
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(intent);
//                }else{
//                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
//                    startActivity(intent);
//                }
                //자동로그인
                //로그인때 저장한 쉐어드에 내 이메일 키가 있으면 ("auto_login",입력이메일) 바로 메인으로 감
                applicationClass.getShared_Email();

//                SharedPreferences sharedPreferences = getSharedPreferences("autologin",MODE_PRIVATE);
//               autoLoginKey = sharedPreferences.getString("auto_login","no_autologin_key"); //이메일
                Log.d("애플리케이션에서 가져온ㄴ", "applicationClass.autoLoginKey: "+applicationClass.autoLoginKey);
                if(applicationClass.autoLoginKey.contentEquals("no_autologin_key")) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }else{
                    retroClient.getUserData_main(applicationClass.autoLoginKey, new RetroCallback() {
                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onSuccess(int code, Object receivedData) {
                            UserData data = (UserData) receivedData;
                            Log.d("데이터 불러오기", "onSuccess: "+data.getName());
                            Toast.makeText(SplashActivity.this, data.getName()+"님 로그인 하였습니다.", Toast.LENGTH_SHORT).show();

                            //if autologinkey!=null && 커플키값이 있으면 메인
//                                                        없으면 signup2
                        }

                        @Override
                        public void onFailure(int code) {

                        }
                    });
                    Intent intent = new Intent(getApplicationContext(), RealMainActivity.class);
                    startActivity(intent);

                }

            }
        },3000);

    }

}//END