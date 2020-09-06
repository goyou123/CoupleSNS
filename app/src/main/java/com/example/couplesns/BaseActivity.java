package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {
    ApplicationClass applicationClass;
    String className = getClass().getSimpleName().trim();
    public static final int SHORT_TOAST = 0;
    public static final int LONG_TOAST = 1;

    private static final String TAG = "로그";


    //로그 : 액티비티명 + 함수명 + 원하는 데이터를 한번에 보기위한 로그
    public void makeLog(String methodData, String strData) {
        Log.d(TAG, className + "_" + methodData + "_" + strData);

    }

    //토스트메세지 : 귀찮음을 없애기 위해 토스트를 띄우는 함수를 만듦
    public void makeToast(String str, int length) {
        if (length == SHORT_TOAST) {
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, str, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Log.d(TAG, className + "_onCreate()");

        applicationClass = (ApplicationClass) getApplicationContext();

    }

}