package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.couplesns.DataClass.UserData;
import com.example.couplesns.RequestBody.RequestPut;
import com.example.couplesns.ResponseBody.ResponseGet;
import com.example.couplesns.RetrofitJava.RetroBaseApiService;
import com.example.couplesns.RetrofitJava.RetroCallback;
import com.example.couplesns.RetrofitJava.RetroClient;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    EditText EditText_SignUp_Name,EditText_SignUp_Email,EditText_SignUp_Phonenum,EditText_SignUp_PassWord,EditText_SignUp_PWCheck;
    Button Button_SignUp_Signup,Button_SignUp_Cancel;
    ImageView ImageView_SignUp_PwCheck;
    TextView MainText;
    RetroBaseApiService retroBaseApiService;
    private RetroClient retroClient;

    String name,email,phone,password,pwcheck;
//    int phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //레트로핏 사용을 위해 레트로핏 인스턴스 생성
        retroClient = RetroClient.getInstance(this).createBaseApi();

        //레이아웃과 연결
        MainText = (TextView) findViewById(R.id.MainText);
        EditText_SignUp_Name = (EditText) findViewById(R.id.EditText_SignUp_Name);
        EditText_SignUp_Email = (EditText) findViewById(R.id.EditText_SignUp_Email);
        EditText_SignUp_Phonenum = (EditText) findViewById(R.id.EditText_SignUp_Phonenum);
        EditText_SignUp_PassWord = (EditText) findViewById(R.id.EditText_SignUp_PassWord);
        EditText_SignUp_PWCheck = (EditText) findViewById(R.id.EditText_SignUp_PWCheck);
        Button_SignUp_Signup = (Button) findViewById(R.id.Button_SignUp_Signup);
        Button_SignUp_Cancel = (Button) findViewById(R.id.Button_SignUp_Cancel);
        ImageView_SignUp_PwCheck = (ImageView) findViewById(R.id.ImageView_SignUp_PwCheck);


        /*비밀번호 중복 체크
         * 텍스트 변화감지*/
        EditText_SignUp_PWCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { //텍스트가 바뀔떄
                String Pw = EditText_SignUp_PassWord.getText().toString(); // 비밀번호 입력값
                String pwCheck = EditText_SignUp_PWCheck.getText().toString(); //비밀번호 재입력값
                Log.d("비번", "onTextChanged: "+Pw +"?"+pwCheck);
                if (Pw.equals(pwCheck)){ // 서로 같으면 체크
                    ImageView_SignUp_PwCheck.setImageResource(R.drawable.check);
                } else if(pwCheck.isEmpty()){ // 비밀번호재입력값이 없으면 null
                    ImageView_SignUp_PwCheck.setImageResource(0);
                }else{ // 틀리면 X
                    ImageView_SignUp_PwCheck.setImageResource(R.drawable.xxxxx);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        //예쩨 ) 취소버튼 누르면
        Button_SignUp_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //유저 데이터 가져와보기
                Toast.makeText(getApplicationContext(),"GET 2 Clicked", Toast.LENGTH_SHORT).show();
//                retroClient.getUserData("1", new RetroCallback() {
//                    @Override
//                    public void onError(Throwable t) {
//                        setError(t.toString());
//                    }
//
//                    @Override
//                    public void onSuccess(int code, Object receivedData) {
//                        List<UserData> data = (List<UserData>) receivedData;
//                        Log.d("데이터 불러오기", "onSuccess: "+data.get(0).getName());
//                        MainText.setText(data.get(0).getName());
//                    }
//
//                    @Override
//                    public void onFailure(int code) {
//
//                    }
//                });
                finish();
            }
        });




        //회원가입 버튼을 누르면
        Button_SignUp_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입때 입력한 이름,이메일,핸드폰번호를 가져와
                name = EditText_SignUp_Name.getText().toString();
                email = EditText_SignUp_Email.getText().toString();
//                phone = Integer.parseInt(EditText_SignUp_Phonenum.getText().toString());
                phone = EditText_SignUp_Phonenum.getText().toString();
                password = EditText_SignUp_PassWord.getText().toString();
                pwcheck = EditText_SignUp_PWCheck.getText().toString();

                //공백처리
                if(name.isEmpty()||email.isEmpty()||phone.isEmpty()||password.isEmpty()||pwcheck.isEmpty()){
                    Toast.makeText(SignUpActivity.this,"공백은 입력하실 수 없습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }



                //로그로 확인 완료
                Log.d("회원", "onClick: " +name+"/"+email+"/"+phone);
//                Toast.makeText(getApplicationContext(), "회원가입 버튼클릭"+phone, Toast.LENGTH_SHORT).show();

                //해쉬맵 형태로 저장하고 (해쉬맵은 인터페이스에서 선언되있음)
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("name", name);
                parameters.put("email", email);
                parameters.put("phone", phone);
                parameters.put("password", password);
                parameters.put("pwcheck", pwcheck);
                parameters.put("state",0);
                parameters.put("other","null");
                parameters.put("profileimg","null");
                parameters.put("couplekey","null");
                Log.d("실행체크", "여기까지 실행"+parameters); // 실행됨


                //레트로핏을 이용해 서버와 연동
                retroClient.plususer(parameters, new RetroCallback() {
                   @Override
                   public void onError(Throwable t) {
                       Log.d("레트로핏", "onError: 1.");
                       setError(t.toString());

                   }

                   //성공 콜백함수가 실행되면
                   @Override
                   public void onSuccess(int code, Object receivedData) {
                       //여기는 데이터를 받는 곳   --> JSON 데이터를 읽어올 수 있다.
                       //데이터를 받아와 Edittext에 붙혀본다
                       //받은 데이터 없으므로 당연히 null값
                       UserData data = (UserData) receivedData;
                       Log.d("레트로핏유저데이터", "onSuccess: "+data.getEmail());
//                       MainText.setText(String.valueOf(data.getName()));
                   }

                   @Override
                   public void onFailure(int code) {
                       Log.d("레트로핏실패", "onSuccess: "+code);

                   }
               });

                //회원가입성공 토스트를 보내고 로그인화면으로 이동
                Toast.makeText(SignUpActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);

            }
        });


    } //OnCreate


    private void setError(String errorString){
        Log.e("레트로핏 -- 에러", errorString);
    }






} //END