package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.DataClass.UserData;
import com.example.couplesns.RetrofitJava.RetroCallback;
import com.example.couplesns.RetrofitJava.RetroClient;

import java.util.HashMap;
import java.util.List;

public class Signup2Activity extends AppCompatActivity {
    TextView Textview_Signup2_Mycode;
    EditText Edittext_Signup2_Anothercode;
    Button Button_Signup2_Connect,Button_Signup2_Gomain;
    ImageView Imageview_Sighup2_Refresh;

    RetroClient retroClient;
    final static String TAG = "Signup2액티비티";

    String randomkey,anotherkey;
    String getmyemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        /* 후에 공유하기 추가
         * 1. 난수값을 가져와 내 코드에 붙힌다.
         * 2. 버튼 클릭하면 상대방 난수를 통해 상대방을 찾고 상대방의 커플키에 내 난수를 넣는다, + 내 커플키에도 내 난수를 넣는다.
         * 3.
         * */
        retroClient = RetroClient.getInstance(this).createBaseApi();

        Textview_Signup2_Mycode = (TextView) findViewById(R.id.Textview_Signup2_Mycode);
        Edittext_Signup2_Anothercode = (EditText) findViewById(R.id.Edittext_Signup2_Anothercode);
        Button_Signup2_Gomain = (Button) findViewById(R.id.Button_Signup2_Gomain);
        Button_Signup2_Connect = (Button) findViewById(R.id.Button_Signup2_Connect);
        Imageview_Sighup2_Refresh = (ImageView) findViewById(R.id.Imageview_Sighup2_Refresh);



        /*signup2로 오는 경로가 2개이다.
        * 1. 로그인에서 올때
        * 2. shared에서 올때
        * 2가지 경우에 난수값을 하나는 인텐트, 하나는 쉐어드로 등록*/
        //인텐트로 온 값 받기 - Shared로 하는게 나을려나?
        Intent intent = getIntent();
        getmyemail = intent.getStringExtra("inputEmail"); //로그인에서 온  이메일값

        //Signup1에서 온 랜덤키값
        if(getmyemail!=null){
            /*로그인 -> signup2로 와 이메일인텐트값이 있을때*/
            //Shared에 저장된 이메일에 해당하는 난수값 가져오기 -> setText / login ver.
            //Sign1에서 저장한 이메일 - 난수값
            SharedPreferences sharedPreferences = getSharedPreferences("Code",MODE_PRIVATE);
            String sharedkey = sharedPreferences.getString(getmyemail,"no_key_login");
            Log.d(TAG, "onCreate: sharedkey"+sharedkey);
            randomkey = sharedkey;
            Log.d(TAG, "onCreate: sharedkey키키"+randomkey);
        }else {
            /*signup1 -> signup2로 와 난수 인텐트 값이 있을때*/
            //sign1에서 왔을때
            String sign1key = intent.getStringExtra("randomKey");
            randomkey = sign1key;
            Log.d(TAG, "onCreate: randomkey키키"+randomkey);
        }
        //텍트스뷰에 난수값 붙히기
        Textview_Signup2_Mycode.setText(randomkey);


