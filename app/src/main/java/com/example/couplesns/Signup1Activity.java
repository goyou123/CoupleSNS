package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.couplesns.DataClass.UserData;
import com.example.couplesns.RetrofitJava.RetroCallback;
import com.example.couplesns.RetrofitJava.RetroClient;

import java.util.HashMap;
import java.util.UUID;

public class Signup1Activity extends AppCompatActivity {
    Button Button_Signup1_Signup,Button_SignUp_EmailCheck;
    CheckBox checkbox1;
    RadioGroup radioGroup1;
    RadioButton Radio_Signup1_Male,Radio_Signup1_Female;
    EditText EditText_Signup1_Name,EditText_Signup1_Email,EditText_Signup1_Phone,EditText_Signup1_Password,EditText_Signup1_PwCheck;
    ImageView Imageview_Signup1_Checkimg;

    RadioButton radioButton;
    private RetroClient retroClient;
    String name,email,phone,password,pwcheck,sex,randomKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);

        //레트로핏 사용을 위해 레트로핏 인스턴스 생성
        retroClient = RetroClient.getInstance(this).createBaseApi();

        //레이아웃과 연결
        EditText_Signup1_Name = (EditText) findViewById(R.id.EditText_Signup1_Name);
        EditText_Signup1_Email = (EditText) findViewById(R.id.EditText_Signup1_Email);
        EditText_Signup1_Phone = (EditText) findViewById(R.id.EditText_Signup1_Phone);
        EditText_Signup1_Password = (EditText) findViewById(R.id.EditText_Signup1_Password);
        EditText_Signup1_PwCheck = (EditText) findViewById(R.id.EditText_Signup1_PwCheck);
        Imageview_Signup1_Checkimg = (ImageView) findViewById(R.id.Imageview_Signup1_Checkimg);
        checkbox1 = (CheckBox) findViewById(R.id.checkbox1);
        Button_Signup1_Signup = (Button) findViewById(R.id.Button_Signup1_Signup);
        Button_SignUp_EmailCheck = (Button) findViewById(R.id.Button_SignUp_EmailCheck);
        radioGroup1 = (RadioGroup) findViewById(R.id.RadioGroup1);


        /*이용약관에 체크해야지 회원가입 버튼 활성화
        * 후에 이용약관 클릭시 링크 추가*/
        Button_Signup1_Signup.setEnabled(false);
        checkbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkbox1.isChecked()){
                    Button_Signup1_Signup.setEnabled(true);
                    Button_Signup1_Signup.setBackground(getResources().getDrawable(R.drawable.coner_main_full));
                }else{
                    Button_Signup1_Signup.setEnabled(false);
                    Button_Signup1_Signup.setBackground(getResources().getDrawable(R.drawable.coner_sub_full));
                }
            }
        });



        /*비밀번호 일치 검사
         * 텍스트 변화감지*/
        EditText_Signup1_PwCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //텍스트가 바뀔떄
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 비밀번호 입력값과 재입력값이 같은지 체크
                String Pw = EditText_Signup1_Password.getText().toString();
                String pwCheck = EditText_Signup1_PwCheck.getText().toString();
                Log.d("비번", "onTextChanged: "+Pw +"?"+pwCheck);

                if (Pw.equals(pwCheck)){
                    // 서로 같으면 체크
                    Imageview_Signup1_Checkimg.setImageResource(R.drawable.check);
                } else if(pwCheck.isEmpty()){
                    // 비밀번호재입력값이 없으면 null
                    Imageview_Signup1_Checkimg.setImageResource(0);
                }else{
                    // 틀리면 X
                    Imageview_Signup1_Checkimg.setImageResource(R.drawable.xxxxx);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        /*회원가입 버튼 눌렀을때 */
        Button_Signup1_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 레트로핏으로 서버와 통신하여 회원가입 진행.
                // 고유 코드를 같이 생성한다.
                 signUp1();
                Log.d("SignupActivity1 랜덤키", "onClick: "+randomKey);
                Intent intent = new Intent(getApplicationContext(),Signup2Activity.class);
                intent.putExtra("randomKey",randomKey);
                startActivity(intent);
            }
        });




    }//OnCreate


    /*회원가입 하면 작성데이터 + 개인코드 생성해서 같이 저장*/
    public void signUp1(){
        //라디오 버튼 값 (성별 가져오기)
        int id = radioGroup1.getCheckedRadioButtonId(); //라디오 그룹 아이디
        radioButton = (RadioButton) findViewById(id); //아이디 값에 따른 라디오 값

        //고유값 생성 함수 - return randomKey;
        makeCode();

        //입력받은 버튼 값
        sex = radioButton.getText().toString(); //남자 or 여자
        name = EditText_Signup1_Name.getText().toString();
        email = EditText_Signup1_Email.getText().toString();
        phone = EditText_Signup1_Phone.getText().toString();
        password = EditText_Signup1_Password.getText().toString();
        pwcheck = EditText_Signup1_PwCheck.getText().toString();
//        Toast.makeText(this, "성별"+sex, Toast.LENGTH_SHORT).show();


        //공백처리
        if(name.isEmpty()||email.isEmpty()||phone.isEmpty()||password.isEmpty()||pwcheck.isEmpty()){
            Toast.makeText(Signup1Activity.this,"공백은 입력하실 수 없습니다.",Toast.LENGTH_SHORT).show();
            return;
        }
//        if(password!=pwcheck){
//            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }

        //해쉬맵 형태로 저장하고 (해쉬맵은 인터페이스에서 선언되있음)
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("email", email);
        parameters.put("phone", phone);
        parameters.put("password", password);
//        parameters.put("pwcheck", pwcheck);
        parameters.put("myCode",randomKey);
//        parameters.put("other","null");
//        parameters.put("profileimg","null");
//        parameters.put("couplekey","null");
        parameters.put("sex",sex);
        Log.d("실행체크", "여기까지 실행"+parameters); // 실행됨

        //shared에 이메일 - 랜덤키 값 형태로 저장 (Signup2에서 사용)
        SharedPreferences sharedPreferences = getSharedPreferences("Code",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(email,randomKey);
        Log.d("수정", "signUp1: "+email+" / " +randomKey);
        editor.commit();

        //자동로그인을 위해 입력 email값 shared에 저장 =>로그인에도 있음
        SharedPreferences sharedPreferences1 = getSharedPreferences("autologin",MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putString("auto_login",email);
        Log.d("수정", "signUp1: "+"auto_login"+" / " +email);
        editor1.commit();


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
                Log.d("새로운서버", "onSuccess: ");
                //여기는 데이터를 받는 곳   --> JSON 데이터를 읽어올 수 있다.
                //데이터를 받아와 Edittext에 붙혀본다
                //받은 데이터 없으므로 당연히 null값

            }

            @Override
            public void onFailure(int code) {
                Log.d("레트로핏실패", "onSuccess: "+code);

            }
        });



    } //Signup1()



    /*랜덤 고유값 생성 - 후에 커플키로 사용*/
    public String makeCode(){
        //개인 코드 생성 - 랜덤으로 만든 UUID 앞자리 8자리만 가져옴(고유값)
        UUID ran = UUID.randomUUID();
        String rand = ran.toString();
        randomKey = rand.substring(0,8);
        Log.d("랜덤", "signUp1: "+ran);
        Log.d("랜덤", "onClick: "+randomKey);
        return randomKey;
    } //makeCode()






    /*레트로핏 에러*/
    private void setError(String errorString){
        Log.e("레트로핏 -- 에러", errorString);
    }

}//END