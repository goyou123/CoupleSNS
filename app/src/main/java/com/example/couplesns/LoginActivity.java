package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.DataClass.UserData;
import com.example.couplesns.RetrofitJava.RetroBaseApiService;
import com.example.couplesns.RetrofitJava.RetroCallback;
import com.example.couplesns.RetrofitJava.RetroClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    EditText EditText_Login_Email,EditText_Login_Password;
    Button Button_Login_Login,Button_Login_SignUp;
    String inputEmail,inputPassword;

    RetroClient retroClient;

    Retrofit retrofit;
    RetroBaseApiService retroBaseApiService;

    ApplicationClass applicationClass;

    String hashPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //레트로핏 사용을 위해 retrofitClient class에 선언해놓은 인스턴스 생성
        retroClient = RetroClient.getInstance(this).createBaseApi();


        //applicationClass
        applicationClass = (ApplicationClass) getApplicationContext();

        EditText_Login_Email = (EditText) findViewById(R.id.EditText_Login_Email);
        EditText_Login_Password = (EditText) findViewById(R.id.EditText_Login_Password);
        Button_Login_Login = (Button) findViewById(R.id.Button_Login_Login);
        Button_Login_SignUp = (Button) findViewById(R.id.Button_Login_SignUp);


        //로그인 버튼 눌렀을때
        Button_Login_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //로그인시 입력한 정보 로그인.php로 보내기
                inputEmail = EditText_Login_Email.getText().toString();
                inputPassword = EditText_Login_Password.getText().toString();

                //입력한값을 해쉬맵형태로 (인터페이스에 있는 형태와 일치해야함)
                HashMap<String, Object> logininfo = new HashMap<>();
                logininfo.put("email",inputEmail);
                logininfo.put("password",inputPassword);
                Log.d("logininfo", "onClick: "+logininfo); //실행 ㅇㅋ



                //자동로그인을 위해 입력 email값 shared에 저장
                SharedPreferences sharedPreferences = getSharedPreferences("autologin",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("auto_login",inputEmail);
                editor.commit();


                /*로그인시 입력한 비밀번호 암호화*/
                hashPassword = applicationClass.encryptSHA256(inputPassword);

                //데이터를 서버로 보내 로그인 여부 판별
                retroClient.retroLogin(logininfo, new RetroCallback() {

                    @Override
                    public void onError(Throwable t) {
                        Log.d("레트로핏-로그인데이터 보내기", "onError: 1."+ t.toString());
                    }

                    @Override
                    public void onSuccess(int code, Object receivedData) {
                        Log.d("레트로핏-로그인데이터 보내기", "onSuccess: "+code+"/"+receivedData);
                        Result_login data = (Result_login) receivedData;
                        String getPassword =  data.getServerResult();
                        String getCouplekey = data.getCouplekey();

                        Log.d("로그인시 서버에서 가져온", "getPassword: "+getPassword);
                        Log.d("로그인시 서버에서 가져온", "getCouplekey: "+getCouplekey);

                        if(getPassword.equals(hashPassword)){
                            if(getCouplekey!=null){
                                //로그인 성공시


                                Toast.makeText(LoginActivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),RealMainActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(LoginActivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),Signup2Activity.class);
                                intent.putExtra("inputEmail",inputEmail);
                                startActivity(intent);
                            }

                        }else{
                            //아이디 비번이 일치하지 않거나 없을경우
                            Toast.makeText(LoginActivity.this, "아이디나 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                        }


//                        if(loginResult.equals("true")){
//                            //로그인 성공시(아이디 비번이 맞을경우)
//                            Toast.makeText(LoginActivity.this, "로그인 되었습니다."+loginResult, Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(),RealMainActivity.class);
//                            startActivity(intent);
//                        }else if(loginResult.equals("nokey")){
//                            //아이디 비번은 맞으나 커플키가 없을 경우
//                            Toast.makeText(LoginActivity.this, "로그인 되었습니다."+loginResult, Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(),Signup2Activity.class);
//                            intent.putExtra("inputEmail",inputEmail);
//                            startActivity(intent);
//                        }else {
//                            //아이디 비번이 일치하지 않거나 없을경우
//                            Toast.makeText(LoginActivity.this, "아이디나 비밀번호를 확인해주세요"+loginResult, Toast.LENGTH_SHORT).show();
//                        }
                    }

                    @Override
                    public void onFailure(int code) {
                        Log.d("레트로핏-로그인데이터 보내기", "onFailure: "+code);
                    }
                });

//                Call<UserData> call = retroBaseApiService.retroLogin(logininfo);
//                call.enqueue(new Callback<UserData>() {
//                    @Override
//                    public void onResponse(Call<UserData> call, Response<UserData> response) {
//                        //데이터가 받아지면 여기에
//                        Log.d("로그인클릭시 포스트", "onResponse: "+response.body().toString());
//                    }
//
//                    @Override
//                    public void onFailure(Call<UserData> call, Throwable t) {
//
//                    }
//                });


//                //로그인.php에서 데이터 받아오기 (true값을 받아와야함)
//                retroClient.logincheck("1", new RetroCallback() {
//                    @Override
//                    public void onError(Throwable t) {
//                        Log.d("레트로핏-로그인데이터 받기", "onFailure: "+t.toString());
//                        //여기로 옴
//                    }
//
//                    @Override
//                    public void onSuccess(int code, Object receivedData) {
//                        Log.d("레트로핏-로그인데이터 받기", "onSuccess: ");
//                        UserData data = (UserData) receivedData;
//                        String ddd = String.valueOf(data.getName());
//                        Toast.makeText(LoginActivity.this, "로그인데이터 받기", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(int code) {
//                        Log.d("레트로핏-로그인데이터 받기", "onFailure11: "+code);
//                    }
//                });

            }
        });




        //회원가입 버튼 클릭시 회원가입 액티비티로 이동
        Button_Login_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Signup1Activity.class);
                startActivity(intent);
            }
        });


    } //OnCreate

    public class NullOnEmptyConverterFactory extends Converter.Factory {

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
            return new Converter<ResponseBody, Object>() {
                @Override
                public Object convert(ResponseBody body) throws IOException {
                    if (body.contentLength() == 0) return null;
                    return delegate.convert(body);
                }
            };
        }
    }
}// END