        /*연결하기 버튼 눌렀을 때*/
        Button_Signup2_Connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //randomkey와 입력받는 키를 서버로 보낸다
                //그값을 이용해 상대유저를찾는다
                connectCouple();
            }
        });



        /*비회원으로 앱 둘러보기 -> 메인액티비티로 이동*/
        Button_Signup2_Gomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RealMainActivity.class);
                startActivity(intent);
            }
        });

        /*새로고침 이미지 눌렀을때 -> 값이 있으면 메인액티비티로 이동*/
        Imageview_Sighup2_Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });

    }//OnCreate

    /*커플 연결하기
    * 고유키를 나와 상대방 커플키에 넣음 */
    public void connectCouple(){
        anotherkey = Edittext_Signup2_Anothercode.getText().toString(); //입력한 상대 코드
        if(anotherkey.isEmpty()){
            Toast.makeText(this, "상대방 코드를 입력해 주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String,Object> couplecode = new HashMap<>();
        couplecode.put("randomkey",randomkey);
        couplecode.put("anotherkey",anotherkey);
        retroClient.connect(couplecode, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }
            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code+receivedData);
                Result_login data = (Result_login) receivedData;
                /* connect.php 에서 나온 커플키($randomkey값) */
                String connectResult = data.getServerResult();
                Log.d(TAG, "onSuccess: 서버에서 가져온 커플키 == "+connectResult);

//                //연결버튼 눌렀을때 shared에 저장된 이메일을 새로 초기화해준다. (로그인에서 온것만 저장되잖아)
//                SharedPreferences sharedPreferences2 = getSharedPreferences("autologin",MODE_PRIVATE);
//                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
//                editor2.putString("auto_login",getmyemail);
//                editor2.commit();

                //로그인한 이메일값 가져오기
                SharedPreferences sharedPreferences1 = getSharedPreferences("autologin",MODE_PRIVATE);
                String sharedemail = sharedPreferences1.getString("auto_login","no_key");
                Log.d(TAG, "onSuccess: ==sharedemail는"+sharedemail);
                Log.d(TAG, "onSuccess: 인텐트로받은 getmyemail"+sharedemail);

                /*shared에 이메일-커플키로 저장*/
                SharedPreferences sharedPreferences = getSharedPreferences("CoupleKey",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(sharedemail,connectResult); /*로그인한 이메일 - 커플키*/
                editor.commit();

                Log.d(TAG, "randomkey 랜덤키: "+randomkey);
                Log.d(TAG, "서버에서 가져온 커플키: "+connectResult);
                if(connectResult.equals(randomkey)){
//                    Toast.makeText(Signup2Activity.this, "커플 연결 완료!"+connectResult, Toast.LENGTH_SHORT).show();
                    Toast.makeText(Signup2Activity.this, "커플 연결 완료!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),RealMainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(Signup2Activity.this, "커플 연결 실패!"+connectResult, Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });

    }//ConnectCouple()


    /*refresh 이미지 눌렀을때 내 유저정보에 couplekey값이 있으면 메인으로 이동하기*/
    public void refresh(){
        anotherkey = Edittext_Signup2_Anothercode.getText().toString(); //입력한 상대 코드 없음
        HashMap<String,Object> refresh = new HashMap<>();
        refresh.put("randomkey",randomkey);
        refresh.put("anotherkey",anotherkey); //사용 X
        Log.d(TAG, "refresh: randomkey"+randomkey);

        retroClient.refresh(refresh, new RetroCallback() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Result_login data = (Result_login) receivedData;
                String refreshResult =  data.getServerResult(); //서버에서 보낸 커플키값(상대 난수값)
                String getcouplekey = data.getCouplekey();

                //로그인값 가져오기
                SharedPreferences sharedPreferences1 = getSharedPreferences("autologin",MODE_PRIVATE);
                String sharedemail = sharedPreferences1.getString("auto_login","no_key");


                Log.d(TAG, "onSuccess: refreshResult는"+refreshResult);
                Log.d(TAG, "onSuccess: getcouplekey는 "+getcouplekey);
                Log.d(TAG, "onSuccess: sharedemail는 "+sharedemail);
                //서버에서 보낸 커플키값이 상대 난수값과 맞으면
                if(refreshResult.contentEquals("couplekeytrue")){
//                    Toast.makeText(Signup2Activity.this, "상대방과 연결되었습니다."+refreshResult, Toast.LENGTH_SHORT).show();
                    Toast.makeText(Signup2Activity.this, "상대방과 연결되었습니다.", Toast.LENGTH_SHORT).show();
                    /*이메일 - 서버에서 보낸 커플키값으로 저장*/
                    SharedPreferences sharedPreferences = getSharedPreferences("CoupleKey",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(sharedemail,getcouplekey); /*로그인한 이메일 - 커플키*/
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(),RealMainActivity.class);
                    startActivity(intent);
                }else{
//                    Toast.makeText(Signup2Activity.this, "상대방이 아직 연결하지 않았습니다!"+refreshResult, Toast.LENGTH_SHORT).show();
                    Toast.makeText(Signup2Activity.this, "상대방이 아직 연결하지 않았습니다!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int code) {

            }
        });

    }//refresh()


}//End